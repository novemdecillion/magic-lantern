package io.github.novemdecillion.adapter.api

import graphql.kickstart.tools.GraphQLMutationResolver
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.schema.DataFetchingEnvironment
import io.github.novemdecillion.adapter.db.AccountRepository
import io.github.novemdecillion.adapter.db.UserRepository
import io.github.novemdecillion.adapter.jooq.tables.pojos.AccountGroupAuthorityEntity
import io.github.novemdecillion.adapter.jooq.tables.pojos.RealmEntity
import io.github.novemdecillion.adapter.oauth2.SyncKeycloakUsersService
import io.github.novemdecillion.domain.Authority
import io.github.novemdecillion.domain.Member
import io.github.novemdecillion.domain.Role
import io.github.novemdecillion.domain.User
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserApi(
  val userRepository: UserRepository,
  val accountRepository: AccountRepository,
  val syncUsersService: SyncKeycloakUsersService
) : GraphQLQueryResolver, GraphQLMutationResolver {
  data class AddGroupMemberCommand(
    val groupId: UUID,
    val userId: List<UUID>,
    val role: List<Role>
  )

  data class GroupMemberCommand(
    val groupId: UUID,
    val userId: UUID,
    val role: List<Role>
  )

  fun currentUser(environment: DataFetchingEnvironment): User {
    return environment.currentUser()
  }

  fun users(): List<User> {
    return userRepository.findAllWithAuthority()
  }

  fun userCount(): Int {
    return userRepository.count()
  }

  fun realms(): List<RealmEntity> {
    return accountRepository.selectRealm()
  }

  fun syncRealm(realmId: String?): Boolean {
    realmId
      ?.also {
        syncUsersService.syncRealm(it)
      }
      ?: syncUsersService.sync()
    return true
  }

  fun groupMembers(groupId: UUID, environment: DataFetchingEnvironment): List<User> {
    return userRepository.selectMemberByGroupTransitionId(groupId)
  }

  fun groupAppendableMembers(groupId: UUID, environment: DataFetchingEnvironment): List<User> {
    return userRepository.selectAppendableMemberByGroupTransitionId(groupId)
  }

  fun addGroupMember(command: AddGroupMemberCommand): Boolean {
    command.userId
      .windowed(100, 100, true)
      .forEach{ userIds ->
        val entities = command.role.flatMap { role ->
          userIds.map { AccountGroupAuthorityEntity(it, command.groupId, role) }
        }
        userRepository.insertAuthorities(entities)
      }
    return true
  }
  fun editGroupMember(command: GroupMemberCommand): Boolean {
    TODO()
  }
  fun deleteGroupMember(groupId: UUID, userId: UUID): Boolean {
    TODO()
  }

}