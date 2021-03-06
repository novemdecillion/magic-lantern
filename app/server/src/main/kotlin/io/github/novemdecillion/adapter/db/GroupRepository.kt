package io.github.novemdecillion.adapter.db

import io.github.novemdecillion.adapter.jooq.tables.pojos.GroupGenerationEntity
import io.github.novemdecillion.adapter.jooq.tables.records.GroupTransitionRecord
import io.github.novemdecillion.adapter.jooq.tables.records.GroupTransitionWithPathRecord
import io.github.novemdecillion.adapter.jooq.tables.references.*
import io.github.novemdecillion.domain.*
import org.jooq.DSLContext
import org.jooq.Record1
import org.jooq.SelectConditionStep
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class GroupRepository(
  private val dslContext: DSLContext,
  private val materializedViewRepository: MaterializedViewRepository
) {
  fun selectCurrentGroupGenerationId(): Int? {
    return dslContext.fetchOne(GroupRepository.selectCurrentGroupGenerationId())?.value1()
  }

  fun createNewGeneration(): Int {
    val newGenerationId = dslContext.insertInto(GROUP_GENERATION)
      .set(GROUP_GENERATION.IS_CURRENT, false)
      .returningResult(GROUP_GENERATION.GROUP_GENERATION_ID)
      .fetchOne()
      ?.value1()
      ?: throw IllegalStateException("新グループ世代の作成に失敗しました。")

    dslContext.execute("create table group_${newGenerationId} partition of group_transition for values in (${newGenerationId})")
    dslContext.execute("create table account_group_authority_${newGenerationId} partition of account_group_authority for values in (${newGenerationId})")
    return newGenerationId
  }

  fun selectCurrentAndAvailableGeneration(): List<GroupGenerationEntity> {
    return dslContext
      .select(GROUP_GENERATION.asterisk())
      .from(GROUP_GENERATION.name)
      .where(GROUP_GENERATION.GROUP_GENERATION_ID.greaterOrEqual(GroupRepository.selectCurrentGroupGenerationId()))
      .orderBy(GROUP_GENERATION.GROUP_GENERATION_ID)
      .fetchInto(GroupGenerationEntity::class.java)
      .also {
        check(it.size in (1..2)) { "現行グループ世代だけか、現行グループ世代を含む2件だけのはずが、${it}が存在しました。" }
      }
  }

  fun updateCurrentGroupGeneration(currentGroupGenerationId: Int, nextGroupGenerationId: Int) {
    dslContext
      .update(GROUP_GENERATION)
      .set(GROUP_GENERATION.IS_CURRENT, DSL.not(GROUP_GENERATION.IS_CURRENT))
      .where(
        GROUP_GENERATION.GROUP_GENERATION_ID.equal(currentGroupGenerationId).and(GROUP_GENERATION.IS_CURRENT.isTrue)
          .or(
            GROUP_GENERATION.GROUP_GENERATION_ID.equal(nextGroupGenerationId).and(GROUP_GENERATION.IS_CURRENT.isFalse)
          )
      )
      .execute()
      .also {
        check(it == 2) { "世代切替の更新件数が2件ではなく、${it}件でした。" }
      }
    materializedViewRepository.refreshCurrentGroupTransitionTable()
  }

  fun insertGroup(group: IGroup): Int {
    val result = dslContext.insertInto(GROUP_TRANSITION)
      .set(GROUP_TRANSITION.GROUP_TRANSITION_ID, group.groupId)
      .set(GROUP_TRANSITION.GROUP_GENERATION_ID, group.groupGenerationId)
      .set(GROUP_TRANSITION.GROUP_NAME, group.groupName)
      .set(GROUP_TRANSITION.PARENT_GROUP_TRANSITION_ID, group.parentGroupId)
      .execute()

    materializedViewRepository.refreshCurrentGroupTransitionTable()
    return result
  }

  fun updateGroup(groupTransitionId: UUID, groupName: String, groupGenerationId: Int) {
    dslContext.update(GROUP_TRANSITION)
      .set(GROUP_TRANSITION.GROUP_NAME, groupName)
      .where(GROUP_TRANSITION.GROUP_TRANSITION_ID.equal(groupTransitionId)
        .and(GROUP_TRANSITION.GROUP_GENERATION_ID.equal(groupGenerationId)))
      .execute()

    materializedViewRepository.refreshCurrentGroupTransitionTable()
  }

  fun deleteGroup(groupTransitionId: UUID, groupGenerationId: Int) {
    require(groupTransitionId != ROOT_GROUP_ID) { "この${groupTransitionId} グループは削除できません。" }

    val targetGroup = GROUP_TRANSITION_WITH_PATH.`as`("target")
    val groups = dslContext
      .with(targetGroup.name)
      .`as`(DSL.select(GROUP_TRANSITION_WITH_PATH.GROUP_GENERATION_ID, DSL.concat(GROUP_TRANSITION_WITH_PATH.PATH, "%").`as`(targetGroup.PATH.name))
        .from(GROUP_TRANSITION_WITH_PATH)
        .where(GROUP_TRANSITION_WITH_PATH.GROUP_TRANSITION_ID.equal(groupTransitionId)
              .and(GROUP_TRANSITION_WITH_PATH.GROUP_GENERATION_ID.equal(groupGenerationId))))
      .select(GROUP_TRANSITION_WITH_PATH.GROUP_TRANSITION_ID, GROUP_TRANSITION_WITH_PATH.GROUP_GENERATION_ID)
        .from(GROUP_TRANSITION_WITH_PATH)
        .join(targetGroup.name)
          .on(GROUP_TRANSITION_WITH_PATH.GROUP_GENERATION_ID.equal(targetGroup.GROUP_GENERATION_ID)
          .and(GROUP_TRANSITION_WITH_PATH.PATH.like(targetGroup.PATH)))
      .fetch { record ->
        record.value1()!! to record.value2()!!
      }

    dslContext.deleteFrom(GROUP_TRANSITION)
      .where(
        GROUP_TRANSITION.GROUP_TRANSITION_ID.`in`(groups.map { it.first })
          .and(GROUP_TRANSITION.GROUP_GENERATION_ID.equal(groups.first().second))
      )
      .execute()

    dslContext.deleteFrom(ACCOUNT_GROUP_AUTHORITY)
      .where(
        ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID.`in`(groups.map { it.first })
          .and(ACCOUNT_GROUP_AUTHORITY.GROUP_GENERATION_ID.equal(groups.first().second))
      )
      .execute()

    materializedViewRepository.refreshCurrentGroupTransitionTable()
  }

  fun deleteAndInsertGroups(groupGenerationId: Int, groups: Collection<IGroup>) {
    dslContext.deleteFrom(GROUP_TRANSITION)
      .where(GROUP_TRANSITION.GROUP_GENERATION_ID.equal(groupGenerationId)).execute()
    dslContext.deleteFrom(ACCOUNT_GROUP_AUTHORITY)
      .where(ACCOUNT_GROUP_AUTHORITY.GROUP_GENERATION_ID.equal(groupGenerationId)).execute()

    groups
      .map { group ->
        GroupTransitionRecord()
          .also { record ->
            record.groupTransitionId = group.groupId
            record.groupGenerationId = group.groupGenerationId
            record.groupName = group.groupName
            group.parentGroupId?.also { record.parentGroupTransitionId = it }
          }
      }
      .also {
        dslContext.batchInsert(it).execute()
      }
    materializedViewRepository.refreshCurrentGroupTransitionTable()
  }

  fun select(groupGenerationId: Int? = null): List<GroupWithPath> {
    return dslContext.selectFrom(GROUP_TRANSITION_WITH_PATH)
      .where(
        if (null == groupGenerationId) {
          GROUP_TRANSITION_WITH_PATH.GROUP_GENERATION_ID.equal(GroupRepository.selectCurrentGroupGenerationId())
            .or(GROUP_TRANSITION_WITH_PATH.GROUP_GENERATION_ID.equal(ROOT_GROUP_GENERATION_ID))
        } else {
          GROUP_TRANSITION_WITH_PATH.GROUP_GENERATION_ID.equal(groupGenerationId)
            .or(GROUP_TRANSITION_WITH_PATH.GROUP_GENERATION_ID.equal(ROOT_GROUP_GENERATION_ID))
        }
      )
      .fetch { record -> recordMapper(record) }
  }

  fun selectById(groupTransitionId: UUID, groupGenerationId: Int? = null): GroupWithPath? {
    return dslContext.selectFrom(GROUP_TRANSITION_WITH_PATH)
      .where(GROUP_TRANSITION_WITH_PATH.GROUP_TRANSITION_ID.equal(groupTransitionId)
        .let { statement ->
          when {
            groupTransitionId == ROOT_GROUP_ID ->
              statement.and(GROUP_TRANSITION_WITH_PATH.GROUP_GENERATION_ID.equal(ROOT_GROUP_GENERATION_ID))
            null == groupGenerationId ->
              statement.and(GROUP_TRANSITION_WITH_PATH.GROUP_GENERATION_ID.equal(GroupRepository.selectCurrentGroupGenerationId()))
            else ->
              statement.and(GROUP_TRANSITION_WITH_PATH.GROUP_GENERATION_ID.equal(groupGenerationId))
          }
        })
      .fetchOne()
      ?.let { record -> recordMapper(record) }
  }

  fun selectByIds(groupTransitionIds: Collection<UUID>, groupGenerationId: Int? = null): List<GroupWithPath> {
    return dslContext.selectFrom(GROUP_TRANSITION_WITH_PATH)
      .where(GROUP_TRANSITION_WITH_PATH.GROUP_TRANSITION_ID.`in`(groupTransitionIds)
        .let { statement ->
          if (null == groupGenerationId) {
            statement.and(
              GROUP_TRANSITION_WITH_PATH.GROUP_GENERATION_ID.equal(GroupRepository.selectCurrentGroupGenerationId())
                .or(GROUP_TRANSITION_WITH_PATH.GROUP_GENERATION_ID.equal(ROOT_GROUP_GENERATION_ID))
            )
          } else {
            statement.and(
              GROUP_TRANSITION_WITH_PATH.GROUP_GENERATION_ID.equal(groupGenerationId)
                .or(GROUP_TRANSITION_WITH_PATH.GROUP_GENERATION_ID.equal(ROOT_GROUP_GENERATION_ID))
            )
          }
        }
      )
      .fetch()
      .map { record -> recordMapper(record) }
  }

  fun selectChildrenByIds(groupTransitionIds: Collection<UUID>, groupGenerationId: Int? = null): List<GroupWithPath> {
    val likeValues: List<String> = groupTransitionIds.map { "%$it%" }
    return dslContext.selectFrom(GROUP_TRANSITION_WITH_PATH)
      .where(GROUP_TRANSITION_WITH_PATH.PATH.like(DSL.any(*likeValues.toTypedArray()))
        .let { statement ->
          if (null == groupGenerationId) {
            statement.and(
              GROUP_TRANSITION_WITH_PATH.GROUP_GENERATION_ID.equal(GroupRepository.selectCurrentGroupGenerationId())
                .or(GROUP_TRANSITION_WITH_PATH.GROUP_GENERATION_ID.equal(ROOT_GROUP_GENERATION_ID))
            )
          } else {
            statement.and(GROUP_TRANSITION_WITH_PATH.GROUP_GENERATION_ID.equal(groupGenerationId)
              .or(GROUP_TRANSITION_WITH_PATH.GROUP_GENERATION_ID.equal(ROOT_GROUP_GENERATION_ID)))
          }
        }
      )
      .fetch()
      .map { record -> recordMapper(record) }
  }

  fun recordMapper(group: IGroup): GroupTransitionRecord {
    return GroupTransitionRecord()
      .also { record ->
        record.groupTransitionId = group.groupId
        record.groupGenerationId = group.groupGenerationId
        record.groupName = group.groupName
        group.parentGroupId?.also { record.parentGroupTransitionId = it }
      }
  }

  companion object {
    fun recordMapper(record: GroupTransitionWithPathRecord): GroupWithPath {
      return GroupWithPath(
        Group(
          record.groupTransitionId!!,
          record.groupGenerationId!!,
          record.groupName!!,
          record.parentGroupTransitionId,
        ),
        convertPath(record.path!!, record.pathName!!)
      )
    }

    fun recordMapper(record: GroupTransitionRecord): Group {
      return Group(
        record.groupTransitionId!!,
        record.groupGenerationId!!,
        record.groupName!!,
        record.parentGroupTransitionId
      )
    }

    fun convertPath(pathIds: String, pathNames: String): List<GroupCore> {
      val groupIds = pathIds.split(GROUP_PATH_SEPARATOR).filter { it.isNotEmpty() }.map { UUID.fromString(it) }
      val groupNames = pathNames.split(GROUP_PATH_SEPARATOR).filter { it.isNotEmpty() }
      require(groupIds.size == groupNames.size)
      return groupIds.indices
        .filter { it != groupIds.lastIndex }
        .map {
          GroupCore(groupIds[it], groupNames[it])
        }
    }

    fun selectEffectiveGroupGenerationIds(): SelectConditionStep<Record1<Int?>> {
      return DSL.select(GROUP_GENERATION.GROUP_GENERATION_ID).from(GROUP_GENERATION)
        .where(GROUP_GENERATION.IS_CURRENT.isTrue)
    }

    fun selectCurrentGroupGenerationId(): SelectConditionStep<Record1<Int?>> {
      return DSL.select(GROUP_GENERATION.GROUP_GENERATION_ID).from(GROUP_GENERATION)
        .where(
          GROUP_GENERATION.GROUP_GENERATION_ID.notEqual(ROOT_GROUP_GENERATION_ID)
            .and(GROUP_GENERATION.IS_CURRENT.isTrue)
        )
    }
  }
}