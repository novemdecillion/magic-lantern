package io.github.novemdecillion.adapter.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter

@Configuration
class SecurityConfiguration {
  @Bean
  fun springSecurityFilterChain(http: ServerHttpSecurity, jwtDecoder: ReactiveJwtDecoder): SecurityWebFilterChain {
    http
      .authorizeExchange().anyExchange().authenticated()
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

    http.oauth2ResourceServer { server: ServerHttpSecurity.OAuth2ResourceServerSpec ->
      server.jwt { jwt: ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec ->
        jwt.jwtDecoder(jwtDecoder)
      }
    }

    return http.build()
  }
}