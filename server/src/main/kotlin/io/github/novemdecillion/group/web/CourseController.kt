package io.github.novemdecillion.group.web

import graphql.kickstart.tools.GraphQLQueryResolver
import io.github.novemdecillion.group.Course
import io.github.novemdecillion.slide.ExplainChapter
import io.github.novemdecillion.slide.SlideConfig
import org.springframework.stereotype.Component
import java.util.*

@Component
class CourseController: GraphQLQueryResolver {
  fun courses(): List<Course> {
    // TODO 疑似コード
    return listOf(Course(UUID(0, 0), SlideConfig("テスト", listOf(ExplainChapter("概要", listOf())))))
  }
}