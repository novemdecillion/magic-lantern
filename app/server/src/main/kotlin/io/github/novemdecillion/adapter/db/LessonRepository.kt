package io.github.novemdecillion.adapter.db

import io.github.novemdecillion.adapter.id.IdGeneratorService
import io.github.novemdecillion.adapter.jooq.tables.records.LessonRecord
import io.github.novemdecillion.adapter.jooq.tables.references.CURRENT_ACCOUNT_GROUP_AUTHORITY
import io.github.novemdecillion.adapter.jooq.tables.references.CURRENT_GROUP_TRANSITION
import io.github.novemdecillion.adapter.jooq.tables.references.LESSON
import io.github.novemdecillion.adapter.jooq.tables.references.STUDY
import io.github.novemdecillion.domain.GroupWithPath
import io.github.novemdecillion.domain.Lesson
import io.github.novemdecillion.domain.LessonWithGroup
import io.github.novemdecillion.domain.User
import org.jetbrains.annotations.NotNull
import org.jooq.*
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class LessonRepository(private val dslContext: DSLContext,
                       private val idGeneratorService: IdGeneratorService) {

  fun insert(slideId: String, groupTransitionIds: Collection<UUID>) {
//    val tempLesson = LESSON.`as`("temp")
//
//    val groupIds = groupTransitionIds.toMutableList()
//
//    var selectStep: SelectOrderByStep<Record3<UUID, String, UUID>> = DSL.select(
//      DSL.value(idGeneratorService.generate()).`as`(tempLesson.LESSON_ID.name),
//      DSL.value(slideId).`as`(tempLesson.SLIDE_ID.name),
//      DSL.value(groupIds.removeAt(0)).`as`(tempLesson.GROUP_ORIGIN_ID.name)
//    )
//
//    groupIds
//      .forEach { groupTransitionId ->
//        val currentSelectStep = DSL.select(
//          DSL.value(idGeneratorService.generate()).`as`(LESSON.LESSON_ID.name),
//          DSL.value(slideId).`as`(LESSON.SLIDE_ID.name),
//          DSL.value(groupTransitionId).`as`(LESSON.GROUP_ORIGIN_ID.name)
//        )
//        selectStep = selectStep.unionAll(currentSelectStep)
//      }
//
//    dslContext.with(tempLesson.name)
//      .`as`(selectStep)
//      .insertInto(LESSON, LESSON.LESSON_ID, LESSON.SLIDE_ID, LESSON.GROUP_ORIGIN_ID)
//      .select(
//        DSL.select(tempLesson.LESSON_ID, tempLesson.SLIDE_ID, CURRENT_GROUP_TRANSITION.GROUP_ORIGIN_ID)
//          .from(tempLesson.name)
//          .innerJoin(CURRENT_GROUP_TRANSITION)
//          .on(CURRENT_GROUP_TRANSITION.GROUP_TRANSITION_ID.equal(tempLesson.GROUP_ORIGIN_ID))
//      )
//      .execute()

    groupTransitionIds
      .map { LessonRecord(lessonId = idGeneratorService.generate(), groupTransitionId = it, slideId = slideId)  }
      .let {
        dslContext.batchInsert(it).execute()
      }

  }

  fun delete(lessonId: UUID): Int {
    return dslContext.deleteFrom(LESSON).where(LESSON.LESSON_ID.equal(lessonId)).execute()
  }

  fun selectById(lessonId: UUID): LessonWithGroup? {
    return selectLesson {
      it.where(LESSON.LESSON_ID.equal(lessonId))
    }.firstOrNull()
  }

  fun selectByIds(lessonIds: Collection<UUID>): List<LessonWithGroup> {
    return selectLesson {
      it.where(LESSON.LESSON_ID.`in`(lessonIds))
    }
  }

//  fun selectByUserId(userId: UUID): List<Lesson> {
//    return dslContext.selectDistinct(LESSON.LESSON_ID, LESSON.SLIDE_ID, CURRENT_ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID)
//      .from(LESSON)
//      .innerJoin(CURRENT_ACCOUNT_GROUP_AUTHORITY)
//      .on(CURRENT_ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID.equal(LESSON.GROUP_TRANSITION_ID)
//          .and(CURRENT_ACCOUNT_GROUP_AUTHORITY.ACCOUNT_ID.equal(userId)))
//      .fetch { record ->
//          Lesson(record.getValue(LESSON.LESSON_ID)!!,
//            record.getValue(LESSON.SLIDE_ID)!!,
//            record.getValue(CURRENT_ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID)!!)
//      }
//  }

  fun selectByUserIdAndNotExistStudy(userId: UUID): List<Lesson> {
    return dslContext.selectDistinct(LESSON.LESSON_ID, LESSON.SLIDE_ID)
      .from(LESSON)
      .innerJoin(CURRENT_ACCOUNT_GROUP_AUTHORITY)
        .on(CURRENT_ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID.equal(LESSON.GROUP_TRANSITION_ID)
          .and(CURRENT_ACCOUNT_GROUP_AUTHORITY.ACCOUNT_ID.equal(userId)))
      .where(LESSON.SLIDE_ID.notIn(DSL.select(STUDY.SLIDE_ID).from(STUDY).where(STUDY.ACCOUNT_ID.equal(userId))))
      .fetch { record ->
        Lesson(record.getValue(LESSON.LESSON_ID)!!,
          record.getValue(LESSON.SLIDE_ID)!!)
      }
  }


  fun selectByGroupIdsAndSlideIds(groupTransitionIds: Collection<UUID>, slideIds: Collection<String>): List<LessonWithGroup> {
    return selectLesson {
      it.where(CURRENT_GROUP_TRANSITION.GROUP_TRANSITION_ID.`in`(groupTransitionIds)
        .and(LESSON.SLIDE_ID.`in`(slideIds)))
    }
  }

  fun selectByGroupTransitionIds(groupTransitionIds: Collection<UUID>): List<LessonWithGroup> {
    return selectLesson {
        it.where(CURRENT_GROUP_TRANSITION.GROUP_TRANSITION_ID.`in`(groupTransitionIds))
      }
  }

  private fun selectLesson(statementModifier: (SelectWhereStep<Record>) -> ResultQuery<Record> = { it }): List<LessonWithGroup> {
    val statement = dslContext.select(LESSON.asterisk(), CURRENT_GROUP_TRANSITION.asterisk())
      .from(LESSON)
      .innerJoin(CURRENT_GROUP_TRANSITION)
      .on(CURRENT_GROUP_TRANSITION.GROUP_TRANSITION_ID.equal(LESSON.GROUP_TRANSITION_ID))

    return statementModifier(statement)
      .fetch { record ->
        val groupWithPath = GroupRepository.recordMapper(record.into(CURRENT_GROUP_TRANSITION))
        LessonWithGroup(
          Lesson(record.getValue(LESSON.LESSON_ID)!!, record.getValue(LESSON.SLIDE_ID)!!),
          groupWithPath.groupId,
          groupWithPath
        )
      }
  }
}