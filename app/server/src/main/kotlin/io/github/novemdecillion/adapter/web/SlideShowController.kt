package io.github.novemdecillion.adapter.web

import io.github.novemdecillion.adapter.db.SlideRepository
import io.github.novemdecillion.adapter.db.StudyRepository
import io.github.novemdecillion.adapter.db.UserRepository
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
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.HandlerMapping
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes
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
@Transactional(rollbackFor = [Throwable::class])
@SessionAttributes(types = [SlideShowController.SlideProgress::class])
class SlideShowController(val userRepository: UserRepository,
                          val studyRepository: StudyRepository,
                          val slideRepository: SlideRepository,
                          val appSlideProp: AppSlideProperties) {

  enum class SlideAction {
    PREV,
    NEXT,
  }

  data class SlideProgress(
    var studyId: UUID? = null,
    var slideId: String? = null,
    var pageIndex: Int = 0) {
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

  @PostMapping("/slideshow/start/{slideId}")
  fun startSlide(@PathVariable slideId: String, slideProgress: SlideProgress): String {
    val (accountName, realmId) = currentAccount()
    val currentUser = userRepository.findByAccountNameAndRealmWithAuthority(accountName, realmId) ?: throw ResponseStatusException(HttpStatus.FORBIDDEN)
    val startedStudy = studyRepository.insert(Study(slideId = slideId, userId = currentUser.userId, startAt = OffsetDateTime.now()))
    slideProgress.update(startedStudy.studyId, slideId, 0)
    return "${UrlBasedViewResolver.REDIRECT_URL_PREFIX}/slideshow/${startedStudy.studyId}/"
  }

  @GetMapping(path = ["/slideshow/{studyId}/**"])
  fun slideResource(@PathVariable studyId: UUID, slideProgress: SlideProgress, request: HttpServletRequest): ResponseEntity<*> {
    val path = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE) as String
    val bestMatchPattern = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE) as String
    val finalPath = antPathMatcher.extractPathWithinPattern(bestMatchPattern, path)
    val pathInSlide = "${slideProgress.slideId}/${finalPath}"
    val pageResource = appSlideProp.rootPath.createRelative(pathInSlide)

    return ResponseEntity.ok(pageResource)
  }

  @GetMapping(path = ["/slideshow/{studyId}/"])
  fun showSlide(@PathVariable studyId: UUID, slideProgress: SlideProgress, modelAndView: ModelAndView): ModelAndView {
    handleStudy(studyId, slideProgress) { study, slide, chapter, chapterIndex, pageIndexInChapter ->

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
          modelAndView.viewName = if (chapter.path.isNullOrBlank()) "exam-template" else chapter.path
          modelAndView.model[PAGE_KEY] = chapter
          val answer = study.answer[chapterIndex] ?: mapOf()
          modelAndView.model[ANSWER_KEY] = answer
          if (1 == pageIndexInChapter) {
            modelAndView.model[CONFIRM_KEY] = true
            val scores = chapter
              .scores(answer, slide.config.option.scoringMethod)
            modelAndView.model[SCORES_KEY] = scores
            modelAndView.model[TOTAL_SCORE_KEY] = scores.sum()
          }
        }
        is SurveyChapter -> {
          modelAndView.viewName = if (chapter.path.isNullOrBlank()) "survey-template" else chapter.path
          modelAndView.model[PAGE_KEY] = chapter
          modelAndView.model[ANSWER_KEY] = study.answer[chapterIndex]
          if (1 == pageIndexInChapter) {
            modelAndView.model[CONFIRM_KEY] = true
          }
        }
      }
    }
    return modelAndView
  }


  @PostMapping("/slideshow/{studyId}/")
  fun slideAction(@PathVariable studyId: UUID, @RequestParam action: SlideAction, @RequestParam params: LinkedMultiValueMap<Int, Int>,
                  slideProgress: SlideProgress): String {

    lateinit var redirectUrl: String

    handleStudy(studyId, slideProgress) { study, slide, chapter, chapterIndex, pageIndexInChapter ->
      redirectUrl = "$SLIDESHOW_PATH/${study.studyId}/"
      when(action) {
        SlideAction.PREV -> {
          if ((slideProgress.pageIndex - 1) < 0) {
            redirectUrl = ENDING_PATH
          } else {
            slideProgress.pageIndex--
          }
        }
        SlideAction.NEXT -> {
          if (slide.config.numberOfPages() <= (slideProgress.pageIndex + 1)) {
            redirectUrl = ENDING_PATH
          } else {
            slideProgress.pageIndex++
          }
        }
      }
      when (chapter) {
        is ExamChapter, is SurveyChapter -> {
          if (0 == pageIndexInChapter) {
            params.remove(ACTION_KEY)
            val updatedStudy = study.copy(answer = study.answer.plus(chapterIndex to params))
            studyRepository.update(updatedStudy)
          }
        }
      }
    }
    return "${UrlBasedViewResolver.REDIRECT_URL_PREFIX}${redirectUrl}"
  }

  fun handleStudy(studyId: UUID, slideProgress: SlideProgress, callback: (study: Study, slide: Slide, chapter: IChapter, chapterIndex: Int, pageIndexInChapter: Int) -> Unit) {
    val study = studyRepository.selectById(studyId) ?: throw ResponseStatusException(HttpStatus.FORBIDDEN)

    val slide =slideRepository.loadSlide(study.slideId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    val (chapter, pageIndexInChapter) = slide.config.chapterAndPageIndex(slideProgress.pageIndex)
      ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    if ((slideProgress.studyId != studyId) || (slideProgress.slideId != slide.slideId)) {
      slideProgress.update(studyId, study.slideId, 0)
    }
    callback(study, slide, chapter.value, chapter.index, pageIndexInChapter)
  }
}
