package io.github.novemdecillion.adapter.web

import graphql.kickstart.tools.GraphQLQueryResolver
import io.github.novemdecillion.slide.ExplainChapter
import io.github.novemdecillion.slide.SlideConfig
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@ConstructorBinding
@ConfigurationProperties("app.slide")
data class AppSlideProperties(
  val rootPath: Resource
)

@Configuration
class SlideShowConfig {

  @Bean
  fun slideRouter(prop: AppSlideProperties): RouterFunction<ServerResponse> = router {
    "/slideshow".nest {
      GET("/{classId}/**") { request ->
        val classId = request.pathVariable("classId")

        // TODO 講習IDから教材IDに変換してリソースを返す

        val pathContainer = request.requestPath().pathWithinApplication()
        val pathElementsCount = pathContainer.elements().size
        val resourcePath = if (6 <= pathElementsCount) {
          pathContainer.subPath(5, pathElementsCount).value()
        } else {
          "overview.html"
        }

        val resource = prop.rootPath.createRelative("sample/$resourcePath")

        // TODO existsすると性能的にどうなんだろう...
        if (resource.exists()) {
          EntityResponse.fromObject(resource).build()
        } else  {
          ServerResponse.notFound().build()
        }
      }
    }
  }
}

@Component
class SlideController: GraphQLQueryResolver {
  fun slides(): List<SlideConfig> {
    return listOf(SlideConfig("テスト", listOf(ExplainChapter("概要", listOf()))))
  }
}