package io.github.novemdecillion.adapter.db

import io.github.novemdecillion.adapter.jooq.tables.records.LessonRecord
import io.github.novemdecillion.adapter.jooq.tables.references.GROUP_TRANSITION_WITH_PATH
import io.github.novemdecillion.adapter.jooq.tables.references.LESSON
import io.github.novemdecillion.domain.*
import org.jooq.*
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class LessonRepository(private val dslContext: DSLContext) {

  fun insert(lessons: Collection<Lesson>) {
    dslContext.batchInsert(
      lessons
        .map { LessonRecord(lessonId = it.lessonId, groupTransitionId = it.groupId, slideId = it.slideId)  }
    ).execute()
  }

  fun delete(lessonId: UUID): Int {
    return dslContext.deleteFrom(LESSON).where(LESSON.LESSON_ID.equal(lessonId)).execute()
  }

  fun deleteBySlideId(slideId: String): Int {
    return dslContext.deleteFrom(LESSON).where(LESSON.SLIDE_ID.equal(slideId)).execute()
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

  fun selectByGroupTransitionIds(groupTransitionIds: Collection<UUID>): List<LessonWithGroup> {
    return selectLesson {
        it.where(GROUP_TRANSITION_WITH_PATH.GROUP_TRANSITION_ID.`in`(groupTransitionIds))
      }
  }

  private fun selectLesson(statementModifier: (SelectWhereStep<Record>) -> ResultQuery<Record> = { it }): List<LessonWithGroup> {
    val statement = dslContext.select(LESSON.asterisk(), GROUP_TRANSITION_WITH_PATH.asterisk())
      .from(LESSON)
      .innerJoin(GROUP_TRANSITION_WITH_PATH)
      .on(GROUP_TRANSITION_WITH_PATH.GROUP_TRANSITION_ID.equal(LESSON.GROUP_TRANSITION_ID))

    return statementModifier(statement)
      .fetch { record ->
        val groupWithPath = GroupRepository.recordMapper(record.into(GROUP_TRANSITION_WITH_PATH))
        LessonWithGroup(
          Lesson(record.getValue(LESSON.LESSON_ID)!!, record.getValue(LESSON.SLIDE_ID)!!, groupWithPath.groupId),
          groupWithPath
        )
      }
  }
}