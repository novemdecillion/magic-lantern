package io.github.novemdecillion.adapter.web

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@ConstructorBinding
@ConfigurationProperties("app.slide")
data class AppSlideProperties(
  val rootPath: Resource
)

@Controller
class SlideShowController(val appSlideProp: AppSlideProperties) {

  @GetMapping("/slideshow/{classId}/**")
  fun slideRouter(@PathVariable classId: String) {
        // TODO 講習IDから教材IDに変換してリソースを返す

//        val pathContainer = request.requestPath().pathWithinApplication()
//        val pathElementsCount = pathContainer.elements().size
//        val resourcePath = if (6 <= pathElementsCount) {
//          pathContainer.subPath(5, pathElementsCount).value()
//        } else {
//          "overview.html"
//        }
//
//        val resource = prop.rootPath.createRelative("sample/$resourcePath")
//
//        // TODO existsすると性能的にどうなんだろう...
//        if (resource.exists()) {
//          EntityResponse.fromObject(resource).build()
//        } else  {
//          ServerResponse.notFound().build()
//        }
  }
}
