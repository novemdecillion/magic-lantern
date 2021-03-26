package io.github.novemdecillion.utils.keycloak.client

import io.github.novemdecillion.utils.lang.logger
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Test
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.ClientResponse

import reactor.core.publisher.Mono

import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import reactor.core.publisher.Flux
import java.util.function.Consumer


class KeycloakTest {
  companion object {
    val log = logger()
    val webClient = WebClient.builder().baseUrl("http://localhost:50010/auth").filter(logResponse()).build()

    val client = KeycloakAdminCliReactiveClient(webClient)
    val realm = "example"

    fun logResponse(): ExchangeFilterFunction {
      return ExchangeFilterFunction.ofResponseProcessor { response: ClientResponse ->
        logStatus(response)
        logHeaders(response)
        logBody(response)
      }
    }

    private fun logStatus(response: ClientResponse) {
      val status: HttpStatus = response.statusCode()
      log.debug("Returned staus code {} ({})", status.value(), status.getReasonPhrase())
    }

    private fun logBody(response: ClientResponse): Mono<ClientResponse> {
      return if (response.statusCode() != null && (response.statusCode().is4xxClientError || response.statusCode().is5xxServerError)) {
        response.bodyToMono(String::class.java)
          .flatMap { body: String? ->
            log.debug("Body is {}", body)
            Mono.just(response)
          }
      } else {
        Mono.just(response)
      }
    }

    private fun logHeaders(response: ClientResponse) {
      response.headers().asHttpHeaders().forEach { name: String?, values: List<String?> ->
        values.forEach(
          Consumer { value: String? -> log.debug("$name : $value") })
      }
    }
  }

  @Test
  fun users() {
    client.accessToken("admin", "password123")
      .flatMapMany { accessToken ->
        client.userRepresentation(accessToken.token, realm)
//          .flatMap { user ->
//            user.isEnabled = true
//            client.updateUser(accessToken.token, realm, user)
//              .map { user }
//          }
      }
      .map {
        println(it.username)
      }
      .blockLast()
  }

  fun addUsers() {
    client.accessToken("admin", "password123")
      .map { it.token }
      .flatMap { token ->
        // ユーザ全削除
        client.userRepresentation(token, realm)
          .flatMap {
            client.deleteUser(token, realm, it.id)
          }
          .collectList()
          .map { token }
      }
      .flatMapMany { token ->
        Flux.fromIterable(0..3000)
          .flatMap {
            val username = RandomStringUtils.randomAlphanumeric(8, 12)
            val user = UserRepresentation()
              .also {
                it.username = username
                it.email = "$username@example.com"
                it.firstName = username.substring(0..5)
                it.lastName = username.substring(6)
                it.isEnabled = true
              }
            client.createUser(token, realm, user)
          }
      }
      .blockLast()
  }


}