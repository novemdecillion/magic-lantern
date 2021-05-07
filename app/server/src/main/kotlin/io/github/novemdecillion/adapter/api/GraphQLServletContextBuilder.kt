package io.github.novemdecillion.adapter.api

import graphql.kickstart.servlet.context.DefaultGraphQLServletContext
import graphql.kickstart.servlet.context.DefaultGraphQLServletContextBuilder
import graphql.kickstart.servlet.context.GraphQLServletContext
import graphql.schema.DataFetchingEnvironment
import io.github.novemdecillion.adapter.db.UserRepository
import io.github.novemdecillion.domain.User
import org.dataloader.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class GraphQLContext(servletContext: GraphQLServletContext, val now: OffsetDateTime, val user: User) : GraphQLServletContext by servletContext

interface LoaderFunctionMaker

@Component
class GraphQLServletContextBuilder(private val userRepository: UserRepository, @Autowired(required = false) private val loaderFunctions: Collection<LoaderFunctionMaker>?) : DefaultGraphQLServletContextBuilder() {

  val dataLoaderRegistry: DataLoaderRegistry = DataLoaderRegistry()
    .also { registry ->
      if (!loaderFunctions.isNullOrEmpty()) {
        loaderFunctions
          .forEach {
            val dataLoader = when (it) {
              is BatchLoader<*, *> -> DataLoader.newDataLoader(it)
              is BatchLoaderWithContext<*, *> -> DataLoader.newDataLoader(it)
              is MappedBatchLoader<*, *> -> DataLoader.newMappedDataLoader(it)
              is MappedBatchLoaderWithContext<*, *> -> DataLoader.newMappedDataLoader(it)
              else -> return@forEach
            }
            registry.register(it::class.java.simpleName, dataLoader)
          }
      }
    }

  @Transactional
  override fun build(request: HttpServletRequest, response: HttpServletResponse): GraphQLContext {
    val authentication = SecurityContextHolder.getContext().authentication
    val (accountName, realmId) = when(authentication) {
      is OAuth2AuthenticationToken -> authentication.name to authentication.authorizedClientRegistrationId
      else -> authentication.name to null
    }
    val user = userRepository.findByAccountNameAndRealmWithAuthority(accountName, realmId)!!
    val servletContext = DefaultGraphQLServletContext.createServletContext().with(dataLoaderRegistry).with(request).with(response).build()
    return GraphQLContext(servletContext, OffsetDateTime.now(), user)
  }
}

fun DataFetchingEnvironment.currentUser(): User {
  return getContext<GraphQLContext>().user
}

fun DataFetchingEnvironment.now(): OffsetDateTime {
  return getContext<GraphQLContext>().now
}

fun <K, V> DataFetchingEnvironment.getDataLoader(key: String): DataLoader<K, V> {
  return getContext<GraphQLContext>().dataLoaderRegistry.getDataLoader(key)
}

