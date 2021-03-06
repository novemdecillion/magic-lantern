package io.github.novemdecillion.adapter.security

import io.github.novemdecillion.adapter.jooq.tables.pojos.AccountEntity
import io.github.novemdecillion.adapter.jooq.tables.references.ACCOUNT
import io.github.novemdecillion.domain.SYSTEM_REALM_ID
import org.jooq.DSLContext
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDetailsService(val dslContext: DSLContext): org.springframework.security.core.userdetails.UserDetailsService {
  @Transactional(readOnly = true, rollbackFor = [Exception::class])
  override fun loadUserByUsername(username: String): UserDetails? {
    return dslContext.selectFrom(ACCOUNT)
      .where(ACCOUNT.ACCOUNT_NAME.equal(username)
        .and(ACCOUNT.REALM_ID.equal(SYSTEM_REALM_ID))
        .and(ACCOUNT.ENABLED.equal(true)))
      .fetchOneInto(AccountEntity::class.java)
      ?.let {
        User(it.accountName, it.password, it.enabled!!, true, true, true, listOf())
      }
      ?: throw UsernameNotFoundException(username)
  }
}