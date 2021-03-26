package io.github.novemdecillion.adapter.keycloak

import io.github.novemdecillion.util.OAuth2Utils
import io.github.novemdecillion.utils.keycloak.client.KeycloakAdminCliClient
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.stereotype.Service

data class Account(val adminName: String, val adminPassword: String)

@ConfigurationProperties("aap.sync-users")
data class SyncUsersProperties(
  val oauth2: Map<String, Account>
)


@Service
class SyncUsersService(val clientRegistrationRepository: InMemoryClientRegistrationRepository, val syncUsersProperties: SyncUsersProperties) {
  @Scheduled(cron = "\${app.sync-users.cron}")
  fun sync() {
    clientRegistrationRepository
      .forEach { registration ->
        val account = syncUsersProperties.oauth2[registration.registrationId]?: return@forEach

        val baseUri = OAuth2Utils.issuerUriToBaseUri(registration.providerDetails.issuerUri)
        val realm = OAuth2Utils.issuerUriToRealm(registration.providerDetails.issuerUri)

        val restClient = RestTemplateBuilder().rootUri(baseUri).build()
        val adminCliClient = KeycloakAdminCliClient(restClient)

        val accessToken = adminCliClient.accessToken(account.adminName, account.adminPassword).token

        val users = adminCliClient.userRepresentation(accessToken, realm)





      }


  }
}