package io.github.novemdecillion.usecase

import io.github.novemdecillion.adapter.web.AppSlideProperties
import io.github.novemdecillion.domain.*
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.servlet.ModelAndView

@Component
class SlideUseCase(
  private val studyRepository: IStudyRepository,
  private val appSlideProp: AppSlideProperties
) {

  companion object {
    const val PAGE_KEY = "page"
    const val ANSWER_KEY = "answer"
    const val ACTION_KEY = "action"
    const val CONFIRM_KEY = "confirm"
    const val SCORES_KEY = "scores"
    const val TOTAL_SCORE_KEY = "totalScore"
    const val OPTION_KEY = "option"

    const val SLIDESHOW_PATH = "/slideshow"
    const val ENDING_PATH = "$SLIDESHOW_PATH/ending"
  }

  enum class SlideAction {
    PREV,
    NEXT,
  }

  fun showPage(
    study: Study,
    slide: Slide,
    chapter: IChapter,
    chapterIndex: Int,
    pageIndexInChapter: Int,
    modelAndView: ModelAndView
  ) {
    var updatedStudy = study.copy()

    when (chapter) {
      is ExplainChapter -> {
        val page = chapter.pages[pageIndexInChapter]
        modelAndView.viewName = if (page.path.isNullOrBlank()) {
          "explain-template"
        } else {
          val pathInSlide = "${slide.slideId}/${page.path}"
          val pageResource = appSlideProp.rootPath.createRelative(pathInSlide)
          if (pageResource.exists()) {
            pathInSlide
          } else {
            page.path
          }
        }
        modelAndView.model[PAGE_KEY] = page
      }
      is ExamChapter -> {
        val answer = study.shuffledAnswer(chapterIndex)
        val examChapterRecord = study.score[chapterIndex]
        val shuffledChapter: ExamChapter = study.shuffledQuestion[chapterIndex]
          ?.let { shuffleTable ->
            chapter.shuffled(shuffleTable)
          }
          ?: run {
            chapter.shuffled()
              .let {
                updatedStudy =
                  updatedStudy.copy(shuffledQuestion = study.shuffledQuestion.plus(chapterIndex to it.second))
                it.first
              }
          }

        modelAndView.viewName = if (shuffledChapter.path.isNullOrBlank()) "exam-template" else shuffledChapter.path
        modelAndView.model[PAGE_KEY] = shuffledChapter
        modelAndView.model[OPTION_KEY] = slide.config.option

        if ((null != answer) && (null != examChapterRecord)) {
          // 回答済み
          if (0 == pageIndexInChapter) {
            modelAndView.model[ANSWER_KEY] = if (examChapterRecord.isPass()) answer else emptyMap()
          } else if (1 == pageIndexInChapter) {
            // 採点ページ
            modelAndView.model[ANSWER_KEY] = answer
            modelAndView.model[CONFIRM_KEY] = true
            val scores = examChapterRecord.questions.map { it.scoring }
            modelAndView.model[SCORES_KEY] = scores
            modelAndView.model[TOTAL_SCORE_KEY] = scores.sum()
          }
        } else {
          // 未回答
          // 問題ページ
          modelAndView.model[ANSWER_KEY] = emptyMap<Int, List<String>>()

        }
      }
      is SurveyChapter -> {
        modelAndView.viewName = if (chapter.path.isNullOrBlank()) "survey-template" else chapter.path
        modelAndView.model[PAGE_KEY] = chapter
        modelAndView.model[ANSWER_KEY] = study.answer[chapterIndex] ?: emptyMap<Int, String>()
        if (1 == pageIndexInChapter) {
          modelAndView.model[CONFIRM_KEY] = true
        }
      }
    }

    updatedStudy = updatedStudy
      .recordProgress(chapterIndex, pageIndexInChapter, slide.config.numberOfPages())
    updatedStudy = updatedStudy.updateStatus()
    studyRepository.update(updatedStudy)
  }

  fun controlPage(
    study: Study, slide: Slide, pageIndex: Int, chapter: IChapter, chapterIndex: Int, pageIndexInChapter: Int,
    action: SlideAction, params: LinkedMultiValueMap<String, String>
  ): Pair<String, Int> {
    var redirectUrl = "${SLIDESHOW_PATH}/${study.studyId}/"
    var resolvedPageIndex = pageIndex
    when (action) {
      SlideAction.PREV -> {
        if ((resolvedPageIndex - 1) < 0) {
          redirectUrl = ENDING_PATH
        } else {
          resolvedPageIndex--
        }

        val (prevChapter, prevPageIndexInChapter) = slide.config.chapterAndPageIndex(resolvedPageIndex)!!
        if ((prevChapter.value !is ExplainChapter)
          && (1 == prevPageIndexInChapter)
        ) {
          // 後の章から逆戻り
          // 回答がない?
          if (study.answer[chapterIndex] == null) {
            resolvedPageIndex--
          }
        }
      }
      SlideAction.NEXT -> {
        if (slide.config.numberOfPages() <= (resolvedPageIndex + 1)) {
          redirectUrl = ENDING_PATH
        } else {
          resolvedPageIndex++
        }

        if ((chapter !is ExplainChapter)
          && (0 == pageIndexInChapter)
        ) {
          var updatedStudy = study.copy()
          when (chapter) {
            is ExamChapter -> {
              val answer = convertToAnswer(params, study.shuffledQuestion[chapterIndex]!!)
              updatedStudy = updatedStudy
                .recordAnswer(
                  chapterIndex, answer,
                  chapter.chapterRecord(Study.convertForExamAnswer(answer), slide.config.option.scoringMethod)
                )
            }
            is SurveyChapter -> {
              val answer = convertToAnswer(params)
              updatedStudy = updatedStudy
                .recordAnswer(chapterIndex, answer)
            }
          }
          updatedStudy = updatedStudy.updateStatus()
          studyRepository.update(updatedStudy)
        }

      }
    }


    return redirectUrl to resolvedPageIndex
  }

  private fun convertToAnswer(params: LinkedMultiValueMap<String, String>): Map<Int, List<String>> {
    return params
      .filter { it.key != ACTION_KEY }
      .map { (key, value) ->
        key.toInt() to value
      }
      .let {
        LinkedMultiValueMap(it.toMap())
      }
  }

  private fun convertToAnswer(
    params: LinkedMultiValueMap<String, String>,
    shuffled: List<Pair<Int, List<Int>>>
  ): Map<Int, List<String>> {
    val convertedParam = convertToAnswer(params)
    return convertedParam
      .map { (questionIndex, answers) ->
        val (shuffledQuestionIndex, shuffledChoices) = shuffled[questionIndex]
        shuffledQuestionIndex to answers.map { shuffledChoices[it.toInt()].toString() }
      }
      .toMap()
  }
}