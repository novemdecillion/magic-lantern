package io.github.novemdecillion.adapter.db

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.novemdecillion.adapter.jooq.tables.records.AccountGroupAuthorityRecord
import io.github.novemdecillion.adapter.jooq.tables.references.ACCOUNT_GROUP_AUTHORITY
import io.github.novemdecillion.adapter.jooq.tables.references.CURRENT_ACCOUNT_GROUP_AUTHORITY
import io.github.novemdecillion.domain.Authority
import io.github.novemdecillion.domain.ROOT_GROUP_GENERATION_ID
import io.github.novemdecillion.domain.ROOT_GROUP_ID
import io.github.novemdecillion.domain.Role
import org.apache.commons.lang3.StringUtils
import org.jooq.DSLContext
import org.jooq.JSONB
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class GroupAuthorityRepository(
  private val dslContext: DSLContext,
  private val objectMapper: ObjectMapper
) {
  private fun rolesToJsonb(roles: Collection<Role>?): JSONB? {
    return objectMapper.writeValueAsJsonb(roles?.ifEmpty { null }?.sortedBy { it.ordinal })
  }

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
          objectMapper.readValue<Collection<Role>?>(record.get(CURRENT_ACCOUNT_GROUP_AUTHORITY.ROLE))?.sortedBy { it.ordinal }
        )
      }
  }

  fun selectCount(
    groupTransitionIds: Collection<UUID>,
    groupGenerationId: Int? = null,
    role: Role? = null
  ): List<Pair<UUID, Int>> {
    return dslContext.select(ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID, DSL.count()).from(ACCOUNT_GROUP_AUTHORITY)
      .where(ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID.`in`(groupTransitionIds)
        .let { statement ->
          if (groupGenerationId == null) {
            statement.and(ACCOUNT_GROUP_AUTHORITY.GROUP_GENERATION_ID.equal(GroupRepository.selectCurrentGroupGenerationId()))
          } else {
            statement.and(ACCOUNT_GROUP_AUTHORITY.GROUP_GENERATION_ID.`in`(groupGenerationId, ROOT_GROUP_GENERATION_ID))
          }
        }
        .let { statement ->
          if (role != null) {
            statement.and(DSL.jsonbExists(ACCOUNT_GROUP_AUTHORITY.ROLE, """$ ? (@ == "${role.name}")"""))
          } else statement
        })
      .groupBy(ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID)
      .fetch { record ->
        record.value1()!! to record.value2()
      }
  }

  fun insertAuthority(userId: UUID, authority: Authority) {
    dslContext.insertInto(ACCOUNT_GROUP_AUTHORITY)
      .set(ACCOUNT_GROUP_AUTHORITY.ACCOUNT_ID, userId)
      .set(ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID, authority.groupId)
      .set(ACCOUNT_GROUP_AUTHORITY.GROUP_GENERATION_ID, authority.groupGenerationInt)
      .set(ACCOUNT_GROUP_AUTHORITY.ROLE, rolesToJsonb(authority.roles))
      .execute()
  }

  fun insertAuthorities(userId: UUID, authorities: Collection<Authority>) {
    insertAuthorities(listOf(userId to authorities))
  }

  fun insertAuthorities(authorities: Collection<Pair<UUID, Collection<Authority>>>) {
    dslContext.batchInsert(authorities
      .flatMap { userIdToAuthority ->
        userIdToAuthority.second.map { authority ->
          AccountGroupAuthorityRecord(
            userIdToAuthority.first,
            authority.groupId,
            authority.groupGenerationInt,
            rolesToJsonb(authority.roles)
          )

        }
      }).execute()
  }

  fun updateAuthorities(userIds: Collection<UUID>, groupTransitionId: UUID, groupGenerationId: Int, setRoles: Collection<Role>) {
    dslContext.update(ACCOUNT_GROUP_AUTHORITY)
      .set(ACCOUNT_GROUP_AUTHORITY.ROLE, rolesToJsonb(setRoles))
      .where(ACCOUNT_GROUP_AUTHORITY.ACCOUNT_ID.`in`(userIds)
        .and(ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID.equal(groupTransitionId))
        .and(ACCOUNT_GROUP_AUTHORITY.GROUP_GENERATION_ID.equal(groupGenerationId)))
      .execute()
  }

  fun updateAuthorities(userIds: Collection<UUID>, groupTransitionId: UUID, groupGenerationId: Int, removeRoles: Collection<Role>, addRoles: Collection<Role>) {
    var updateFieldStatement = ACCOUNT_GROUP_AUTHORITY.ROLE.name
    removeRoles
      .forEach {
        updateFieldStatement += "- '$it'"

      }
    val addRolesJson = if (addRoles.isNotEmpty()) {
      addRoles.joinToString(prefix = "'[", postfix = "]'") { """ "$it" """ }
        .also {
          updateFieldStatement += "|| $it"
        }
    } else StringUtils.EMPTY

    dslContext.update(ACCOUNT_GROUP_AUTHORITY)
      .set(ACCOUNT_GROUP_AUTHORITY.ROLE,
        DSL.`when`(ACCOUNT_GROUP_AUTHORITY.ROLE.isNull, DSL.field(addRolesJson, JSONB::class.java))
          .otherwise(DSL.field(updateFieldStatement, JSONB::class.java))
      )
      .where(ACCOUNT_GROUP_AUTHORITY.ACCOUNT_ID.`in`(userIds)
        .and(ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID.equal(groupTransitionId))
        .and(ACCOUNT_GROUP_AUTHORITY.GROUP_GENERATION_ID.equal(groupGenerationId)))
      .execute()
  }

  fun deleteAuthorities(groupTransitionId: UUID, userIds: List<UUID>, groupGenerationId: Int? = null) {
    dslContext.deleteFrom(ACCOUNT_GROUP_AUTHORITY)
      .where(
        ACCOUNT_GROUP_AUTHORITY.ACCOUNT_ID.`in`(userIds)
          .and(ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID.equal(groupTransitionId))
          .let { statement ->
            when {
              groupTransitionId == ROOT_GROUP_ID -> {
                statement.and(ACCOUNT_GROUP_AUTHORITY.GROUP_GENERATION_ID.equal(ROOT_GROUP_GENERATION_ID))
              }
              null == groupGenerationId -> {
                statement.and(ACCOUNT_GROUP_AUTHORITY.GROUP_GENERATION_ID.equal(GroupRepository.selectCurrentGroupGenerationId()))
              }
              else -> {
                statement.and(ACCOUNT_GROUP_AUTHORITY.GROUP_GENERATION_ID.equal(groupGenerationId))
              }
            }
          }

      )
      .execute()
  }
}