package io.github.novemdecillion

import io.github.novemdecillion.adapter.db.SlideRepository
import io.github.novemdecillion.adapter.web.AppSlideProperties
import io.github.novemdecillion.domain.Slide
import org.springframework.core.io.FileSystemResource

fun loadSlide(slideId: String): Slide? {
  val slideRootResource = FileSystemResource("src/test/resources/slides/")
  val prop = AppSlideProperties(slideRootResource)
  val slideRepo = SlideRepository(prop)
  return slideRepo.loadSlide(slideId)
}