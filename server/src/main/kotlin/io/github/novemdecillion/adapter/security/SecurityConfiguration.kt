package io.github.novemdecillion.adapter.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.User

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers


@Configuration
class SecurityConfiguration {
  @Bean
  @Order(1)
  fun appSecurityFilterChain(http: ServerHttpSecurity, jwtDecoder: ReactiveJwtDecoder): SecurityWebFilterChain {
    // @formatter:off
    http
      .securityMatcher(ServerWebExchangeMatchers.pathMatchers("/api/**", "/api"))
      .authorizeExchange()
        .pathMatchers("/api/**").authenticated()
      .and()
        .formLogin().disable()
      .oauth2Login()
      .and()
        .oauth2Client()
      .and()
        .csrf().disable()
      .headers()
        // デフォルトのDENYだとiframeに教材を埋め込み表示できないので変更
        .frameOptions().mode(XFrameOptionsServerHttpHeadersWriter.Mode.SAMEORIGIN)
    // @formatter:on

    http.oauth2ResourceServer { server: ServerHttpSecurity.OAuth2ResourceServerSpec ->
      server.jwt { jwt: ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec ->
        jwt.jwtDecoder(jwtDecoder)
      }
    }

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

  @Bean
  @Order(2)
  fun adminSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
    // @formatter:off
    http
      .securityMatcher(ServerWebExchangeMatchers.pathMatchers("/admin/**", "/admin"))
      .authorizeExchange()
        .pathMatchers("/admin/login").permitAll()
        .pathMatchers("/admin/**", "/admin").authenticated()
        .anyExchange().permitAll()
      .and()
      .formLogin()
        .loginPage("/admin/login")
    // @formatter:on

    return http.build()
  }

//  @Bean
//  @Order(3)
//  fun globalSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
//    // @formatter:off
//    http
//      .securityMatcher(NegatedServerWebExchangeMatcher(ServerWebExchangeMatchers.pathMatchers("/webjars/**", "/public/**")))
//      .authorizeExchange()
//        .anyExchange().permitAll()
//    // @formatter:on
//
//    return http.build()
//  }

}