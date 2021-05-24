package io.github.novemdecillion.adapter.api

import graphql.kickstart.tools.GraphQLMutationResolver
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.schema.DataFetchingEnvironment
import io.github.novemdecillion.adapter.db.GroupRepository
import io.github.novemdecillion.domain.*
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

  data class UpdateGroupCommand(
    val groupId: UUID,
    val groupName: String
  )

  fun authoritativeGroups(role: Role, environment: DataFetchingEnvironment): List<GroupWithPath> {
    return environment.currentUser().authorities
      .filter { it.roles.contains(role) }
      .map { it.groupId }
      .let {
        groupRepository.selectByIds(it)
      }
  }

  fun effectiveGroups(role: Role, environment: DataFetchingEnvironment): List<GroupWithPath> {
    return environment.currentUser().authorities
      .filter { it.roles.contains(role) }
      .map { it.groupId }
      .let {
        groupRepository.selectChildrenByIds(it)
      }
  }

//  fun topManageableGroups(environment: DataFetchingEnvironment): List<GroupWithPath> {
//    val manageableGroupIds = environment.currentUser().authorities
//      ?.filter { it.roles.contains(Role.MANAGER) }
//      ?.map { it.groupId }
//      ?: return listOf()
//    val groups = groupRepository.selectByIds(manageableGroupIds).map { group -> group.groupIdPath() to group }
//
//    return groups
//      .filter { group ->
//        groups.firstOrNull { path -> path.first.contains(group.first) }?.let { false } ?: true
//      }
//      .map { it.second }
//  }

  fun isTopManageableGroup(groupId: UUID, environment: DataFetchingEnvironment): Boolean {
    val manageableGroupIds = environment.currentUser().authorities
      ?.filter { it.roles.contains(Role.GROUP) }
      ?.map { it.groupId }
    if (manageableGroupIds.isNullOrEmpty()
      || !manageableGroupIds.contains(groupId)) {
      return false
    }

    val groups = groupRepository.selectByIds(manageableGroupIds).map { group -> group.groupIdPath() to group.groupId }
    return groups
      .filter { group ->
        groups
          .firstOrNull { path ->
            return@firstOrNull (path.second != group.second)
              && path.first.contains(group.first)
          }
          ?.let { false }
          ?: true
      }
      .map { it.second }
      .contains(groupId)
  }

  fun group(groupId: UUID, environment: DataFetchingEnvironment): GroupWithPath? {
    return groupRepository.selectById(groupId)
  }

  fun addGroup(command: AddGroupCommand, environment: DataFetchingEnvironment): Boolean {
    val now = environment.now().toLocalDate()
    groupRepository.insertNewGroup(now, command.groupName, command.parentGroupId, command.groupGenerationId)
    return true
  }

  fun updateGroup(command: UpdateGroupCommand): Boolean {
    groupRepository.updateGroup(command.groupId, command.groupName)
    return true
  }

  fun deleteGroup(groupTransitionId: UUID): Boolean {
    groupRepository.deleteGroup(groupTransitionId)
    return true
  }
}
