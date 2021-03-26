package io.github.novemdecillion.adapter.api

import graphql.kickstart.tools.GraphQLQueryResolver
import io.github.novemdecillion.slide.SlideConfig
import org.springframework.stereotype.Component
import java.util.*

@Component
class CourseApi: GraphQLQueryResolver {
  data class Course(
    val courseId: UUID,
    val slide: SlideConfig
  )


  fun courses(): List<Course> {
    // TODO 疑似コード
//    return listOf(Course(UUID(0, 0), SlideConfig("テスト", listOf(ExplainChapter("概要", listOf())))))
    return listOf(Course(UUID(0, 0), SlideConfig("テスト", listOf())))
  }
}