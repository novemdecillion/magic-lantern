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

  enum class UserApiResult {
    UserAlreadyExist,
    UserNotFound,
    PasswordNotMatch,
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
      val apiException = ApiException("ユーザの追加に失敗しました。")
      if (ex is DuplicateKeyException) {
        apiException.message = "指定のアカウント名は既に存在しています。"
        apiException.setCode(UserApiResult.UserAlreadyExist.name)
      }
      log.error(apiException.message, ex)
      throw apiException
    }
    return true
  }

  @GraphQLApi
  fun updateUser(command: UpdateUserCommand): Boolean {
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

      if ((command.isAdmin != null)
          || (command.isGroup != null)
          ||  (command.isSlide != null)) {

        val removeRoles = mutableListOf<Role>()
        val addRoles = mutableListOf<Role>()

        updateUserRoleFlags(Role.ADMIN, command.isAdmin, removeRoles, addRoles)
        updateUserRoleFlags(Role.GROUP, command.isGroup, removeRoles, addRoles)
        updateUserRoleFlags(Role.SLIDE, command.isSlide, removeRoles, addRoles)

        authorityRepository.updateAuthorities(listOf(command.userId), ROOT_GROUP_ID, ROOT_GROUP_GENERATION_ID, removeRoles, addRoles)
      }
    } catch (ex: Exception) {
      val message = "ユーザの追加に失敗しました。"
      log.error(message, ex)
      throw ApiException(message)
    }
    return true
  }

  private fun updateUserRoleFlags(role: Role, flag: Boolean?, removeRoles: MutableList<Role>, addRoles: MutableList<Role>) {
    if (flag == true) {
      removeRoles.add(role)
      addRoles.add(role)
    } else if (flag == false) {
      removeRoles.add(role)
    }
  }

  @GraphQLApi
  fun changePassword(command: ChangePasswordCommand, environment: DataFetchingEnvironment): Boolean {
    try {
      val user = environment.currentUser()

      val account = accountRepository.selectByAccountNameAndRealmId(user.accountName, user.realmId)
        // 本来はありえない。
        ?: throw ApiException("ユーザが見つかりません。", UserApiResult.UserNotFound.name)

      if (!passwordEncoder.matches(command.oldPassword, account.password)) {
        throw ApiException("パスワードが誤っています。", UserApiResult.PasswordNotMatch.name)
      }

      val encodedNewPassword = passwordEncoder.encode(command.newPassword)
      accountRepository.changePassword(user.userId, encodedNewPassword)
    } catch (ex: Exception) {
      throw ApiException("パスワードの変更に失敗しました。")
    }
    return true
  }

  @GraphQLApi
  fun deleteUser(userId: UUID): Boolean {
    return 1 == accountRepository.delete(userId)
  }
}