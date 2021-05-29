package io.github.novemdecillion.adapter.db

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.novemdecillion.adapter.jooq.tables.records.AccountGroupAuthorityRecord
import io.github.novemdecillion.adapter.jooq.tables.references.ACCOUNT_GROUP_AUTHORITY
import io.github.novemdecillion.domain.Authority
import io.github.novemdecillion.domain.ROOT_GROUP_GENERATION_ID
import io.github.novemdecillion.domain.ROOT_GROUP_ID
import io.github.novemdecillion.domain.Role
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

//  fun selectCount(groupTransitionId: UUID, groupGenerationId: Int? = null): Int {
//    return dslContext.selectCount().from(ACCOUNT_GROUP_AUTHORITY)
//      .where(ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID.equal(groupTransitionId)
//        .let { statement ->
//          when {
//            groupTransitionId == ROOT_GROUP_ID -> {
//              statement.and(ACCOUNT_GROUP_AUTHORITY.GROUP_GENERATION_ID.equal(ROOT_GROUP_GENERATION_ID))
//            }
//            null == groupGenerationId -> {
//              statement.and(ACCOUNT_GROUP_AUTHORITY.GROUP_GENERATION_ID.equal(GroupRepository.selectCurrentGroupGenerationId()))
//            }
//            else -> {
//              statement.and(ACCOUNT_GROUP_AUTHORITY.GROUP_GENERATION_ID.equal(groupGenerationId))
//            }
//          }
//        }
//      )
//      .fetchOne()?.value1() ?: 0
//  }

  fun selectCount(groupTransitionIds: Collection<UUID>, groupGenerationId: Int, role: Role? = null): List<Pair<UUID, Int>> {
    return dslContext.select(ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID, DSL.count()).from(ACCOUNT_GROUP_AUTHORITY)
      .where(ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID.`in`(groupTransitionIds)
              .and(ACCOUNT_GROUP_AUTHORITY.GROUP_GENERATION_ID.equal(groupGenerationId))
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
        userIdToAuthority.second.map {authority ->
          AccountGroupAuthorityRecord(userIdToAuthority.first, authority.groupId, authority.groupGenerationInt, rolesToJsonb(authority.roles))

        }
      }).execute()
  }

  fun updateAuthorities(authorities: Collection<Pair<UUID, Collection<Authority>>>) {
    dslContext.batchUpdate(authorities
      .flatMap { userIdToAuthority ->
        userIdToAuthority.second.map {authority ->
          AccountGroupAuthorityRecord(userIdToAuthority.first, authority.groupId, authority.groupGenerationInt, rolesToJsonb(authority.roles))

        }
      }).execute()
  }

  fun updateAuthority(userId: UUID, authority: Authority) {
    dslContext.update(ACCOUNT_GROUP_AUTHORITY)
      .set(ACCOUNT_GROUP_AUTHORITY.ROLE, rolesToJsonb(authority.roles))
      .where(
        ACCOUNT_GROUP_AUTHORITY.ACCOUNT_ID.equal(userId)
          .and(ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID.equal(authority.groupId))
      )
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