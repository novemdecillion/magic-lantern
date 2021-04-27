package io.github.novemdecillion.adapter.db

import io.github.novemdecillion.adapter.id.IdGeneratorService
import io.github.novemdecillion.adapter.jooq.tables.records.CurrentGroupTransitionRecord
import io.github.novemdecillion.adapter.jooq.tables.records.GroupOriginRecord
import io.github.novemdecillion.adapter.jooq.tables.records.GroupTransitionRecord
import io.github.novemdecillion.adapter.jooq.tables.references.CURRENT_GROUP_TRANSITION
import io.github.novemdecillion.adapter.jooq.tables.references.GROUP_GENERATION_PERIOD
import io.github.novemdecillion.adapter.jooq.tables.references.GROUP_ORIGIN
import io.github.novemdecillion.adapter.jooq.tables.references.GROUP_TRANSITION
import io.github.novemdecillion.domain.ENTIRE_GROUP_GENERATION_ID
import io.github.novemdecillion.domain.ENTIRE_GROUP_ID
import io.github.novemdecillion.domain.Group
import org.jooq.DSLContext
import org.jooq.Record1
import org.jooq.SelectConditionStep
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.util.*

@Repository
class GroupRepository(private val dslContext: DSLContext,
                      private val materializedViewRepository: MaterializedViewRepository,
                      private val idGeneratorService: IdGeneratorService) {
  fun insertNewGroup(
    now: OffsetDateTime,
    groupName: String,
    parentGroupTransitionId: UUID = ENTIRE_GROUP_ID,
    groupGenerationId: UUID? = null
  ): Group {
    val originId = idGeneratorService.generate()
    dslContext.insertInto(GROUP_ORIGIN)
      .set(GroupOriginRecord(originId))
      .execute()

    val groupRecord = dslContext.insertInto(GROUP_TRANSITION)
      .set(GROUP_TRANSITION.GROUP_TRANSITION_ID, originId)
      .let { insertStatement ->
        groupGenerationId
          ?.let {
            insertStatement
              .set(GROUP_TRANSITION.GROUP_GENERATION_ID, groupGenerationId)
          }
          ?: run {
            insertStatement
              .set(GROUP_TRANSITION.GROUP_GENERATION_ID, selectCurrentGroupGenerationId(now))
          }
      }
      .set(GROUP_TRANSITION.GROUP_ORIGIN_ID, originId)
      .set(GROUP_TRANSITION.GROUP_NAME, groupName)
      .set(GROUP_TRANSITION.PARENT_GROUP_TRANSITION_ID, parentGroupTransitionId)
      .returning().fetchOne()!!

    materializedViewRepository.refreshCurrentGroupTransitionTable()
    return recordMapper(groupRecord)
  }

  fun updateGroup(groupTransitionId: UUID, groupName: String) {
    dslContext.update(GROUP_TRANSITION)
      .set(GroupTransitionRecord()
        .also {
          it.groupTransitionId = groupTransitionId
          it.groupName = groupName
        })
      .execute()

    materializedViewRepository.refreshCurrentGroupTransitionTable()
  }

  fun deleteGroup(groupTransitionId: UUID) {
    // TODO 子グループや権限の削除
    dslContext.deleteFrom(GROUP_TRANSITION)
      .where(GROUP_TRANSITION.GROUP_TRANSITION_ID.equal(groupTransitionId))
      .execute()
    materializedViewRepository.refreshCurrentGroupTransitionTable()
  }

  fun selectAll(): List<Group> {
    return dslContext.selectFrom(CURRENT_GROUP_TRANSITION)
      .fetch(::recordMapper)
  }

  fun recordMapper(group: Group): GroupTransitionRecord {
    return GroupTransitionRecord(
      groupTransitionId = group.groupId,
      groupGenerationId = group.groupGenerationId,
      groupOriginId = group.groupOriginId,
      groupName = group.groupName,
      parentGroupTransitionId = group.groupGenerationId
    )
  }

  fun recordMapper(record: CurrentGroupTransitionRecord): Group {
    return Group(
      record.groupTransitionId!!,
      record.groupOriginId!!,
      record.groupGenerationId!!,
      record.groupName!!,
      record.parentGroupTransitionId
    )
  }

  fun recordMapper(record: GroupTransitionRecord): Group {
    return Group(
      record.groupTransitionId!!,
      record.groupOriginId!!,
      record.groupGenerationId!!,
      record.groupName!!,
      record.parentGroupTransitionId
    )
  }

  fun selectCurrentGroupGenerationId(now: OffsetDateTime): SelectConditionStep<Record1<UUID?>> {
    return DSL.select(GROUP_GENERATION_PERIOD.GROUP_GENERATION_ID).from(GROUP_GENERATION_PERIOD)
      .where(
        GROUP_GENERATION_PERIOD.GROUP_GENERATION_ID.notEqual(ENTIRE_GROUP_GENERATION_ID)
          .and(GROUP_GENERATION_PERIOD.START_DATE.lessOrEqual(now).or(GROUP_GENERATION_PERIOD.START_DATE.isNull))
          .and(GROUP_GENERATION_PERIOD.END_DATE.greaterThan(now).or(GROUP_GENERATION_PERIOD.END_DATE.isNull)))
  }
}