package io.github.novemdecillion.adapter.security

import io.github.novemdecillion.domain.SYSTEM_REALM_ID
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken

fun currentAccount(): Pair<String, String> {
  return when(val authentication = SecurityContextHolder.getContext().authentication) {
    is OAuth2AuthenticationToken -> authentication.name to authentication.authorizedClientRegistrationId
    else -> authentication.name to SYSTEM_REALM_ID
  }
}