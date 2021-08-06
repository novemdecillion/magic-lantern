package io.github.novemdecillion.adapter.db

import io.github.novemdecillion.adapter.web.AppSlideProperties
import io.github.novemdecillion.loadSlide
import io.kotest.core.spec.style.StringSpec
import org.assertj.core.api.Assertions
import org.springframework.core.io.FileSystemResource
import java.nio.file.Paths

class SlideRepositoryTest: StringSpec({
  "basicロード" {
    val slide = loadSlide("basic")
    Assertions.assertThat(slide).isNotNull
    Assertions.assertThat(slide?.chapters?.size).isEqualTo(4)
  }

  "10threats2019ロード" {
    val slide = loadSlide("10threats2019")
    Assertions.assertThat(slide).isNotNull
    Assertions.assertThat(slide?.chapters?.size).isEqualTo(6)
  }
})