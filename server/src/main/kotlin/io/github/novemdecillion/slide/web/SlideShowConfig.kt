package io.github.novemdecillion.slide.web

import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.kickstart.tools.GraphQLResolver
import io.github.novemdecillion.slide.ExplainChapter
import io.github.novemdecillion.slide.SlideConfig
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.FileUrlResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.server.PathContainer
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.reactive.function.server.*
import org.springframework.web.util.pattern.PathPatternParser
import reactor.core.publisher.Mono
import java.io.IOException
import java.io.UncheckedIOException
import java.nio.charset.StandardCharsets

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