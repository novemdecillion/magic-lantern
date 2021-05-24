package io.github.novemdecillion.adapter.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RegexRequestMatcher
import javax.servlet.http.HttpServletRequest


@Configuration
class SecurityConfiguration(val clientRegistrationRepository: ClientRegistrationRepository): WebSecurityConfigurerAdapter() {
  companion object {
    const val PATH_LOGIN = "/login"
    const val PATH_LOGOUT = "/logout"
  }

  override fun configure(web: WebSecurity) {
    web.ignoring().antMatchers("/webjars/**", "/public/**")
  }

  override fun configure(http: HttpSecurity) {
    // @formatter:off
    http
      .authorizeRequests()
        .antMatchers("/login/**", "/oauth2/**").permitAll()
        .anyRequest().authenticated()
        .and()
      .formLogin()
        .loginPage(PATH_LOGIN).permitAll()
        .defaultSuccessUrl("/", true)
        .and()
      .logout()
        // デフォルトだとPOSTしか受け付けない
        .logoutRequestMatcher(AntPathRequestMatcher(PATH_LOGOUT, HttpMethod.GET.name))
        .logoutSuccessHandler(oidcLogoutSuccessHandler())
        .logoutSuccessUrl(PATH_LOGIN)
        .and()
      .oauth2Login()
        .loginPage(PATH_LOGIN).permitAll()
        .and()
      .csrf()
        .requireCsrfProtectionMatcher(AntPathRequestMatcher(PATH_LOGIN, HttpMethod.POST.name))
        .and()
      .headers()
        // デフォルトのDENYだとiframeに教材を埋め込み表示できないので変更
        .frameOptions().sameOrigin()
        .and()
      .exceptionHandling()
        // Ajaxならログインページにリダイレクトされないようにする
        .defaultAuthenticationEntryPointFor({_, response, _ ->
          response.status = HttpStatus.FORBIDDEN.value()
        }, AntPathRequestMatcher("/api/**"))
    // @formatter:on
  }

  @Bean
  fun passwordEncoder(): PasswordEncoder {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder()
  }

  fun oidcLogoutSuccessHandler(): OidcClientInitiatedLogoutSuccessHandler {
    val successHandler = OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository)
    successHandler.setPostLogoutRedirectUri("{baseUrl}$PATH_LOGIN")
    return successHandler
  }

}

