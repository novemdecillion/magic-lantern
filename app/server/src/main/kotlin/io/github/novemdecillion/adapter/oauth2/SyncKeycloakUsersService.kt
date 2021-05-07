package io.github.novemdecillion.adapter.oauth2

import io.github.novemdecillion.adapter.db.AccountRepository
import io.github.novemdecillion.adapter.db.UserRepository
import io.github.novemdecillion.adapter.id.IdGeneratorService
import io.github.novemdecillion.adapter.jooq.tables.pojos.AccountEntity
import io.github.novemdecillion.adapter.jooq.tables.pojos.RealmEntity
import io.github.novemdecillion.adapter.scheduling.SPRING_SCHEDULING_ENABLED_KEY
import io.github.novemdecillion.domain.Authority
import io.github.novemdecillion.domain.ENTIRE_GROUP_ID
import io.github.novemdecillion.domain.Role
import io.github.novemdecillion.util.OAuth2Utils
import io.github.novemdecillion.utils.keycloak.client.KeycloakAdminCliClient
import io.github.novemdecillion.utils.lang.logger
import org.keycloak.representations.account.UserRepresentation
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.*

data class AccountProperties(val adminName: String, val adminPassword: String)

@ConfigurationProperties("app.sync-users")
@ConstructorBinding
data class SyncUsersProperties(
  val oauth2: Map<String, AccountProperties>
)

@Service
class SyncKeycloakUsersService(
  val clientRegistrationRepository: InMemoryClientRegistrationRepository,
  val syncUsersProperties: SyncUsersProperties,
  val idGeneratorService: IdGeneratorService,
  val accountRepository: AccountRepository,
  val userRepository: UserRepository
) {
  companion object {
    val log = logger()
  }

  @Value("\${${SPRING_SCHEDULING_ENABLED_KEY}}")
  var scheduled: Boolean = false

  @EventListener
  fun startup(event: ContextRefreshedEvent) {
    if (scheduled) {
      event.applicationContext.getBean(TransactionTemplate::class.java)
        .execute {
          sync()
        }
    }
  }

  @Scheduled(cron = "\${app.sync-users.cron}")
  @Transactional(rollbackFor = [Exception::class])
  fun sync() {
    // TODO 認証サーバの設定が削除されたユーザを無効化する
    clientRegistrationRepository
      .forEach { registration ->
        syncRealm(registration)
      }
  }

  fun syncRealm(realmId: String) {
    val registration = clientRegistrationRepository.findByRegistrationId(realmId) ?: return
    syncRealm(registration)
  }

  @Synchronized
  fun syncRealm(registration: ClientRegistration) {
    val account = syncUsersProperties.oauth2[registration.registrationId]
      ?: run {
        log.warn(
          "認証サーバ(spring.security.oauth2.client.registration.${registration.registrationId})のadminアカウントの設定(app.sync-users.oauth2.${registration.registrationId})がないため、ユーザ情報の同期ができません。"
        )
        return
      }

    log.info("認証サーバ(${registration.clientName})とのユーザ情報の同期を開始します。")
    val baseUri = OAuth2Utils.issuerUriToBaseUri(registration.providerDetails.issuerUri)
    val realmInKeycloak = OAuth2Utils.issuerUriToRealm(registration.providerDetails.issuerUri)

    val restClient = RestTemplateBuilder().rootUri(baseUri).build()
    val adminCliClient = KeycloakAdminCliClient(restClient)

    val accessToken = adminCliClient.accessToken(account.adminName, account.adminPassword).token

    val users = adminCliClient.userRepresentation(accessToken, realmInKeycloak)

    val realm = registration.registrationId

    var newComerCount = 0
    var updateCount = 0
    val storedAccountNames = accountRepository.selectAccountNames(realm).toMutableSet()

    users.windowed(100, 1, true)
      .forEach { userRepresentations ->

        val accountNames = userRepresentations.map { it.username }
        val storedAccounts =
          accountRepository.selectByAccountNameAndRealm(accountNames, realm).associateBy { it.accountName }

        userRepresentations
          .forEach { userRepresentation ->
            storedAccountNames.remove(userRepresentation.username)
            storedAccounts[userRepresentation.username]
              ?.also { storedAccount ->
                // ユーザ更新
                if (syncAccount(storedAccount, userRepresentation)) {
                  updateCount++
                }
              }
              ?: run {
                // ユーザ追加
                syncNewComer(realm, userRepresentation)
                newComerCount++
              }
          }
      }
    storedAccountNames.windowed(100, 1, true)
      .forEach {
        // 認証サーバ側で削除された?
        accountRepository.updateEnableByAccountNameAndRealm(it, realm, false)
      }
    val disabledCount = storedAccountNames.size
    accountRepository.update(RealmEntity(realmId = registration.registrationId, syncAt = OffsetDateTime.now()))
    log.info("Keycloakサーバ(${registration.clientName})とのユーザ情報の同期を終了します。新規${newComerCount}人, 更新${updateCount}人, 無効化${disabledCount}人")
  }


  fun syncAccount(account: AccountEntity, userRepresentation: UserRepresentation): Boolean {
    val userName = userName(userRepresentation, account.locale)
    if ((account.enabled != true)
      || (account.userName != userName)
      || (account.email != userRepresentation.email)
    ) {
      accountRepository.update(
        account.copy(enabled = true, userName = userName, email = userRepresentation.email)
      )
      return true
    }
    return false
  }

  fun syncNewComer(realm: String, userRepresentation: UserRepresentation) {
    val userName = userName(userRepresentation, null)
    val account = AccountEntity(
      idGeneratorService.generate(), userRepresentation.username, null,
      userName, userRepresentation.email, null, realm, true
    )
    accountRepository.insert(account)
    userRepository.insertAuthority(account.accountId!!, Authority(ENTIRE_GROUP_ID, listOf(Role.NONE)))
  }

  fun userName(userRepresentation: UserRepresentation, locale: String?): String = when {
    !userRepresentation.firstName.isNullOrEmpty() && !userRepresentation.lastName.isNullOrEmpty() -> {
      val eastern = when (locale ?: Locale.JAPAN.country) {
        Locale.JAPAN.country -> true
        else -> false
      }

      if (eastern) {
        "${userRepresentation.lastName} ${userRepresentation.firstName}"
      } else {
        "${userRepresentation.firstName} ${userRepresentation.lastName}"
      }
    }
    !userRepresentation.firstName.isNullOrEmpty() -> userRepresentation.firstName
    !userRepresentation.lastName.isNullOrEmpty() -> userRepresentation.lastName
    else -> userRepresentation.username
  }
}