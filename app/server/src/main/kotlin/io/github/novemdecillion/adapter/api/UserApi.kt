package io.github.novemdecillion.adapter.api

import graphql.kickstart.tools.GraphQLMutationResolver
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.schema.DataFetchingEnvironment
import io.github.novemdecillion.adapter.db.AccountRepository
import io.github.novemdecillion.adapter.db.UserRepository
import io.github.novemdecillion.adapter.jooq.tables.pojos.RealmEntity
import io.github.novemdecillion.adapter.oauth2.SyncKeycloakUsersService
import io.github.novemdecillion.domain.User
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Component

@Component
class UserApi(val userRepository: UserRepository,
              val accountRepository: AccountRepository,
              val syncUsersService: SyncKeycloakUsersService
): GraphQLQueryResolver, GraphQLMutationResolver {

  fun currentUser(environment: DataFetchingEnvironment): User {
    return environment.currentUser()
  }

  fun users(): List<User> {
    return userRepository.findAllWithAuthority()
  }

  fun userCount(): Int {
    return userRepository.count()
  }

  fun realms(): List<RealmEntity> {
    return accountRepository.selectRealm()
  }

  fun syncRealm(realmId: String?): Boolean {
    realmId
      ?.also {
        syncUsersService.syncRealm(it)
      }
      ?: syncUsersService.sync()
    return true
  }
}