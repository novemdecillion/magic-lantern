package io.github.novemdecillion.adapter.db

import io.github.novemdecillion.adapter.jooq.tables.pojos.AccountEntity
import io.github.novemdecillion.adapter.jooq.tables.pojos.CurrentAccountGroupAuthorityEntity
import io.github.novemdecillion.adapter.jooq.tables.references.ACCOUNT
import io.github.novemdecillion.adapter.jooq.tables.references.CURRENT_ACCOUNT_GROUP_AUTHORITY
import io.github.novemdecillion.adapter.jooq.tables.references.CURRENT_GROUP_TRANSITION
import io.github.novemdecillion.domain.Authority
import io.github.novemdecillion.domain.User
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.ResultQuery
import org.jooq.SelectWhereStep
import org.springframework.stereotype.Repository
import javax.annotation.PostConstruct
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.transaction.support.TransactionTemplate


@Repository
class UserRepository(private val dslContext: DSLContext) {

  private val selectWithAuthorityStatement: SelectWhereStep<Record>
  init {
    selectWithAuthorityStatement = CURRENT_ACCOUNT_GROUP_AUTHORITY.fields().filter { it.name != CURRENT_ACCOUNT_GROUP_AUTHORITY.ACCOUNT_ID.name }
      .let {
        dslContext.select(ACCOUNT.asterisk(), *it.toTypedArray())
          .from(ACCOUNT)
          .leftOuterJoin(CURRENT_ACCOUNT_GROUP_AUTHORITY)
          .on(ACCOUNT.ACCOUNT_ID.equal(CURRENT_ACCOUNT_GROUP_AUTHORITY.ACCOUNT_ID))
      }
  }

  fun selectWithAuthority(statementModifier: (SelectWhereStep<Record>)-> ResultQuery<Record> = { it }): List<User> {
    return statementModifier(selectWithAuthorityStatement)
      .fetchGroups(ACCOUNT)
      .map {
        it.key.into(AccountEntity::class.java) to it.value.into(CurrentAccountGroupAuthorityEntity::class.java)
      }
      .map { (account, authorities) ->
        convert(account, authorities)
      }
  }

  fun findAll(): List<User> = selectWithAuthority()

  fun findByAccountNameAndRealm(accountName: String, realm: String?): User? {
    return selectWithAuthority {
      it.where(ACCOUNT.ACCOUNT_NAME.equal(accountName))
      .and(realm
        ?.let { ACCOUNT.REALM.equal(it) }
        ?: ACCOUNT.REALM.isNull)
    }.firstOrNull()
  }

  fun convert(account: AccountEntity, authorities: Collection<CurrentAccountGroupAuthorityEntity>): User {
    return User(account.accountId!!, account.userName!!, account.realm, account.enabled!!,
      authorities
        .map {
          Authority(it.groupOriginId!!, it.role!!)
        }.toSet()
    )
  }

  fun refreshCurrentAccountGroupAuthorityTable() {
    dslContext.execute("refresh materialized view ${CURRENT_ACCOUNT_GROUP_AUTHORITY.name}")
  }

  fun refreshCurrentGroupTransitionTable() {
    dslContext.execute("refresh materialized view ${CURRENT_GROUP_TRANSITION.name}")
  }

  @EventListener(ContextRefreshedEvent::class)
  fun onApplicationEvent(event: ContextRefreshedEvent) {
    event.applicationContext.getBean(TransactionTemplate::class.java)
      .execute {
        refreshCurrentAccountGroupAuthorityTable()
        refreshCurrentGroupTransitionTable()
      }
  }

}