package io.github.novemdecillion.utils.keycloak.client

import io.github.novemdecillion.utils.lang.logger
import org.keycloak.OAuth2Constants
import org.keycloak.representations.AccessTokenResponse
import org.keycloak.representations.idm.UserRepresentation
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.ErrorRepresentation
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks

open class KeycloakReactiveClient(protected val webClient: WebClient, private val realm: String, private val client: String) {
  companion object {
    val log = logger()
  }

  fun accessToken(username: String, password: String): Mono<AccessTokenResponse> {
    return webClient.post().uri("/realms/$realm/protocol/openid-connect/token")
      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .body(
        BodyInserters
          .fromFormData(OAuth2Constants.CLIENT_ID, client)
          .with("username", username)
          .with("password", password)
          .with(OAuth2Constants.GRANT_TYPE, OAuth2Constants.PASSWORD)
      )
      .retrieve()
      .onStatus(::isServerError, ::serverError)
      .bodyToMono<AccessTokenResponse>()
//      .doOnNext {
//        log.info("expires_in: ${it.expiresIn}, refresh_expires_in: ${it.refreshExpiresIn}")
//      }
  }

  protected fun isServerError(status: HttpStatus) : Boolean {
    return status.is5xxServerError
  }

  protected fun serverError(response: ClientResponse) : Mono<Throwable> {
    return response.bodyToMono(ErrorRepresentation::class.java)
      .map {
        WebClientResponseException(
          it.errorMessage,
          response.statusCode().value(),
          response.statusCode().reasonPhrase,
          null,
          null,
          null
        )
      }
  }
}

class KeycloakAdminCliReactiveClient(webClient: WebClient) : KeycloakReactiveClient(webClient, "master", "admin-cli") {
  companion object {
    val log = logger()
  }

  fun userCount(token: String, realm: String): Mono<Int> {
    return webClient.get()
      .uri("/admin/realms/$realm/users/count")
      .headers {
        it.setBearerAuth(token)
      }
      .retrieve()
      .onStatus(::isServerError, ::serverError)
      .bodyToMono()
  }

  fun userRepresentation(token: String, realm: String, pageSize: Int = 100): Flux<UserRepresentation> {
    return userCount(token, realm)
      .flatMapMany { userCount ->
        log.debug("ユーザ件数: $userCount")

        val sink = Sinks.many().unicast().onBackpressureBuffer<Int>()
        sink.tryEmitNext(0)
        sink
          .asFlux()
          .flatMap { offset ->
            webClient.get()
              .uri {
                it.path("/admin/realms/$realm/users")
                  .queryParam("first", offset)
                  .queryParam("max", pageSize)
                  .build()
              }
              .headers {
                it.setBearerAuth(token)
              }
              .retrieve()
              .onStatus(::isServerError, ::serverError)
              .bodyToMono<List<UserRepresentation>>()
              .flatMapMany { users ->
                val currentUserCount = offset + users.size
                log.info("ユーザ取得件数: $currentUserCount / $userCount")

                if (users.size < pageSize) {
                  sink.tryEmitComplete()
                } else {
                  sink.tryEmitNext(currentUserCount)
                }
                Flux.fromIterable(users)
              }
          }
      }
  }

//  fun mappingsRepresentation(token: String, realm: String, userId: String) : Mono<MappingsRepresentation> {
//    return webClient.get().uri("/admin/realms/$realm/users/$userId/role-mappings")
//      .headers {
//        it.setBearerAuth(token)
//      }
//      .retrieve()
//      .bodyToMono<MappingsRepresentation>()
//      .doOnError{
//        log.error("mappingsRepresentation", it)
//      }
//  }

  fun createUser(token: String, realm: String, user: UserRepresentation): Mono<Boolean> {
    return webClient
      .post()
      .uri("/admin/realms/$realm/users")
      .contentType(MediaType.APPLICATION_JSON)
      .headers {
        it.setBearerAuth(token)
      }
      .bodyValue(user)
      .retrieve()
      .onStatus(::isServerError, ::serverError)
      .bodyToMono<Boolean>()
      .switchIfEmpty(Mono.just(true))
  }

  fun createUsers(token: String, realm: String, users: Collection<UserRepresentation>): Flux<Boolean> {
    return Flux
      .fromIterable(users)
      .flatMap { user ->
        createUser(token, realm, user)
      }
  }

  fun resetPassword(token: String, realm: String, userId: String, password: String, temporary: Boolean = false): Mono<Boolean> {
    val credential = CredentialRepresentation()
      .also {
        it.type = "password"
        it.value = password
        it.isTemporary = temporary
      }
    return webClient.put().uri("/admin/realms/$realm/users/$userId/reset-password")
      .headers {
        it.setBearerAuth(token)
      }
      .bodyValue(credential)
      .retrieve()
      .onStatus(::isServerError, ::serverError)
      .bodyToMono<Boolean>()
      .switchIfEmpty(Mono.just(true))
  }

  fun updateUser(token: String, realm: String, user: UserRepresentation): Mono<Boolean> {
    return webClient
      .put()
      .uri("/admin/realms/$realm/users/${user.id}")
      .contentType(MediaType.APPLICATION_JSON)
      .headers {
        it.setBearerAuth(token)
      }
      .bodyValue(user)
      .retrieve()
      .onStatus(::isServerError, ::serverError)
      .bodyToMono<Boolean>()
      .switchIfEmpty(Mono.just(true))
  }

  fun deleteUser(token: String, realm: String, userId: String): Mono<Boolean> {
    return webClient
      .delete()
      .uri("/admin/realms/$realm/users/$userId")
      .headers {
        it.setBearerAuth(token)
      }
      .retrieve()
      .onStatus(::isServerError, ::serverError)
      .bodyToMono<Boolean>()
      .switchIfEmpty(Mono.just(true))

  }
}