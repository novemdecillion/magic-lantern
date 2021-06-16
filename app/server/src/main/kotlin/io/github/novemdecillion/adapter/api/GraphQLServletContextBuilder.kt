package io.github.novemdecillion.adapter.api

import graphql.kickstart.servlet.context.DefaultGraphQLServletContext
import graphql.kickstart.servlet.context.DefaultGraphQLServletContextBuilder
import graphql.kickstart.servlet.context.GraphQLServletContext
import graphql.schema.DataFetchingEnvironment
import io.github.novemdecillion.adapter.db.AccountRepository
import io.github.novemdecillion.adapter.db.GroupRepository
import io.github.novemdecillion.adapter.security.currentAccount
import io.github.novemdecillion.domain.User
import org.dataloader.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.reflect.KClass

class GraphQLContext(
  servletContext: GraphQLServletContext,
  val now: OffsetDateTime,
  val user: User,
  val currentGroupGenerationId: Int
) : GraphQLServletContext by servletContext

interface LoaderFunctionMaker<K, V>

@Component
class GraphQLServletContextBuilder(
  private val userRepository: AccountRepository,
  private val groupRepository: GroupRepository,
  @Autowired(required = false) private val loaderFunctions: Collection<LoaderFunctionMaker<*, *>>?) : DefaultGraphQLServletContextBuilder() {

  val dataLoaderRegistry: DataLoaderRegistry = DataLoaderRegistry()
    .also { registry ->
      if (!loaderFunctions.isNullOrEmpty()) {
        val options = DataLoaderOptions().setCachingEnabled(false)

        loaderFunctions
          .forEach {
            val dataLoader = when (it) {
              is BatchLoader<*, *> -> DataLoader.newDataLoader(it, options)
              is BatchLoaderWithContext<*, *> -> DataLoader.newDataLoader(it, options)
              is MappedBatchLoader<*, *> -> DataLoader.newMappedDataLoader(it, options)
              is MappedBatchLoaderWithContext<*, *> -> DataLoader.newMappedDataLoader(it, options)
              else -> return@forEach
            }
            registry.register(it::class.java.simpleName, dataLoader)
          }
      }
    }

  @Transactional(rollbackFor = [Throwable::class])
  override fun build(request: HttpServletRequest, response: HttpServletResponse): GraphQLContext {
    val (accountName, realmId) = currentAccount()
    val user = userRepository.selectByAccountNameAndRealmWithAuthority(accountName, realmId)!!
    val currentGroupGenerationId = groupRepository.selectCurrentGroupGenerationId()!!
    val servletContext = DefaultGraphQLServletContext.createServletContext().with(dataLoaderRegistry).with(request).with(response).build()
    return GraphQLContext(servletContext, OffsetDateTime.now(), user, currentGroupGenerationId)
  }
}

fun DataFetchingEnvironment.currentUser(): User {
  return getContext<GraphQLContext>().user
}

fun DataFetchingEnvironment.currentGroupGenerationId(): Int {
  return getContext<GraphQLContext>().currentGroupGenerationId
}

fun DataFetchingEnvironment.now(): OffsetDateTime {
  return getContext<GraphQLContext>().now
}

fun <K, V, T: LoaderFunctionMaker<K, V>> DataFetchingEnvironment.dataLoader(key: KClass<T>): DataLoader<K, V> {
  return getContext<GraphQLContext>().dataLoaderRegistry.getDataLoader(key.java.simpleName)
}

