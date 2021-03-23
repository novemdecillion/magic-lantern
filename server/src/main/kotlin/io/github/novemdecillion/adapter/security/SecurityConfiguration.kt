package io.github.novemdecillion.adapter.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.User

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.web.server.DefaultServerRedirectStrategy
import org.springframework.security.web.server.ServerRedirectStrategy
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.net.URI


@Configuration
class SecurityConfiguration(val clientRegistrationRepository: ReactiveClientRegistrationRepository) {
  private val redirectStrategy: ServerRedirectStrategy = DefaultServerRedirectStrategy()
//  @Autowired
//  lateinit var clientRegistrationRepository: ReactiveClientRegistrationRepository

  @Bean
  fun appSecurityFilterChain(http: ServerHttpSecurity, jwtDecoder: ReactiveJwtDecoder): SecurityWebFilterChain {
    // @formatter:off
    http
      .securityMatcher(ServerWebExchangeMatchers.pathMatchers("/api/**", "/", "/login", "/login/**", "/oauth2/**", "/logout"))
      .authorizeExchange()
        .pathMatchers("/login", "/login/**", "/oauth2/**").permitAll()
        .anyExchange().authenticated()
      .and()
      .formLogin()
        .loginPage("/login")
//        .authenticationSuccessHandler(RedirectServerAuthenticationSuccessHandler("/admin"))
      .and()
      .logout()
//      .logoutUrl("/admin/logout")
        .logoutSuccessHandler(oidcLogoutSuccessHandler())
      .and()
      .oauth2Login()
//      .and()
//        .oauth2Client()
      .and()
      .csrf()
        .requireCsrfProtectionMatcher(ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST,"/login"))
        .accessDeniedHandler(::csrfAccessDeniedHandler)
      .and()
      .headers()
        // デフォルトのDENYだとiframeに教材を埋め込み表示できないので変更
        .frameOptions().mode(XFrameOptionsServerHttpHeadersWriter.Mode.SAMEORIGIN)
    // @formatter:on

//    http.oauth2ResourceServer { server: ServerHttpSecurity.OAuth2ResourceServerSpec ->
//      server.jwt { jwt: ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec ->
//        jwt.jwtDecoder(jwtDecoder)
//      }
//    }

    return http.build()
  }

  @Bean
  fun passwordEncoder(): PasswordEncoder {
    return BCryptPasswordEncoder()
  }

  @Bean
  fun userDetailsService(): MapReactiveUserDetailsService? {
    val user: UserDetails = User
      .withUsername("admin")
      .password(passwordEncoder().encode("password123"))
      .roles("ADMIN")
      .build()
    return MapReactiveUserDetailsService(user)
  }

  fun oidcLogoutSuccessHandler(): OidcClientInitiatedServerLogoutSuccessHandler {
    val successHandler = OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository)
    successHandler.setPostLogoutRedirectUri("{baseUrl}")
    return successHandler
  }

  fun csrfAccessDeniedHandler(exchange: ServerWebExchange, denied: AccessDeniedException): Mono<Void> {
    return redirectStrategy.sendRedirect(exchange, URI.create("/login"))
  }

//  class AuthenticationSuccessHandler: RedirectServerAuthenticationSuccessHandler() {
//    private val redirectStrategy = DefaultServerRedirectStrategy()
//    init {
//      setRedirectStrategy(redirectStrategy)
//    }
//    override fun onAuthenticationSuccess(webFilterExchange: WebFilterExchange, authentication: Authentication): Mono<Void> {
//      return if (authentication is UsernamePasswordAuthenticationToken) {
//        redirectStrategy.sendRedirect(webFilterExchange, )
//      } else {
//        super.onAuthenticationSuccess(webFilterExchange, authentication)
//      }
//    }
//  }
}

