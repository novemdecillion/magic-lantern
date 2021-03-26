package io.github.novemdecillion.adapter.api

import graphql.kickstart.tools.GraphQLQueryResolver
import io.github.novemdecillion.adapter.db.UserRepository
import io.github.novemdecillion.domain.User
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.stereotype.Component

@Component
class UserApi(val userRepository: UserRepository): GraphQLQueryResolver {

  fun currentUser(): User? {
    val authentication = SecurityContextHolder.getContext().authentication

    val realm = if (authentication is OAuth2AuthenticationToken) {
      authentication.authorizedClientRegistrationId
    }
    else null

    return userRepository.findByAccountNameAndRealm(authentication.name, realm)
  }

  fun users(): List<User> {
    return userRepository.findAll()
  }

}