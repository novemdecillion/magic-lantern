package io.github.novemdecillion.adapter.db

import io.github.novemdecillion.adapter.id.IdGeneratorService
import io.github.novemdecillion.adapter.jooq.tables.pojos.LessonEntity
import io.github.novemdecillion.adapter.jooq.tables.records.LessonRecord
import io.github.novemdecillion.adapter.jooq.tables.references.CURRENT_GROUP_TRANSITION
import io.github.novemdecillion.adapter.jooq.tables.references.LESSON
import io.github.novemdecillion.domain.Group
import io.github.novemdecillion.domain.Lesson
import io.github.novemdecillion.domain.LessonWithGroup
import org.jetbrains.annotations.NotNull
import org.jooq.DSLContext
import org.jooq.Record3
import org.jooq.SelectOrderByStep
import org.jooq.SelectSelectStep
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class LessonRepository(private val dslContext: DSLContext,
                       private val idGeneratorService: IdGeneratorService
) {

  fun insert(slideId: String, groupTransitionIds: Collection<UUID>) {
    val tempLesson = LESSON.`as`("temp")

    val groupIds = groupTransitionIds.toMutableList()

    var selectStep: SelectOrderByStep<Record3<UUID, String, UUID>> = DSL.select(
      DSL.value(idGeneratorService.generate()).`as`(tempLesson.LESSON_ID.name),
      DSL.value(slideId).`as`(tempLesson.SLIDE_ID.name),
      DSL.value(groupIds.removeAt(0)).`as`(tempLesson.GROUP_ORIGIN_ID.name)
    )

    groupIds
      .forEach { groupTransitionId ->
        val currentSelectStep = DSL.select(
          DSL.value(idGeneratorService.generate()).`as`(LESSON.LESSON_ID.name),
          DSL.value(slideId).`as`(LESSON.SLIDE_ID.name),
          DSL.value(groupTransitionId).`as`(LESSON.GROUP_ORIGIN_ID.name)
        )
        selectStep = selectStep.unionAll(currentSelectStep)
      }

    dslContext.with(tempLesson.name)
      .`as`(selectStep)
      .insertInto(LESSON)
      .select(
        DSL.select(tempLesson.LESSON_ID, tempLesson.SLIDE_ID, CURRENT_GROUP_TRANSITION.GROUP_ORIGIN_ID)
          .from(tempLesson)
          .leftJoin(CURRENT_GROUP_TRANSITION)
          .on(CURRENT_GROUP_TRANSITION.GROUP_TRANSITION_ID.equal(tempLesson.GROUP_ORIGIN_ID))
      )
      .execute()
  }

//  fun selectAll(): List<Lesson> {
//    return dslContext.selectFrom(LESSON)
//      .fetch { record ->
//        Lesson(record.lessonId!!, record.slideId!!, /*TODO*/ record.groupOriginId!!)
//      }
//  }

  fun selectByGroupTransitionIds(groupTransitionIds: Collection<UUID>): List<LessonWithGroup> {
    return dslContext.select(LESSON.asterisk(), CURRENT_GROUP_TRANSITION.asterisk())
      .from(LESSON)
        .leftJoin(CURRENT_GROUP_TRANSITION)
          .on(CURRENT_GROUP_TRANSITION.GROUP_ORIGIN_ID.equal(LESSON.GROUP_ORIGIN_ID)
            .and(CURRENT_GROUP_TRANSITION.GROUP_TRANSITION_ID.`in`(groupTransitionIds)))
      .fetch { record ->
        val group = GroupRepository.recordMapper(record.into(CURRENT_GROUP_TRANSITION))
        LessonWithGroup(
          Lesson(record.getValue(LESSON.LESSON_ID)!!, record.getValue(LESSON.SLIDE_ID)!!, group.group.groupId),
          group)
      }
  }


}