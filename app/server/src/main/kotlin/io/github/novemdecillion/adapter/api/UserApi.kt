package io.github.novemdecillion.adapter.api

import graphql.kickstart.tools.GraphQLMutationResolver
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.schema.DataFetchingEnvironment
import io.github.novemdecillion.adapter.db.AccountRepository
import io.github.novemdecillion.adapter.db.UserRepository
import io.github.novemdecillion.adapter.id.IdGeneratorService
import io.github.novemdecillion.adapter.jooq.tables.pojos.AccountEntity
import io.github.novemdecillion.adapter.jooq.tables.pojos.AccountGroupAuthorityEntity
import io.github.novemdecillion.adapter.jooq.tables.pojos.RealmEntity
import io.github.novemdecillion.adapter.oauth2.SyncKeycloakUsersService
import io.github.novemdecillion.domain.*
import io.github.novemdecillion.utils.lang.logger
import org.springframework.dao.DuplicateKeyException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserApi(
  private val userRepository: UserRepository,
  private val accountRepository: AccountRepository,
  private val syncUsersService: SyncKeycloakUsersService,
  private val idGeneratorService: IdGeneratorService,
  private val passwordEncoder: PasswordEncoder
) : GraphQLQueryResolver, GraphQLMutationResolver {
  companion object {
    val log = logger()
  }

  data class AddUserCommand (
    val userName: String,
    val accountName: String,
    val password: String,
    val email: String?,
    val enabled: Boolean,
    val isAdmin: Boolean,
    val isGroupManager: Boolean
  )

  data class UpdateUserCommand (
    val userId: UUID,
    val accountName: String?,
    val userName: String?,
    val password: String?,
    val email: String?,
    val enabled: Boolean?,
    val isAdmin: Boolean?,
    val isGroupManager: Boolean?
  )

  data class ChangePasswordCommand (
    val oldPassword: String,
    val newPassword: String
  )

  data class GroupMemberCommand(
    val groupId: UUID,
    val userIds: List<UUID>,
    val role: List<Role>
  )

  data class DeleteGroupMemberCommand(
    val groupId: UUID,
    val userIds: List<UUID>
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

  enum class AddUserResult {
    Success,
    DuplicateAccountName,
    UnknownError
  }
  fun addUser(command: AddUserCommand): AddUserResult {
    try {
      val account = AccountEntity(
        accountId = idGeneratorService.generate(),
        accountName = command.accountName,
        userName = command.userName,
        realmId = SYSTEM_REALM_ID,
        password = passwordEncoder.encode(command.password),
        email = command.email,
        enabled = command.enabled
      )
      accountRepository.insert(account)

      val roles = mutableListOf<Role>()
      if (command.isAdmin) {
        roles.add(Role.ADMIN)
      }
      if (command.isGroupManager) {
        roles.add(Role.GROUP)
      }
      if (roles.isEmpty()) {
        roles.add(Role.NONE)
      }
      userRepository.insertAuthority(account.accountId!!, Authority(ENTIRE_GROUP_ID, roles))
    } catch (ex: Exception) {
      log.error("ユーザの追加に失敗しました。", ex)
      return when(ex) {
          is DuplicateKeyException -> AddUserResult.DuplicateAccountName
          else -> AddUserResult.UnknownError
      }
    }
    return AddUserResult.Success
  }

  enum class UpdateUserResult {
    Success,
    UnknownError
  }
  fun updateUser(command: UpdateUserCommand): UpdateUserResult {
    try {
      val account = AccountEntity(
        accountId = command.userId,
        accountName = command.accountName,
        userName = command.userName,
        email = command.email,
        enabled = command.enabled
      )
      command.password?.also { account.password  = passwordEncoder.encode(command.password) }
      accountRepository.update(account)

      val authority = userRepository.selectAuthorityByUserIdAndGroupId(command.userId, ENTIRE_GROUP_ID)
        ?: Authority(ENTIRE_GROUP_ID, listOf())
      val newRoles = authority.roles.toMutableSet()
      newRoles.remove(Role.NONE)
      command.isAdmin
        ?.also { requestFlag ->
          val isAdmin: Boolean = authority.roles.contains(Role.ADMIN)
          when {
            !isAdmin && requestFlag -> newRoles.add(Role.ADMIN)
            isAdmin && !requestFlag -> newRoles.remove(Role.ADMIN)
          }
        }
      command.isGroupManager
        ?.also { requestFlag ->
          val isGroupManager: Boolean = authority.roles.contains(Role.GROUP)
          when {
            !isGroupManager && requestFlag -> newRoles.add(Role.GROUP)
            isGroupManager && !requestFlag -> newRoles.remove(Role.GROUP)
          }
        }
      if (newRoles.isEmpty()) {
        newRoles.add(Role.NONE)
      }
      if (newRoles != authority.roles.toSet()) {
        userRepository.updateAuthority(command.userId, authority.copy(roles = newRoles))
      }
    } catch (ex: Exception) {
      log.error("ユーザの追加に失敗しました。", ex)
      return UpdateUserResult.UnknownError
    }
    return UpdateUserResult.Success
  }

  enum class ChangePasswordResult {
    Success,
    UserNotFound,
    PasswordNotMatch,
    UnknownError
  }
  fun changePassword(command: ChangePasswordCommand, environment: DataFetchingEnvironment): ChangePasswordResult {
    try {
      val user = environment.currentUser()

      val account = accountRepository.selectByAccountNameAndRealmId(user.accountName, user.realmId)
        ?: return ChangePasswordResult.UserNotFound

      if (!passwordEncoder.matches(command.oldPassword, account.password)) {
        return ChangePasswordResult.PasswordNotMatch
      }

      val encodedNewPassword = passwordEncoder.encode(command.newPassword)
      accountRepository.changePassword(user.userId, encodedNewPassword)
    } catch (ex: Exception) {
      return ChangePasswordResult.UnknownError
    }
    return ChangePasswordResult.Success
  }

  fun deleteUser(userId: UUID): Boolean {
    return 1 == accountRepository.delete(userId)
  }

  fun groupMembers(groupId: UUID, environment: DataFetchingEnvironment): List<User> {
    return userRepository.selectMemberByGroupTransitionId(groupId)
  }

  fun groupAppendableMembers(groupId: UUID, environment: DataFetchingEnvironment): List<User> {
    return userRepository.selectAppendableMemberByGroupTransitionId(groupId)
  }

  fun addGroupMember(command: GroupMemberCommand): Boolean {
    command.userIds
      .windowed(100, 100, true)
      .forEach{ userIds ->
        val entities = command.role.flatMap { role ->
          userIds.map { AccountGroupAuthorityEntity(it, command.groupId, role) }
        }
        userRepository.insertAuthorities(entities)
      }
    return true
  }

  fun updateGroupMember(command: GroupMemberCommand): Boolean {
    command.userIds
      .windowed(100, 100, true)
      .forEach{ userIds ->
        val entities = command.role.flatMap { role ->
          userIds.map { AccountGroupAuthorityEntity(it, command.groupId, role) }
        }
        userRepository.updateAuthorities(entities)
      }
    return true
  }

  fun deleteGroupMember(command: DeleteGroupMemberCommand): Boolean {
    command.userIds
      .windowed(100, 100, true)
      .forEach{ userIds ->
        userRepository.deleteAuthorities(command.groupId, userIds)
      }
    return true
  }

}