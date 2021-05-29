package io.github.novemdecillion.adapter.web

import io.github.novemdecillion.adapter.db.SlideRepository
import io.github.novemdecillion.adapter.db.StudyRepository
import io.github.novemdecillion.adapter.db.UserRepository
import io.github.novemdecillion.adapter.id.IdGeneratorService
import io.github.novemdecillion.adapter.security.currentAccount
import io.github.novemdecillion.domain.*
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.AntPathMatcher
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.HandlerMapping
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.view.UrlBasedViewResolver
import java.time.OffsetDateTime
import java.util.*
import javax.servlet.http.HttpServletRequest


@ConstructorBinding
@ConfigurationProperties("app.slide")
data class AppSlideProperties(
  val rootPath: Resource
)

@Controller
@SessionAttributes(types = [SlideShowController.SlideProgress::class])
class SlideShowController(
  private val userRepository: UserRepository,
  private val studyRepository: StudyRepository,
  private val slideRepository: SlideRepository,
  private val appSlideProp: AppSlideProperties,
  private val idGeneratorService: IdGeneratorService,
) {

  enum class SlideAction {
    PREV,
    NEXT,
  }

  data class SlideProgress(
    var studyId: UUID? = null,
    var slideId: String? = null,
    var pageIndex: Int = 0
  ) {
    fun update(studyId: UUID? = null, slideId: String? = null, pageIndex: Int = 0) {
      this.studyId = studyId
      this.slideId = slideId
      this.pageIndex = pageIndex
    }
  }

  companion object {
    val antPathMatcher = AntPathMatcher()

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

  @ModelAttribute
  fun slideProgress(): SlideProgress {
    return SlideProgress()
  }

  @GetMapping(ENDING_PATH)
  fun ending(): String {
    return "ending"
  }

  fun currentUser(): User {
    val (accountName, realmId) = currentAccount()
    return userRepository.findByAccountNameAndRealmWithAuthority(accountName, realmId) ?: throw ResponseStatusException(
      HttpStatus.FORBIDDEN
    )
  }

//  @PostMapping("/slideshow/start/{slideId}")
//  @Transactional(rollbackFor = [Throwable::class])
//  fun startSlide(@PathVariable slideId: String, slideProgress: SlideProgress): String {
//    val currentUser = currentUser()
//    val startStudy = Study(
//      studyId = idGeneratorService.generate(),
//      slideId = slideId,
//      userId = currentUser.userId,
//      startAt = OffsetDateTime.now(),
//      status = StudyStatus.ON_GOING
//    )
//    studyRepository.insert(startStudy)
//    slideProgress.update(startStudy.studyId, slideId, 0)
//    return "${UrlBasedViewResolver.REDIRECT_URL_PREFIX}/slideshow/${startStudy.studyId}/"
//  }

  @GetMapping(path = ["/slideshow/{studyId}/**"])
  fun slideResource(
    @PathVariable studyId: UUID,
    slideProgress: SlideProgress,
    request: HttpServletRequest
  ): ResponseEntity<*> {
    val path = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE) as String
    val bestMatchPattern = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE) as String
    val finalPath = antPathMatcher.extractPathWithinPattern(bestMatchPattern, path)
    val pathInSlide = "${slideProgress.slideId}/${finalPath}"
    val pageResource = appSlideProp.rootPath.createRelative(pathInSlide)

    return ResponseEntity.ok(pageResource)
  }

  @GetMapping(path = ["/slideshow/{studyId}/"])
  @Transactional(rollbackFor = [Throwable::class])
  fun showSlide(@PathVariable studyId: UUID,
                @RequestParam chapter: Int?,
                slideProgress: SlideProgress,
                modelAndView: ModelAndView): ModelAndView {
    val currentUser = currentUser()

    handleStudy(studyId, currentUser.userId, slideProgress, chapter) { study, slide, currentChapter, chapterIndex, pageIndexInChapter ->

      when (currentChapter) {
        is ExplainChapter -> {
          val page = currentChapter.pages[pageIndexInChapter]
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
          modelAndView.viewName = if (currentChapter.path.isNullOrBlank()) "exam-template" else currentChapter.path
          modelAndView.model[PAGE_KEY] = currentChapter
          modelAndView.model[OPTION_KEY] = slide.config.option
          modelAndView.model[ANSWER_KEY] = study.answer[chapterIndex] ?: emptyMap<Int, List<String>>()
          if (1 == pageIndexInChapter) {
            modelAndView.model[CONFIRM_KEY] = true
            val scores = study.score[chapterIndex]?.questions?.map { it.scoring } ?: listOf()
            modelAndView.model[SCORES_KEY] = scores
            modelAndView.model[TOTAL_SCORE_KEY] = scores.sum()
          }
        }
        is SurveyChapter -> {
          modelAndView.viewName = if (currentChapter.path.isNullOrBlank()) "survey-template" else currentChapter.path
          modelAndView.model[PAGE_KEY] = currentChapter
          modelAndView.model[ANSWER_KEY] = study.answer[chapterIndex] ?: emptyMap<Int, String>()
          if (1 == pageIndexInChapter) {
            modelAndView.model[CONFIRM_KEY] = true
          }
        }
      }

      var updatedStudy = study
        .recordProgress(chapterIndex, pageIndexInChapter, slide.config.numberOfPages())
      updatedStudy = updatedStudy.updateStatus()
      studyRepository.update(updatedStudy)
    }
    return modelAndView
  }

  @PostMapping("/slideshow/{studyId}/")
  @Transactional(rollbackFor = [Throwable::class])
  fun slideAction(
    @PathVariable studyId: UUID,
    @RequestParam action: SlideAction,
    @RequestParam params: LinkedMultiValueMap<String, String>,
    slideProgress: SlideProgress
  ): String {

    lateinit var redirectUrl: String
    val currentUser = currentUser()

    handleStudy(studyId, currentUser.userId, slideProgress, null) { study, slide, chapter, chapterIndex, pageIndexInChapter ->
      redirectUrl = "$SLIDESHOW_PATH/${study.studyId}/"
      when (action) {
        SlideAction.PREV -> {
          if ((slideProgress.pageIndex - 1) < 0) {
            redirectUrl = ENDING_PATH
          } else {
            slideProgress.pageIndex--
          }
        }
        SlideAction.NEXT -> {
          var updatedStudy = study.copy()
          if (slide.config.numberOfPages() <= (slideProgress.pageIndex + 1)) {
            redirectUrl = ENDING_PATH
          } else {
            slideProgress.pageIndex++
          }
          when (chapter) {
            is ExamChapter ->
              if (0 == pageIndexInChapter) {
                val answer = convertToAnswer(params)
                updatedStudy = updatedStudy
                  .recordAnswer(
                    chapterIndex, answer,
                    chapter.chapterRecord(Study.convertForExamAnswer(answer), slide.config.option.scoringMethod)
                  )
              }
            is SurveyChapter ->
              if (0 == pageIndexInChapter) {
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
    return "${UrlBasedViewResolver.REDIRECT_URL_PREFIX}${redirectUrl}"
  }

  fun convertToAnswer(params: LinkedMultiValueMap<String, String>): Map<Int, List<String>> {
    return params
      .filter { it.key != ACTION_KEY }
      .map { (key, value) ->
        key.toInt() to value
      }
      .let {
        LinkedMultiValueMap(it.toMap())
      }
  }

  fun handleStudy(
    studyId: UUID, userId: UUID,
    slideProgress: SlideProgress,
    requestChapterIndex: Int?,
    callback: (study: Study, slide: Slide, chapter: IChapter, chapterIndex: Int, pageIndexInChapter: Int) -> Unit
  ) {
    val study = studyRepository.selectById(studyId, userId) ?: throw ResponseStatusException(HttpStatus.FORBIDDEN)

    val slide = slideRepository.loadSlide(study.slideId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    val (chapter, pageIndexInChapter) = if (requestChapterIndex == null) {
      slide.config.chapterAndPageIndex(slideProgress.pageIndex)
        ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    } else {
      IndexedValue(requestChapterIndex, slide.config.chapters[requestChapterIndex]) to 0
    }

    if ((slideProgress.studyId != studyId) || (slideProgress.slideId != slide.slideId)) {
      slideProgress.update(studyId, study.slideId, 0)
    }
    callback(study, slide, chapter.value, chapter.index, pageIndexInChapter)
  }
}