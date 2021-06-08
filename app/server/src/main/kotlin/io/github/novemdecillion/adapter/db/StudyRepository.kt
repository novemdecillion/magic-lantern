package io.github.novemdecillion.adapter.db

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.novemdecillion.adapter.jooq.tables.records.StudyRecord
import io.github.novemdecillion.adapter.jooq.tables.references.CURRENT_ACCOUNT_GROUP_AUTHORITY
import io.github.novemdecillion.adapter.jooq.tables.references.LESSON
import io.github.novemdecillion.adapter.jooq.tables.references.STUDY
import io.github.novemdecillion.domain.*
import io.github.novemdecillion.usecase.IStudyRepository
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class StudyRepository(
  private val dslContext: DSLContext,
  private val objectMapper: ObjectMapper
): IStudyRepository {
  fun recordMapper(study: StudyRecord): Study {
    return Study(
      studyId = study.studyId!!,
      userId = study.accountId!!,
      slideId = study.slideId!!,
      status = study.status!!,

      progress = study.progress?.let { objectMapper.readValue(it.data()) } ?: mapOf(),
      progressRate = study.progressRate ?: 0,
      answer = study.answer?.let { objectMapper.readValue(it.data()) } ?: mapOf(),
      score = study.score?.let { objectMapper.readValue(it.data()) } ?: mapOf(),
      shuffledQuestion = study.shuffledQuestion?.let { objectMapper.readValue(it.data()) } ?: mapOf(),

      startAt = study.startAt,
      endAt = study.endAt
    )
  }

  fun recordMapper(study: Study): StudyRecord {
    val record = StudyRecord()
    record.studyId = study.studyId
    record.accountId = study.userId
    record.slideId = study.slideId
    record.progress = objectMapper.writeValueAsJsonb(study.progress.ifEmpty { null })
    record.progressRate = study.progressRate
    record.answer = objectMapper.writeValueAsJsonb(study.answer.ifEmpty { null })
    record.score = objectMapper.writeValueAsJsonb(study.score.ifEmpty { null })
    record.shuffledQuestion = objectMapper.writeValueAsJsonb(study.shuffledQuestion.ifEmpty { null })
    record.status = study.status
    study.startAt?.also { record.startAt = it }
    study.endAt?.also { record.endAt = it }
    return record
  }

  fun insert(study: Study) {
    dslContext.executeInsert(recordMapper(study))
  }

  override fun update(study: Study) {
    dslContext.executeUpdate(recordMapper(study))
  }

  fun delete(studyId: UUID): Int {
    return dslContext.deleteFrom(STUDY).where(STUDY.STUDY_ID.equal(studyId)).execute()
  }

  fun selectById(studyId: UUID): Study? {
    return dslContext.selectFrom(STUDY)
      .where(STUDY.STUDY_ID.equal(studyId))
      .fetchOne(::recordMapper)
  }

  fun selectByUserId(userId: UUID): List<Study> {
    return dslContext.selectFrom(STUDY)
      .where(STUDY.ACCOUNT_ID.equal(userId))
      .fetch(::recordMapper)
  }

  fun selectByLessonId(lessonId: UUID): List<Study> {
    return dslContext.select(STUDY.asterisk())
      .from(STUDY)
        .innerJoin(LESSON).on(LESSON.LESSON_ID.equal(lessonId).and(LESSON.SLIDE_ID.equal(STUDY.SLIDE_ID)))
        .innerJoin(CURRENT_ACCOUNT_GROUP_AUTHORITY)
          .on(LESSON.GROUP_TRANSITION_ID.equal(CURRENT_ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID)
              .and(DSL.jsonbExists(CURRENT_ACCOUNT_GROUP_AUTHORITY.ROLE,"""$ ? (@ == "${Role.STUDY.name}")"""))
          )
      .fetchInto(StudyRecord::class.java)
      .map {
        recordMapper(it)
      }
  }

  fun selectBySlideIdAndGroupId(slideId: String, groupTransitionId: UUID): List<Study> {
    return dslContext
      .select(STUDY.asterisk())
      .from(STUDY)
        .innerJoin(CURRENT_ACCOUNT_GROUP_AUTHORITY)
          .on(
            STUDY.ACCOUNT_ID.equal(CURRENT_ACCOUNT_GROUP_AUTHORITY.ACCOUNT_ID)
              .and(STUDY.SLIDE_ID.equal(slideId))
              .and(DSL.jsonbExists(CURRENT_ACCOUNT_GROUP_AUTHORITY.ROLE,"""$ ? (@ == "${Role.STUDY.name}")"""))
              .and(CURRENT_ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID.equal(groupTransitionId))
          )
      .fetchInto(StudyRecord::class.java)
      .map {
        recordMapper(it)
      }
  }

  fun selectNotStartStudyByUserId(userId: UUID): List<NotStartStudy> {
    return dslContext.select(LESSON.SLIDE_ID)
      .from(LESSON)
      .innerJoin(CURRENT_ACCOUNT_GROUP_AUTHORITY)
      .on(CURRENT_ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID.equal(LESSON.GROUP_TRANSITION_ID)
        .and(CURRENT_ACCOUNT_GROUP_AUTHORITY.ACCOUNT_ID.equal(userId))
        .and(DSL.jsonbExists(CURRENT_ACCOUNT_GROUP_AUTHORITY.ROLE,"""$ ? (@ == "${Role.STUDY.name}")"""))
      )
      .where(LESSON.SLIDE_ID.notIn(DSL.select(STUDY.SLIDE_ID).from(STUDY).where(STUDY.ACCOUNT_ID.equal(userId))))
      .fetch { record ->
        NotStartStudy(
          userId = userId,
          slideId = record.getValue(LESSON.SLIDE_ID)!!)
      }
  }

  fun selectNotStartStudyByLessonId(lessonId: UUID): List<NotStartStudy> {
    return dslContext.select(LESSON.SLIDE_ID, CURRENT_ACCOUNT_GROUP_AUTHORITY.ACCOUNT_ID)
      .from(LESSON)
      .innerJoin(CURRENT_ACCOUNT_GROUP_AUTHORITY)
        .on(CURRENT_ACCOUNT_GROUP_AUTHORITY.GROUP_TRANSITION_ID.equal(LESSON.GROUP_TRANSITION_ID)
          .and(LESSON.LESSON_ID.equal(lessonId))
          .and(DSL.jsonbExists(CURRENT_ACCOUNT_GROUP_AUTHORITY.ROLE,"""$ ? (@ == "${Role.STUDY.name}")""")))
      .where(LESSON.SLIDE_ID.notIn(DSL.select(STUDY.SLIDE_ID).from(STUDY).where(STUDY.ACCOUNT_ID.equal(CURRENT_ACCOUNT_GROUP_AUTHORITY.ACCOUNT_ID))))
      .fetch { record ->
        NotStartStudy(
          userId = record.getValue(CURRENT_ACCOUNT_GROUP_AUTHORITY.ACCOUNT_ID)!!,
          slideId = record.getValue(LESSON.SLIDE_ID)!!)
      }
  }
}