package io.github.novemdecillion.adapter.api

import graphql.kickstart.tools.GraphQLQueryResolver
import org.apache.commons.lang3.StringUtils
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AppApi: GraphQLQueryResolver {
  data class User(
    val username: String,
    val authorities: List<String>
  )

  fun currentUser(): Mono<User> {
    return ReactiveSecurityContextHolder.getContext()
      .map {
        return@map User(it.authentication.name, listOf())
      }
  }
}