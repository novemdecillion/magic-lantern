package io.github.novemdecillion.adapter.web

import io.github.novemdecillion.adapter.db.AccountRepository
import io.github.novemdecillion.adapter.db.SlideRepository
import io.github.novemdecillion.adapter.db.StudyRepository
import io.github.novemdecillion.adapter.security.currentAccount
import io.github.novemdecillion.domain.*
import io.github.novemdecillion.usecase.SlideUseCase
import io.github.novemdecillion.usecase.SlideUseCase.Companion.ENDING_PATH
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
  private val userRepository: AccountRepository,
  private val studyRepository: StudyRepository,
  private val slideRepository: SlideRepository,
  private val slideUseCase: SlideUseCase,
  private val appSlideProp: AppSlideProperties
) {

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
    return userRepository.selectByAccountNameAndRealmWithAuthority(accountName, realmId) ?: throw ResponseStatusException(
      HttpStatus.FORBIDDEN
    )
  }

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
      slideUseCase.showPage(study, slide, currentChapter, chapterIndex, pageIndexInChapter, modelAndView)
    }

    return modelAndView
  }

  @PostMapping("/slideshow/{studyId}/")
  @Transactional(rollbackFor = [Throwable::class])
  fun slideAction(
    @PathVariable studyId: UUID,
    @RequestParam action: SlideUseCase.SlideAction,
    @RequestParam params: LinkedMultiValueMap<String, String>,
    slideProgress: SlideProgress
  ): String {

    val currentUser = currentUser()

    val redirectUrl = handleStudy(studyId, currentUser.userId, slideProgress, null) { study, slide, chapter, chapterIndex, pageIndexInChapter ->
      slideUseCase.controlPage(study, slide, slideProgress.pageIndex, chapter, chapterIndex, pageIndexInChapter, action, params)
        .let { (url, pageIndex) ->
          slideProgress.pageIndex = pageIndex
          url
        }
    }
    return "${UrlBasedViewResolver.REDIRECT_URL_PREFIX}${redirectUrl}"
  }

  fun <T> handleStudy(
    studyId: UUID, userId: UUID,
    slideProgress: SlideProgress,
    requestChapterIndex: Int?,
    callback: (study: Study, slide: Slide, chapter: IChapter, chapterIndex: Int, pageIndexInChapter: Int) -> T
  ): T {
    val study = studyRepository.selectById(studyId) ?: throw ResponseStatusException(HttpStatus.FORBIDDEN)

    val slide = slideRepository.loadSlide(study.slideId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    if ((slideProgress.studyId != studyId) || (slideProgress.slideId != slide.slideId)) {
      slideProgress.update(studyId, study.slideId, 0)
    }

    val (chapter, pageIndexInChapter) = if (requestChapterIndex == null) {
      slide.config.chapterAndPageIndex(slideProgress.pageIndex)
        ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    } else {
      slideProgress.pageIndex = slide.config.chapterStartPageIndex(requestChapterIndex)
      IndexedValue(requestChapterIndex, slide.config.chapters[requestChapterIndex]) to 0
    }

    return callback(study, slide, chapter.value, chapter.index, pageIndexInChapter)
  }
}
