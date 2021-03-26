package io.github.novemdecillion.adapter.api

import graphql.kickstart.tools.GraphQLQueryResolver
import io.github.novemdecillion.slide.ExplainChapter
import io.github.novemdecillion.slide.SlideConfig
import org.springframework.stereotype.Component

@Component
class SlideApi: GraphQLQueryResolver {
  fun slides(): List<SlideConfig> {
    return listOf(SlideConfig("テスト", listOf(ExplainChapter("概要", listOf()))))
  }
}