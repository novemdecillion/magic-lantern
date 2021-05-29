package io.github.novemdecillion.adapter.db

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.novemdecillion.adapter.jooq.tables.records.AccountRecord
import io.github.novemdecillion.adapter.jooq.tables.references.*
import io.github.novemdecillion.domain.*
import io.github.novemdecillion.domain.Role
import io.github.novemdecillion.domain.User
import org.jooq.*
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserRepository(
  private val dslContext: DSLContext,
  private val objectMapper: ObjectMapper
) {
  fun selectAuthorityByUserIdAndGroupId(userId: UUID, groupId: UUID, groupGenerationId: Int? = null): Authority? {
    return dslContext.selectFrom(CURRENT_ACCOUNT_GROUP_AUTHORITY)
      .where(
        CURRENT_ACCOUNT_GROUP_AUTHORITY.ACCOUNT_ID.equal(userId)
          .and(CURRENT_ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID.equal(groupId))
      )
      .fetchOne { record ->
        Authority(
          record.get(CURRENT_ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID)!!,
          record.get(CURRENT_ACCOUNT_GROUP_AUTHORITY.GROUP_GENERATION_ID)!!,
          objectMapper.readValue(record.get(CURRENT_ACCOUNT_GROUP_AUTHORITY.ROLE))
        )
      }
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
              Authority(groupTransitionId!!, groupGenerationId!!, objectMapper.readValue(roles))
            }
        recordMapper(account).copy(authorities = authorities)
      }
  }

  fun selectByIds(userIds: Collection<UUID>): List<User> {
    return selectWithAuthority {
      it.where(ACCOUNT.ACCOUNT_ID.`in`(userIds))
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

  fun selectAppendableMemberByGroupTransitionId(groupTransitionId: UUID, groupGenerationId: Int?): List<User> {
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

  fun selectMemberByGroupTransitionId(groupTransitionId: UUID, groupGenerationId: Int?): List<User> {
    return selectWithAuthority { statement ->
      statement.where(CURRENT_ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID.equal(groupTransitionId))
    }
  }

  fun selectByLessonIdAndNotExistStudy(lessonId: UUID): List<User> {
    // TODO 見直したい
    return dslContext.select(ACCOUNT.asterisk(), CURRENT_ACCOUNT_GROUP_AUTHORITY.asterisk())
      .from(ACCOUNT)
        .innerJoin(CURRENT_ACCOUNT_GROUP_AUTHORITY)
          .on(ACCOUNT.ACCOUNT_ID.equal(CURRENT_ACCOUNT_GROUP_AUTHORITY.ACCOUNT_ID)
            .and(DSL.jsonbExists(CURRENT_ACCOUNT_GROUP_AUTHORITY.ROLE,"""$ ? (@ == "${Role.STUDY.name}")"""))
          )
        .innerJoin(LESSON)
          .on(LESSON.LESSON_ID.equal(lessonId)
            .and(LESSON.GROUP_TRANSITION_ID.equal(CURRENT_ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID))
          )
        .innerJoin(STUDY).on(ACCOUNT.ACCOUNT_ID.notIn(STUDY.ACCOUNT_ID))
      .fetchGroups(ACCOUNT)
      .map { (account, result) ->
        val authorities =
          result.into(CURRENT_ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID, CURRENT_ACCOUNT_GROUP_AUTHORITY.GROUP_GENERATION_ID, CURRENT_ACCOUNT_GROUP_AUTHORITY.ROLE)
            .map { (groupTransitionId, groupGenerationId, roles) ->
              Authority(groupTransitionId!!, groupGenerationId!!, objectMapper.readValue(roles))
            }
        recordMapper(account).copy(authorities = authorities)
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