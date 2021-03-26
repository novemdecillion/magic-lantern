package io.github.novemdecillion.utils.keycloak.client

import io.github.novemdecillion.utils.lang.logger
import org.keycloak.OAuth2Constants
import org.keycloak.representations.AccessTokenResponse
import org.keycloak.representations.account.UserRepresentation
import org.springframework.http.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import org.springframework.web.client.postForEntity

open class KeycloakClient(protected val restClient: RestTemplate, private val realm: String, private val client: String) {
  companion object {
    val log = logger()
  }
  
  fun accessToken(username: String, password: String): AccessTokenResponse {
    val headers = HttpHeaders()
    headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

    val body = LinkedMultiValueMap<String, String>()
    body.add(OAuth2Constants.CLIENT_ID, client)
    body.add("username", username)
    body.add("password", password)
    body.add(OAuth2Constants.GRANT_TYPE, OAuth2Constants.PASSWORD)

    val request = HttpEntity(body, headers)
    return restClient.postForEntity<AccessTokenResponse>("/realms/$realm/protocol/openid-connect/token", request).body!!
  }
}

class KeycloakAdminCliClient(restClient: RestTemplate) : KeycloakClient(restClient, "master", "admin-cli") {
  companion object {
    val log = logger()
  }

  fun userCount(token: String, realm: String): Int {
    val headers = HttpHeaders()
    headers.setBearerAuth(token)
    val request = HttpEntity<MultiValueMap<String, String>>(headers)
    return restClient.exchange(
      "/admin/realms/$realm/users/count", HttpMethod.GET, request, Int::class.java).body!!
  }

  fun userRepresentation(token: String, realm: String, pageSize: Int = 100): Collection<UserRepresentation> {
    var userCount = userCount(token, realm)
    log.info("ユーザ件数: $userCount")
    val headers = HttpHeaders()
    headers.setBearerAuth(token)
    val request = HttpEntity<MultiValueMap<String, String>>(headers)
    var offset = 0
    val users = mutableListOf<UserRepresentation>()

    do {
      val response  = restClient.exchange<List<UserRepresentation>>(
        "/admin/realms/$realm/users?first=$offset&max=$pageSize",
        HttpMethod.GET, request)
        .body
        ?: listOf()
      users.addAll(response)
      offset += response.size
      log.info("ユーザ取得件数: $offset / $userCount")

      // 全ユーザ取得前に応答が0件
      if (response.isEmpty() && (offset < userCount)) {
        val retryUserCount = userCount(token, realm)
        log.info("再取得ユーザ件数: $retryUserCount")
        // 最初からやり直し
        users.clear()
        userCount = retryUserCount
        offset = 0
      }
    } while (offset < userCount)
    return users
  }

//  fun mappingsRepresentation(token: String, realm: String, userId: String) : Mono<MappingsRepresentation> {
//    return restClient.get().uri("/admin/realms/$realm/users/$userId/role-mappings")
//      .headers {
//        it.setBearerAuth(token)
//      }
//      .retrieve()
//      .bodyToMono<MappingsRepresentation>()
//      .doOnError{
//        log.error("mappingsRepresentation", it)
//      }
//  }

/*
  fun createUser(token: String, realm: String, user: UserRepresentation): Mono<Boolean> {
    return restClient
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

  fun deleteUser(token: String, realm: String, userId: String): Mono<Boolean> {
    return restClient
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
*/
}