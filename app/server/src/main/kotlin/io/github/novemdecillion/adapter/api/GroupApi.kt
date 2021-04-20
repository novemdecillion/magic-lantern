package io.github.novemdecillion.adapter.api

import graphql.kickstart.tools.GraphQLQueryResolver
import io.github.novemdecillion.adapter.db.GroupRepository
import io.github.novemdecillion.domain.Course
import io.github.novemdecillion.domain.Group
import io.github.novemdecillion.slide.SlideConfig
import org.springframework.stereotype.Component
import java.util.*

@Component
class GroupApi(private val groupRepository: GroupRepository): GraphQLQueryResolver {
  fun groups(): List<Group> {
    return groupRepository.selectAll()
  }

  fun courses(): List<Course> {
    // TODO 疑似コード
    return listOf(Course(UUID(0, 0), "test", null, null))
  }
}