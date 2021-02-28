package io.github.novemdecillion

import org.assertj.core.api.Assertions
import org.jsoup.Jsoup
import org.jsoup.nodes.FormElement
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.HttpHeaders
import org.springframework.test.web.reactive.server.FluxExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult
import org.springframework.util.MultiValueMapAdapter

//import org.testcontainers.containers.Neo4jContainer
//import org.testcontainers.junit.jupiter.Container
//import org.testcontainers.junit.jupiter.Testcontainers
//import java.time.Duration
//import org.springframework.test.context.DynamicPropertyRegistry
//
//import org.springframework.test.context.DynamicPropertySource
import java.net.URI


@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "36000")
class LearningApplicationTests {
//  @Autowired
//  lateinit var httpWebHandlerAdapter: HttpWebHandlerAdapter

  @Autowired
  lateinit var webClient: WebTestClient

  @Test
  fun contextLoads() {
    // Topページの要求
    var response: FluxExchangeResult<String> =
      webClient.get().uri("http://localhost:8080").exchange().expectStatus().isFound.expectHeader()
        .location("/oauth2/authorization/keycloak")
        .returnResult()
    Assertions.assertThat(response.responseCookies).isEmpty()
    var redirectUrl = "http://localhost:8080${response.responseHeaders.location}"


    // OAuth2認証エントリへリダイレクト
    println("OAuth2認証エントリへのリダイレクト: $redirectUrl")
    response =
      webClient.get().uri(redirectUrl).exchange().expectStatus().isFound
        .returnResult()
    Assertions.assertThat(response.responseHeaders.location.toString())
      .containsPattern("""http://localhost:50010/auth/realms/example/protocol/openid-connect/auth\?response_type=code&client_id=e-learning&state=.+&redirect_uri=http://localhost:8080/login/oauth2/code/keycloak""")
    Assertions.assertThat(response.responseCookies).containsKey("SESSION")
    val cookies = response.responseCookies
      .map { (t, u) ->
        t to u.map { it.value }
      }
      .toMap()
      .let {
        MultiValueMapAdapter(it)
      }
    redirectUrl = response.responseHeaders.location.toString()

    // Keycloakログインページへリダイレクト
    println("Keycloakログインページへのリダイレクト: $redirectUrl")
    val cookieHolder = mutableMapOf<String, String>()
    val connect = Jsoup.connect(redirectUrl)
    val loginPage = connect
      .also { it.cookies(cookieHolder) }
      .execute()
      .also {
        Assertions.assertThat(it.statusCode()).isEqualTo(200)
        cookieHolder.putAll(it.cookies())
      }
      .parse()

    // Keycloakでログイン
    redirectUrl = loginPage.selectFirst("#kc-form-login")
      .let { it as FormElement }
      .submit()
      .also {
        it.data("username").value("user0001")
        it.data("password").value("password123")
      }
      .cookies(cookieHolder)
      .followRedirects(false)
      .execute()
      .also {
        Assertions.assertThat(it.statusCode()).isEqualTo(302)
        cookieHolder.putAll(it.cookies())
      }
      .header(HttpHeaders.LOCATION)


    // OAuth2認証コードへリダイレクト
    println("OAuth2認証コードへのリダイレクト: $redirectUrl")
    // TODO これしないとURLが再URLエンコードされてしまい?サーバ側でstateの値がマッチしなくなる
    val uri = URI.create(redirectUrl)
    webClient.get().uri(uri).cookies { it.addAll(cookies) }.exchange()
      .expectStatus().isFound.expectHeader().location("/")
  }

/*
  @Test
  fun contextLoads() {
    // Topページの要求
    var request = MockServerHttpRequest.get("http://localhost:8080").build()
    var response = MockServerHttpResponse()
    httpWebHandlerAdapter.handle(request, response).block()

    Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.FOUND)
    var redirectUrl = response.headers.location.toString()
    Assertions.assertThat(redirectUrl)
      .isEqualTo("/oauth2/authorization/keycloak")
    Assertions.assertThat(response.cookies).isEmpty()

    // OAuth2認証エントリへリダイレクト
    redirectUrl = "http://localhost:8080$redirectUrl"
    println("OAuth2認証エントリへのリダイレクト: $redirectUrl")
    request =
      MockServerHttpRequest.get(redirectUrl).build()
    response = MockServerHttpResponse()
    httpWebHandlerAdapter.handle(request, response).block()

    Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.FOUND)
    Assertions.assertThat(response.headers.location.toString())
      .containsPattern("""http://localhost:50010/auth/realms/example/protocol/openid-connect/auth\?response_type=code&client_id=e-learning&state=.+&redirect_uri=http://localhost:8080/login/oauth2/code/keycloak""")
    Assertions.assertThat(response.cookies).containsKey("SESSION")
    val cookies = response.cookies
      .map { (t, u) ->
        t to u.map { HttpCookie(it.name, it.value) }
      }
      .toMap()
      .let {
        MultiValueMapAdapter(it)
      }

    // Keycloakログインページへリダイレクト
    redirectUrl = response.headers.location.toString()
    println("Keycloakログインページへのリダイレクト: $redirectUrl")
    val cookieHolder = mutableMapOf<String, String>()
    val connect = Jsoup.connect(redirectUrl)
    val loginPage = connect
      .also { it.cookies(cookieHolder) }
      .execute()
      .also {
        Assertions.assertThat(it.statusCode()).isEqualTo(200)
        cookieHolder.putAll(it.cookies())
      }
      .parse()

    // Keycloakでログイン
    var loginResponse = loginPage.selectFirst("#kc-form-login")
      .let { it as FormElement }
      .submit()
      .also {
        it.data("username").value("user0001")
        it.data("password").value("password123")
      }
      .cookies(cookieHolder)
      .followRedirects(false)
      .execute()
      .also {
        Assertions.assertThat(it.statusCode()).isEqualTo(302)
        cookieHolder.putAll(it.cookies())
      }

    // OAuth2認証コードへリダイレクト
    redirectUrl = loginResponse.header(HttpHeaders.LOCATION)
    println("OAuth2認証コードへのリダイレクト: $redirectUrl")
    request = MockServerHttpRequest.get(redirectUrl).cookies(cookies).build()
    response = MockServerHttpResponse()
    httpWebHandlerAdapter.handle(request, response).block()
    Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.FOUND)
    Assertions.assertThat(response.headers.location.toString()).isEqualTo("/")
  }
 */
}
