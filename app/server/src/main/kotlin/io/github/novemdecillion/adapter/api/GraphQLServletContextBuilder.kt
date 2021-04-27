package io.github.novemdecillion.adapter.api

import graphql.kickstart.servlet.context.DefaultGraphQLServletContext
import graphql.kickstart.servlet.context.DefaultGraphQLServletContextBuilder
import graphql.kickstart.servlet.context.GraphQLServletContext
import graphql.schema.DataFetchingEnvironment
import io.github.novemdecillion.adapter.db.UserRepository
import io.github.novemdecillion.domain.User
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class GraphQLContext(servletContext: GraphQLServletContext, val now: OffsetDateTime, val user: User) : GraphQLServletContext by servletContext

@Component
class GraphQLServletContextBuilder(private val userRepository: UserRepository) : DefaultGraphQLServletContextBuilder() {
  @Transactional
  override fun build(request: HttpServletRequest, response: HttpServletResponse): GraphQLContext {
    val authentication = SecurityContextHolder.getContext().authentication
    val (accountName, realmId) = when(authentication) {
      is OAuth2AuthenticationToken -> authentication.name to authentication.authorizedClientRegistrationId
      else -> authentication.name to null
    }
    val user = userRepository.findByAccountNameAndRealmWithAuthority(accountName, realmId)!!
    val servletContext = DefaultGraphQLServletContext.createServletContext().with(request).with(response).build()
    return GraphQLContext(servletContext, OffsetDateTime.now(), user)
  }
}

fun DataFetchingEnvironment.currentUser(): User {
  return getContext<GraphQLContext>().user
}

fun DataFetchingEnvironment.now(): OffsetDateTime {
  return getContext<GraphQLContext>().now
}