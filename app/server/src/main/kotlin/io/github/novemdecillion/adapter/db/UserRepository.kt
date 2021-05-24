package io.github.novemdecillion.adapter.db

import io.github.novemdecillion.adapter.id.IdGeneratorService
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
class UserRepository(
  private val dslContext: DSLContext,
  private val materializedViewRepository: MaterializedViewRepository
) {

//  fun insertAuthority(userId: UUID, groupId: UUID, role: Role) {
//    val record = AccountGroupAuthorityRecord()
//      .also {
//        it.accountId = userId
//        it.groupTransitionId = groupId
//        it.role = role
//      }
//    dslContext.executeInsert(record)
//    materializedViewRepository.refreshCurrentAccountGroupAuthorityTable()
//  }
//
  fun insertAuthority(userId: UUID, authority: Authority) {
    insertAuthorityOnly(userId, authority)
    materializedViewRepository.refreshCurrentAccountGroupAuthorityTable()
  }

  private fun insertAuthorityOnly(userId: UUID, authority: Authority) {
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

  fun updateAuthorities(authorities: Collection<AccountGroupAuthorityEntity>) {
    authorities
      .groupBy { it.groupTransitionId }
      .forEach { (groupTransitionId, authoritiesAtGroup) ->
        dslContext.deleteFrom(ACCOUNT_GROUP_AUTHORITY)
          .where(
            ACCOUNT_GROUP_AUTHORITY.ACCOUNT_ID.`in`(authoritiesAtGroup.map { it.accountId })
              .and(ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID.equal(groupTransitionId))
          )
          .execute()
      }
    insertAuthorities(authorities)
  }

  fun updateAuthority(userId: UUID, authority: Authority) {
    dslContext.deleteFrom(ACCOUNT_GROUP_AUTHORITY)
      .where(
        ACCOUNT_GROUP_AUTHORITY.ACCOUNT_ID.equal(userId)
          .and(ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID.equal(authority.groupId))
      )
      .execute()
    insertAuthorityOnly(userId, authority)
    materializedViewRepository.refreshCurrentAccountGroupAuthorityTable()
  }

  fun deleteAuthorities(groupTransitionId: UUID, userIds: List<UUID>) {
    dslContext.deleteFrom(ACCOUNT_GROUP_AUTHORITY)
      .where(
        ACCOUNT_GROUP_AUTHORITY.ACCOUNT_ID.`in`(userIds)
          .and(ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID.equal(groupTransitionId))
      )
      .execute()
    materializedViewRepository.refreshCurrentAccountGroupAuthorityTable()
  }


  fun selectAuthorityByUserIdAndGroupId(userId: UUID, groupId: UUID): Authority? {
    return dslContext.selectFrom(CURRENT_ACCOUNT_GROUP_AUTHORITY)
      .where(CURRENT_ACCOUNT_GROUP_AUTHORITY.ACCOUNT_ID.equal(userId)
        .and(CURRENT_ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID.equal(groupId)))
      .fetchGroups(CURRENT_ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID)
      .map { record ->
        Authority(record.key!!, record.value.getValues(CURRENT_ACCOUNT_GROUP_AUTHORITY.ROLE).filterNotNull())
      }
      .firstOrNull()
  }

  private fun selectWithAuthority(statementModifier: (SelectWhereStep<Record>) -> ResultQuery<Record> = { it }): List<User> {
    val statement =
      CURRENT_ACCOUNT_GROUP_AUTHORITY.fields().filter { it.name != CURRENT_ACCOUNT_GROUP_AUTHORITY.ACCOUNT_ID.name }
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
//            .mapNotNull { (key, value) ->
//              if (null == key) {
//                return@mapNotNull null
//              }
//              val nonNullValues = value.filterNotNull()
//              if (nonNullValues.isEmpty()) {
//                return@mapNotNull null
//              }
//              return@mapNotNull key to nonNullValues
//            }
            .map { (groupTransitionId, roles) ->
              Authority(groupTransitionId!!, roles.filterNotNull())
            }
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

  fun findByAccountNameAndRealmWithAuthority(accountName: String, realm: String): User? {
    return selectWithAuthority { statement ->
      statement.where(ACCOUNT.ACCOUNT_NAME.equal(accountName))
        .and(ACCOUNT.REALM_ID.equal(realm))
    }.firstOrNull()
  }

  fun selectAppendableMemberByGroupTransitionId(groupTransitionId: UUID): List<User> {

    val parentGroupIdQuery = DSL.select(CURRENT_GROUP_TRANSITION.PARENT_GROUP_TRANSITION_ID)
      .from(CURRENT_GROUP_TRANSITION)
      .where(CURRENT_GROUP_TRANSITION.GROUP_TRANSITION_ID.equal(groupTransitionId))

    val appendableAccountIdsQuery = DSL.select(CURRENT_ACCOUNT_GROUP_AUTHORITY.ACCOUNT_ID)
      .from(CURRENT_ACCOUNT_GROUP_AUTHORITY)
      .where(
        CURRENT_ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID.equal(groupTransitionId)
          .or(CURRENT_ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID.equal(parentGroupIdQuery))
      )
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
      record.accountId!!, record.accountName!!, record.userName!!, record.email, record.realmId!!,
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