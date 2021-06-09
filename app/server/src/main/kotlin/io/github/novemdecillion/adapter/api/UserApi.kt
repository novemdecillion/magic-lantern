package io.github.novemdecillion.adapter.api

import graphql.kickstart.tools.GraphQLMutationResolver
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.schema.DataFetchingEnvironment
import io.github.novemdecillion.adapter.db.AccountRepository
import io.github.novemdecillion.adapter.db.GroupAuthorityRepository
import io.github.novemdecillion.adapter.db.RealmRepository
import io.github.novemdecillion.adapter.id.IdGeneratorService
import io.github.novemdecillion.adapter.jooq.tables.pojos.AccountEntity
import io.github.novemdecillion.adapter.jooq.tables.pojos.RealmEntity
import io.github.novemdecillion.adapter.oauth2.SyncKeycloakUsersService
import io.github.novemdecillion.domain.*
import io.github.novemdecillion.utils.lang.logger
import org.dataloader.MappedBatchLoader
import org.springframework.dao.DuplicateKeyException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

@Component
class UserApi(
  private val userRepository: AccountRepository,
  private val accountRepository: AccountRepository,
  private val realmRepository: RealmRepository,
  private val authorityRepository: GroupAuthorityRepository,
  private val syncUsersService: SyncKeycloakUsersService,
  private val idGeneratorService: IdGeneratorService,
  private val passwordEncoder: PasswordEncoder
) : GraphQLQueryResolver, GraphQLMutationResolver {
  companion object {
    val log = logger()
  }

  @Component
  class UserLoader(private val userRepository: AccountRepository): MappedBatchLoader<UUID, User>, LoaderFunctionMaker<UUID, User> {
    override fun load(keys: Set<UUID>): CompletionStage<Map<UUID, User>> {
      return CompletableFuture.completedFuture(userRepository.selectByIds(keys).associateBy { it.userId })
    }
  }

  data class AddUserCommand (
    val userName: String,
    val accountName: String,
    val password: String,
    val email: String?,
    val enabled: Boolean?,
    val isAdmin: Boolean?,
    val isGroup: Boolean?,
    val isSlide: Boolean?
  )

  data class UpdateUserCommand (
    val userId: UUID,
    val accountName: String?,
    val userName: String?,
    val password: String?,
    val email: String?,
    val enabled: Boolean?,
    val isAdmin: Boolean?,
    val isGroup: Boolean?,
    val isSlide: Boolean?
  )

  data class ChangePasswordCommand (
    val oldPassword: String,
    val newPassword: String
  )

  @GraphQLApi
  fun currentUser(environment: DataFetchingEnvironment): User {
    return environment.currentUser()
  }

  @GraphQLApi
  fun users(): List<User> {
    return userRepository.selectAllWithAuthority()
  }

  @GraphQLApi
  fun userCount(): Int {
    return userRepository.selectCount()
  }

  @GraphQLApi
  fun realms(): List<RealmEntity> {
    return realmRepository.selectAll()
  }

  @GraphQLApi
  fun syncRealm(realmId: String?): Boolean {
    realmId
      ?.also {
        syncUsersService.syncRealm(it)
      }
      ?: syncUsersService.sync()
    return true
  }

  @GraphQLApi
  fun addUser(command: AddUserCommand): Boolean {
    try {
      val account = AccountEntity(
        accountId = idGeneratorService.generate(),
        accountName = command.accountName,
        userName = command.userName,
        realmId = SYSTEM_REALM_ID,
        password = passwordEncoder.encode(command.password),
        email = command.email,
        enabled = (command.enabled ?: false)
      )
      accountRepository.insert(account)

      val roles = mutableListOf<Role>()
      if (command.isAdmin == true) {
        roles.add(Role.ADMIN)
      }
      if (command.isGroup == true) {
        roles.add(Role.GROUP)
      }
      if (command.isSlide == true) {
        roles.add(Role.SLIDE)
      }
      authorityRepository.insertAuthority(account.accountId!!, Authority.forRootGroup(roles))
    } catch (ex: Exception) {
      log.error("ユーザの追加に失敗しました。", ex)
      throw when(ex) {
          is DuplicateKeyException -> ApiException("指定のアカウント名は既に存在しています。")
          else -> ApiException("ユーザの追加に失敗しました。")
      }
    }
    return true
  }

  enum class UpdateUserResult {
    Success,
    UnknownError
  }
  @GraphQLApi
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

//      val authority = authorityRepository.selectAuthorityByUserIdAndGroupId(command.userId, ROOT_GROUP_ID)
//        ?: Authority.forRootGroup()
//      val newRoles = authority.roles?.toMutableSet() ?: mutableListOf()
//      command.isAdmin
//        ?.also { requestFlag ->
//          val isAdmin: Boolean = newRoles.contains(Role.ADMIN)
//          when {
//            !isAdmin && requestFlag -> newRoles.add(Role.ADMIN)
//            isAdmin && !requestFlag -> newRoles.remove(Role.ADMIN)
//          }
//        }
//      command.isGroup
//        ?.also { requestFlag ->
//          val isGroup: Boolean = newRoles.contains(Role.GROUP)
//          when {
//            !isGroup && requestFlag -> newRoles.add(Role.GROUP)
//            isGroup && !requestFlag -> newRoles.remove(Role.GROUP)
//          }
//        }
//      command.isSlide
//        ?.also { requestFlag ->
//          val isSlide: Boolean = newRoles.contains(Role.SLIDE)
//          when {
//            !isSlide && requestFlag -> newRoles.add(Role.SLIDE)
//            isSlide && !requestFlag -> newRoles.remove(Role.SLIDE)
//          }
//        }
//      if (newRoles != authority.roles?.toSet()) {
//      }

      if ((command.isAdmin != null)
          || (command.isGroup != null)
          ||  (command.isSlide != null)) {

        val removeRoles = mutableListOf<Role>()
        val addRoles = mutableListOf<Role>()
        if (command.isAdmin == true) {
          removeRoles.add(Role.ADMIN)
          addRoles.add(Role.ADMIN)
        } else if (command.isAdmin == false) {
          removeRoles.add(Role.ADMIN)
        }

        if (command.isGroup == true) {
          removeRoles.add(Role.GROUP)
          addRoles.add(Role.GROUP)
        } else if (command.isGroup == false) {
          removeRoles.add(Role.GROUP)
        }

        if (command.isSlide == true) {
          removeRoles.add(Role.SLIDE)
          addRoles.add(Role.SLIDE)
        } else if (command.isSlide == false) {
          removeRoles.add(Role.SLIDE)
        }
        authorityRepository.updateAuthorities(listOf(command.userId), ROOT_GROUP_ID, ROOT_GROUP_GENERATION_ID, removeRoles, addRoles)
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
  @GraphQLApi
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

  @GraphQLApi
  fun deleteUser(userId: UUID): Boolean {
    return 1 == accountRepository.delete(userId)
  }
}