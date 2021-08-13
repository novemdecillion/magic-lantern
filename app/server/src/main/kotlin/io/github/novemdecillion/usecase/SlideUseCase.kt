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
    const val COMPLETED_KEY = "completed"
    const val SCORES_KEY = "scores"
    const val TOTAL_SCORE_KEY = "totalScore"
    const val OPTION_KEY = "option"

    const val SLIDESHOW_PATH = "/slideshow"
    const val ENDING_PATH = "$SLIDESHOW_PATH/ending"

    const val DEFAULT_EXPLAIN_TEMPLATE = "explain-template"
    const val DEFAULT_EXAM_TEMPLATE = "exam-template"
    const val DEFAULT_SURVEY_TEMPLATE = "survey-template"
  }

  enum class SlideAction {
    PREV,
    NEXT,
  }

  fun chapterStartPageIndex(study: Study, slide: Slide, chapterIndex: Int): Pair<Int, Int> {
    var pageIndex = slide.chapterStartPageIndex(chapterIndex)
    var pageIndexInChapter = 0

    if (study.isComplete() && (slide.chapters[chapterIndex] !is ExplainChapter)) {
      pageIndex++
      pageIndexInChapter++
    }
    return pageIndex to pageIndexInChapter
  }

  fun showPage(
    study: Study,
    slide: Slide,
    chapterIndex: Int,
    pageIndexInChapter: Int,
    modelAndView: ModelAndView
  ) {
    var updatedStudy = study.copy()

    when (val chapter: IChapter = slide.chapters[chapterIndex]) {
      is ExplainChapter -> {
        val page = chapter.pages[pageIndexInChapter]
        modelAndView.viewName = if (page.path.isNullOrBlank()) {
          DEFAULT_EXPLAIN_TEMPLATE
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
        val shuffledAnswer = study.shuffledAnswer(chapterIndex)
        val examChapterRecord = study.score[chapterIndex]
        val shuffledChapter: ExamChapter = study.shuffledQuestion[chapterIndex]
          ?.let { shuffleTable ->
            chapter.replayShuffle(shuffleTable)
          }
          ?: run {
            chapter.shuffle()
              .let { (shuffledChap, shuffleTable) ->
                updatedStudy =
                  updatedStudy.copy(shuffledQuestion = study.shuffledQuestion.plus(chapterIndex to shuffleTable))
                shuffledChap
              }
          }

        modelAndView.viewName = if (shuffledChapter.path.isNullOrBlank()) DEFAULT_EXAM_TEMPLATE else shuffledChapter.path
        modelAndView.model[PAGE_KEY] = shuffledChapter
        modelAndView.model[OPTION_KEY] = slide.option
        modelAndView.model[COMPLETED_KEY] = study.isComplete()
        if ((null != shuffledAnswer) && (null != examChapterRecord)) {
          // 回答済み
          if (0 == pageIndexInChapter) {
            modelAndView.model[ANSWER_KEY] = if (examChapterRecord.isPass()) shuffledAnswer else emptyMap()
          } else if (1 == pageIndexInChapter) {
            // 採点ページ
            modelAndView.model[ANSWER_KEY] = shuffledAnswer
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
        modelAndView.viewName = if (chapter.path.isNullOrBlank()) DEFAULT_SURVEY_TEMPLATE else chapter.path
        modelAndView.model[PAGE_KEY] = chapter
        modelAndView.model[ANSWER_KEY] = study.answer[chapterIndex] ?: emptyMap<Int, String>()
        modelAndView.model[COMPLETED_KEY] = study.isComplete()
        if (1 == pageIndexInChapter) {
          modelAndView.model[CONFIRM_KEY] = true
        }
      }
    }

    // 未完了ならレコードを更新
    if (!study.isComplete()) {
      updatedStudy = updatedStudy
        .recordProgress(chapterIndex, pageIndexInChapter, slide.numberOfPages())
      updatedStudy = updatedStudy.updateStatus()
      studyRepository.update(updatedStudy)
    }
  }

  fun controlPage(
    study: Study, slide: Slide, pageIndex: Int, chapterIndex: Int, pageIndexInChapter: Int,
    action: SlideAction, params: LinkedMultiValueMap<String, String>
  ): Pair<String, Int> {
    var redirectUrl = "${SLIDESHOW_PATH}/${study.studyId}/"
    var resolvedPageIndex = pageIndex
    val chapter: IChapter = slide.chapters[chapterIndex]

    if (study.isComplete()) {
      when (action) {
        SlideAction.PREV -> {
          if ((resolvedPageIndex - 1) < 0) {
            redirectUrl = ENDING_PATH
          } else {
            resolvedPageIndex--
          }
          // 試験またはアンケートの章で、先頭ページの表示なら
          if (doIfExamOrSurveyPage(slide, resolvedPageIndex, 0)) {
            resolvedPageIndex--
          }
        }
        SlideAction.NEXT -> {
          if (slide.numberOfPages() <= (resolvedPageIndex + 1)) {
            redirectUrl = ENDING_PATH
          } else {
            resolvedPageIndex++
          }
          // 試験またはアンケートの章で、先頭ページの表示なら
          if (doIfExamOrSurveyPage(slide, resolvedPageIndex, 0)) {
            resolvedPageIndex++
          }
        }
      }
    } else {
      when (action) {
        SlideAction.PREV -> {
          if ((resolvedPageIndex - 1) < 0) {
            redirectUrl = ENDING_PATH
          } else {
            resolvedPageIndex--
          }

          // 後の章から逆戻りが試験またはアンケートの章で、回答がない?
          if (doIfExamOrSurveyPage(slide, resolvedPageIndex, 1)
            && (study.answer[chapterIndex] == null)
          ) {
            // 採点 or 確認 ページではなく、出題ページを表示させる
            resolvedPageIndex--
          }
        }
        SlideAction.NEXT -> {
          if (slide.numberOfPages() <= (resolvedPageIndex + 1)) {
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
                    chapter.chapterRecord(Study.convertForExamAnswer(answer), slide.option.scoringMethod)
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
    }

    return redirectUrl to resolvedPageIndex
  }

  private fun doIfExamOrSurveyPage(slide: Slide, pageIndex: Int, pageIndexInChapter: Int): Boolean {
    val (prevChapter, prevPageIndexInChapter) = slide.chapterAndPageIndex(pageIndex)!!
    return (prevChapter.value !is ExplainChapter)
      && (prevPageIndexInChapter == pageIndexInChapter)
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