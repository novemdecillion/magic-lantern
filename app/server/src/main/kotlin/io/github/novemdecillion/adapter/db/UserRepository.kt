package io.github.novemdecillion.adapter.db

import io.github.novemdecillion.adapter.jooq.tables.pojos.AccountGroupAuthorityEntity
import io.github.novemdecillion.adapter.jooq.tables.records.AccountGroupAuthorityRecord
import io.github.novemdecillion.adapter.jooq.tables.records.AccountRecord
import io.github.novemdecillion.adapter.jooq.tables.references.ACCOUNT
import io.github.novemdecillion.adapter.jooq.tables.references.ACCOUNT_GROUP_AUTHORITY
import io.github.novemdecillion.adapter.jooq.tables.references.CURRENT_ACCOUNT_GROUP_AUTHORITY
import io.github.novemdecillion.adapter.jooq.tables.references.CURRENT_GROUP_TRANSITION
import io.github.novemdecillion.domain.*
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.ResultQuery
import org.jooq.SelectWhereStep
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserRepository(private val dslContext: DSLContext,
                     private val materializedViewRepository: MaterializedViewRepository) {

//  fun insert(user: User) {
//    val record = recordMapper(user)
//    dslContext.insertInto(ACCOUNT)
//        .set(record)
//        .execute()
//  }
//
//  fun update(user: User) {
//    val record = recordMapper(user)
//    dslContext.update(ACCOUNT)
//        .set(record)
//        .execute()
//  }

  fun insertAuthority(userId: UUID, authority: Authority) {
    val records = authority.roles
      .map { role ->
        AccountGroupAuthorityRecord()
          .also {
            it.accountId = userId
            it.groupTransitionId = authority.groupId
            it.role = role
          }
      }
    dslContext.batchInsert(records).execute()
    materializedViewRepository.refreshCurrentAccountGroupAuthorityTable()
  }

  fun insertAuthorities(authorities: Collection<AccountGroupAuthorityEntity>) {
    dslContext.batchInsert(authorities
      .map { authority ->
        AccountGroupAuthorityRecord()
          .also {
            it.from(authority)
          }
      }).execute()
    materializedViewRepository.refreshCurrentAccountGroupAuthorityTable()
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
          result.intoGroups(CURRENT_ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID, CURRENT_ACCOUNT_GROUP_AUTHORITY.ROLE)
            .mapNotNull { (key, value) ->
              if (null == key) {
                return@mapNotNull null
              }
              val nonNullValues = value.filterNotNull()
              if (nonNullValues.isEmpty()) {
                return@mapNotNull null
              }
              return@mapNotNull key to nonNullValues
            }
            .map { (groupTransitionId, roles) ->
              Authority(groupTransitionId, roles)
            }
//            .let { groupIdToRoles ->
//              return@let groupIdToRoles
//                .firstOrNull { it.groupId == ENTIRE_GROUP_ID }
//                ?.let { groupIdToRoles }
//                ?: run {
//                  // 全体グループのデフォルト権限を追加
//                  groupIdToRoles.plus(Authority(ENTIRE_GROUP_ID, listOf(Role.STUDENT)))
//                }
//            }
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
    return selectWithAuthority { statement ->
      statement.where(ACCOUNT.ACCOUNT_NAME.equal(accountName))
        .and(realm
          ?.let { ACCOUNT.REALM_ID.equal(it) }
          ?: ACCOUNT.REALM_ID.isNull)
    }.firstOrNull()
  }

  fun selectAppendableMemberByGroupTransitionId(groupTransitionId: UUID): List<User> {

    val parentGroupIdQuery = DSL.select(CURRENT_GROUP_TRANSITION.PARENT_GROUP_TRANSITION_ID)
      .from(CURRENT_GROUP_TRANSITION)
      .where(CURRENT_GROUP_TRANSITION.GROUP_TRANSITION_ID.equal(groupTransitionId))

    val appendableAccountIdsQuery = DSL.select(CURRENT_ACCOUNT_GROUP_AUTHORITY.ACCOUNT_ID)
      .from(CURRENT_ACCOUNT_GROUP_AUTHORITY)
      .where(CURRENT_ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID.equal(groupTransitionId)
        .or(CURRENT_ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID.equal(parentGroupIdQuery)))
      .groupBy(CURRENT_ACCOUNT_GROUP_AUTHORITY.ACCOUNT_ID)
      .having(
        DSL.arrayAgg(CURRENT_ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID).notContains(arrayOf(groupTransitionId))
      )

    return selectWithAuthority { statement ->
      statement.where(ACCOUNT.ACCOUNT_ID.`in`(appendableAccountIdsQuery))
    }
  }

  fun selectMemberByGroupTransitionId(groupTransitionId: UUID): List<User> {
    return selectWithAuthority { statement ->
      statement.where(CURRENT_ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID.equal(groupTransitionId))
    }
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