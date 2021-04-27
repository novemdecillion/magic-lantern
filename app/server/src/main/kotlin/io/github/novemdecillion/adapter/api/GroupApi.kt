package io.github.novemdecillion.adapter.api

import graphql.kickstart.execution.context.DefaultGraphQLContext
import graphql.kickstart.servlet.context.DefaultGraphQLServletContext
import graphql.kickstart.tools.GraphQLMutationResolver
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.schema.DataFetchingEnvironment
import io.github.novemdecillion.adapter.db.GroupRepository
import io.github.novemdecillion.domain.Course
import io.github.novemdecillion.domain.Group
import io.github.novemdecillion.domain.Role
import io.github.novemdecillion.domain.User
import io.github.novemdecillion.slide.SlideConfig
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

@Component
class GroupApi(private val groupRepository: GroupRepository): GraphQLQueryResolver, GraphQLMutationResolver {
  data class AddGroupCommand(
    val groupOriginId: UUID?,
    val groupGenerationId: UUID?,
    val groupName: String,
    val parentGroupId: UUID
  )

  data class EditGroupCommand(
    val groupId: UUID,
    val groupName: String
  )

  data class GroupMemberCommand(
    val groupId: UUID,
    val userId: UUID,
    val role: List<Role>
  )

  fun groups(environment: DataFetchingEnvironment): List<Group> {
    // TODO 権限に応じて変える
    val user = environment.currentUser()
    return groupRepository.selectAll()
  }

  fun addGroup(command: AddGroupCommand, environment: DataFetchingEnvironment): Boolean {
    val now = environment.now()
    groupRepository.insertNewGroup(now, command.groupName, command.parentGroupId, command.groupGenerationId)
    return true
  }

  fun editGroup(command: EditGroupCommand): Boolean {
    // TODO
    groupRepository.updateGroup(command.groupId, command.groupName)
    return true
  }

  fun deleteGroup(groupTransitionId: UUID): Boolean {
    // TODO
    groupRepository.deleteGroup(groupTransitionId)
    return true
  }

  fun addGroupMember(command: GroupMemberCommand): Boolean {
    TODO()
  }
  fun editGroupMember(command: GroupMemberCommand): Boolean {
    TODO()
  }
  fun deleteGroupMember(groupId: UUID, userId: UUID): Boolean {
    TODO()
  }

  fun courses(): List<Course> {
    // TODO 疑似コード
    return listOf(Course(UUID(0, 0), "test", null, null))
  }
}