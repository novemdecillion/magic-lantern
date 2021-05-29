package io.github.novemdecillion.adapter.oauth2

import io.github.novemdecillion.adapter.db.AccountRepository
import io.github.novemdecillion.adapter.jooq.tables.pojos.RealmEntity
import io.github.novemdecillion.domain.SYSTEM_REALM_ID
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate

@Service
class SyncRealmService(
  val accountRepository: AccountRepository,
  val clientRegistrationRepository: InMemoryClientRegistrationRepository
) {
  @EventListener
  @Order(Ordered.LOWEST_PRECEDENCE - 10)
  fun startup(event: ContextRefreshedEvent) {
    event.applicationContext.getBean(TransactionTemplate::class.java)
      .execute {
        sync()
      }
  }

  fun sync() {
    val realms = accountRepository.selectRealm()
      .filter { it.realmId != SYSTEM_REALM_ID }
      .associateBy { it.realmId }
      .toMutableMap()
    clientRegistrationRepository
      .forEach { registration ->
        realms.remove(registration.registrationId)
          ?.also { realm ->
            if (realm.realmName != registration.clientName) {
              realm.realmName = registration.clientName
              accountRepository.update(realm)
            }
          }
          ?: run {
            accountRepository.insert(RealmEntity(registration.registrationId, registration.clientName, true, null))
          }
      }
    realms
      .forEach { (_, realm) ->
        realm.enabled = false
        accountRepository.update(realm)
    }
  }
}