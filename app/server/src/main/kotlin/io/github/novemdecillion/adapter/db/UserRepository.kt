package io.github.novemdecillion.adapter.db

import io.github.novemdecillion.adapter.jooq.tables.records.AccountRecord
import io.github.novemdecillion.adapter.jooq.tables.references.ACCOUNT
import io.github.novemdecillion.adapter.jooq.tables.references.CURRENT_ACCOUNT_GROUP_AUTHORITY
import io.github.novemdecillion.domain.Authority
import io.github.novemdecillion.domain.ENTIRE_GROUP_ID
import io.github.novemdecillion.domain.Role
import io.github.novemdecillion.domain.User
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.ResultQuery
import org.jooq.SelectWhereStep
import org.springframework.stereotype.Repository

@Repository
class UserRepository(private val dslContext: DSLContext) {

  fun insert(user: User) {
    val record = recordMapper(user)
    dslContext.insertQuery(ACCOUNT)
      .also {
        it.setRecord(record)
        it.execute()
      }
  }

  fun update(user: User) {
    val record = recordMapper(user)
    dslContext.updateQuery(ACCOUNT)
      .also {
        it.setRecord(record)
        it.execute()
      }
  }

  private fun selectWithAuthority(statementModifier: (SelectWhereStep<Record>) -> ResultQuery<Record> = { it }): List<User> {
    val statement = CURRENT_ACCOUNT_GROUP_AUTHORITY.fields().filter { it.name != CURRENT_ACCOUNT_GROUP_AUTHORITY.ACCOUNT_ID.name }
      .let {
        dslContext.select(ACCOUNT.asterisk(), *it.toTypedArray())
          .from(ACCOUNT)
          .leftOuterJoin(CURRENT_ACCOUNT_GROUP_AUTHORITY)
          .on(ACCOUNT.ACCOUNT_ID.equal(CURRENT_ACCOUNT_GROUP_AUTHORITY.ACCOUNT_ID))
      }

    return statementModifier(statement)
      .fetchGroups(ACCOUNT)
      .map { (account, result) ->

        val authorities =
          result.into(CURRENT_ACCOUNT_GROUP_AUTHORITY.GROUP_ORIGIN_ID, CURRENT_ACCOUNT_GROUP_AUTHORITY.ROLE)
            .map { record ->
              record.value1()
                ?.let {
                  Authority(it, record.value2()!!)
                }
                ?: Authority(ENTIRE_GROUP_ID, Role.STUDENT)
            }
            .toSet()
        recordMapper(account).copy(authorities = authorities)
      }
  }

  fun count(): Int {
    return dslContext.selectCount().from(ACCOUNT)
      .fetchOneInto(Int::class.java) ?: 0
  }

  fun findAllWithAuthority(): List<User> = selectWithAuthority {
    it.orderBy(ACCOUNT.USER_NAME)
  }

  fun findByAccountNameAndRealmWithAuthority(accountName: String, realm: String?): User? {
    return selectWithAuthority {
      it.where(ACCOUNT.ACCOUNT_NAME.equal(accountName))
        .and(realm
          ?.let { ACCOUNT.REALM_ID.equal(it) }
          ?: ACCOUNT.REALM_ID.isNull)
    }.firstOrNull()
  }

  fun recordMapper(record: AccountRecord): User {
    return User(
      record.accountId!!, record.accountName!!, record.userName!!, record.email, record.realmId,
      record.enabled!!
    )
  }

  fun recordMapper(user: User): AccountRecord {
    // passwordとlocaleは反映できない
    return AccountRecord()
      .also {
        it.accountId = user.userId
        it.accountName = user.accountName
        it.userName = user.userName
        it.email = user.email
        it.realmId = user.realmId
        it.enabled = user.enabled
      }
  }

}