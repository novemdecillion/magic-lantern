package io.github.novemdecillion.adapter.db

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.novemdecillion.adapter.jooq.tables.pojos.AccountEntity
import io.github.novemdecillion.adapter.jooq.tables.pojos.RealmEntity
import io.github.novemdecillion.adapter.jooq.tables.records.AccountRecord
import io.github.novemdecillion.adapter.jooq.tables.records.RealmRecord
import io.github.novemdecillion.adapter.jooq.tables.references.*
import io.github.novemdecillion.domain.Authority
import io.github.novemdecillion.domain.Role
import io.github.novemdecillion.domain.User
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.ResultQuery
import org.jooq.SelectOnConditionStep
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class AccountRepository(
  private val dslContext: DSLContext,
  private val objectMapper: ObjectMapper
  ) {
  companion object {
    fun recordMapper(account: AccountEntity): AccountRecord {
      val record = AccountRecord()
      account.accountId?.also { record.accountId = it }
      account.accountName?.also { record.accountName = it }
      account.password?.also { record.password = it }
      account.userName?.also { record.userName = it }
      account.email?.also { record.email = it }
      account.locale?.also { record.locale = it }
      account.realmId?.also { record.realmId = it }
      account.enabled?.also { record.enabled = it }
      return record
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

  fun insert(account: AccountEntity): Int {
    require(account.accountId != null)
    val record = recordMapper(account)
    return dslContext.insertInto(ACCOUNT)
      .set(record)
      .execute()
  }

  fun update(account: AccountEntity): Int {
    require(account.accountId != null)
    val record = recordMapper(account)
    return dslContext.update(ACCOUNT)
      .set(record)
      .where(ACCOUNT.ACCOUNT_ID.equal(account.accountId))
      .execute()
  }

  fun changePassword(accountId: UUID, encodedNewPassword: String): Int {
    return dslContext.update(ACCOUNT)
      .set(ACCOUNT.PASSWORD, encodedNewPassword)
      .where(ACCOUNT.ACCOUNT_ID.equal(accountId))
      .execute()
  }

  fun delete(userId: UUID): Int {
    return dslContext.deleteFrom(ACCOUNT).where(ACCOUNT.ACCOUNT_ID.equal(userId)).execute()
  }

  fun updateEnableByAccountNameAndRealmId(accountNames: Collection<String>, realm: String, enabled: Boolean) {
    dslContext.updateQuery(ACCOUNT)
      .also {
        it.addConditions(ACCOUNT.ACCOUNT_NAME.`in`(accountNames)
          .and(ACCOUNT.REALM_ID.equal(realm)))
        it.addValue(ACCOUNT.ENABLED, enabled)
        it.execute()
      }
  }

  fun selectAccountNamesByRealmId(realm: String): List<String> {
    return dslContext.select(ACCOUNT.ACCOUNT_NAME).from(ACCOUNT)
      .where(ACCOUNT.REALM_ID.equal(realm))
      .fetchInto(String::class.java)
  }

  fun selectByAccountNameAndRealmId(accountName: String, realmId: String): AccountEntity? {
    return dslContext.selectFrom(ACCOUNT)
      .where(ACCOUNT.ACCOUNT_NAME.equal(accountName))
      .and(ACCOUNT.REALM_ID.equal(realmId))
      .fetchOneInto(AccountEntity::class.java)
  }

  fun selectByAccountNamesAndRealmId(accountNames: Collection<String>, realmId: String): List<AccountEntity> {
    return dslContext.selectFrom(ACCOUNT)
      .where(ACCOUNT.ACCOUNT_NAME.`in`(accountNames))
      .and(ACCOUNT.REALM_ID.equal(realmId))
      .fetchInto(AccountEntity::class.java)
  }

  private fun selectWithAuthority(statementModifier: (SelectOnConditionStep<Record>) -> ResultQuery<Record> = { it }): List<User> {
    val statement = dslContext.select(ACCOUNT.asterisk(), CURRENT_ACCOUNT_GROUP_AUTHORITY.asterisk())
      .from(ACCOUNT)
      .innerJoin(CURRENT_ACCOUNT_GROUP_AUTHORITY)
      .on(ACCOUNT.ACCOUNT_ID.equal(CURRENT_ACCOUNT_GROUP_AUTHORITY.ACCOUNT_ID))

    return statementModifier(statement)
      .fetchGroups(ACCOUNT)
      .map { (account, result) ->
        val authorities =
          result.into(CURRENT_ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID, CURRENT_ACCOUNT_GROUP_AUTHORITY.GROUP_GENERATION_ID, CURRENT_ACCOUNT_GROUP_AUTHORITY.ROLE)
            .map { (groupTransitionId, groupGenerationId, roles) ->
              Authority(groupTransitionId!!, groupGenerationId!!, objectMapper.readValue<Collection<Role>?>(roles)?.sortedBy { it.ordinal })
            }
        recordMapper(account).copy(authorities = authorities)
      }
  }

  fun selectByIds(userIds: Collection<UUID>): List<User> {
    return selectWithAuthority {
      it.where(ACCOUNT.ACCOUNT_ID.`in`(userIds))
    }
  }

  fun selectCount(): Int {
    return dslContext.selectCount().from(ACCOUNT)
      .fetchOneInto(Int::class.java) ?: 0
  }

  fun selectAllWithAuthority(): List<User> = selectWithAuthority {
    it.orderBy(ACCOUNT.USER_NAME)
  }

  fun selectByAccountNameAndRealmWithAuthority(accountName: String, realm: String): User? {
    return selectWithAuthority { statement ->
      statement.where(ACCOUNT.ACCOUNT_NAME.equal(accountName))
        .and(ACCOUNT.REALM_ID.equal(realm))
    }.firstOrNull()
  }

  fun selectAppendableMemberByGroupTransitionId(groupTransitionId: UUID, groupGenerationId: Int?): List<User> {
    val parentGroupIdQuery = DSL.select(GROUP_TRANSITION_WITH_PATH.PARENT_GROUP_TRANSITION_ID)
      .from(GROUP_TRANSITION_WITH_PATH)
      .where(GROUP_TRANSITION_WITH_PATH.GROUP_TRANSITION_ID.equal(groupTransitionId))

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

  fun selectMemberByGroupTransitionId(groupTransitionId: UUID, groupGenerationId: Int?): List<User> {
    return selectWithAuthority { statement ->
      statement.where(CURRENT_ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID.equal(groupTransitionId))
    }
  }
}