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

  data class EditGroupCommand(
    val groupId: UUID,
    val groupName: String
  )

  fun authoritativeGroups(role: Role, environment: DataFetchingEnvironment): List<GroupWithPath> {
    val groupIds = environment.currentUser().authorities
      ?.filter { it.roles.contains(role) }
      ?.map { it.groupId }
      ?: return listOf()
    return groupRepository.selectByIds(groupIds)
  }

  fun effectiveGroups(role: Role, environment: DataFetchingEnvironment): List<GroupWithPath> {
    val manageableGroupIds = environment.currentUser().authorities
      ?.filter { it.roles.contains(role) }
      ?.map { it.groupId }
      ?: return listOf()
    return groupRepository.selectChildrenByIds(manageableGroupIds)
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

    val groups = groupRepository.selectByIds(manageableGroupIds).map { group -> group.groupIdPath() to group.group.groupId }
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
}