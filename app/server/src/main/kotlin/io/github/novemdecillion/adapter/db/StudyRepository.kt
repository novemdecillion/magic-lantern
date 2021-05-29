package io.github.novemdecillion.adapter.db

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.novemdecillion.adapter.jooq.tables.records.StudyRecord
import io.github.novemdecillion.adapter.jooq.tables.references.ACCOUNT_GROUP_AUTHORITY
import io.github.novemdecillion.adapter.jooq.tables.references.CURRENT_ACCOUNT_GROUP_AUTHORITY
import io.github.novemdecillion.adapter.jooq.tables.references.LESSON
import io.github.novemdecillion.adapter.jooq.tables.references.STUDY
import io.github.novemdecillion.domain.ROOT_GROUP_GENERATION_ID
import io.github.novemdecillion.domain.ROOT_GROUP_ID
import io.github.novemdecillion.domain.Role
import io.github.novemdecillion.domain.Study
import org.jooq.DSLContext
import org.jooq.JSONB
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class StudyRepository(
  private val dslContext: DSLContext,
  private val objectMapper: ObjectMapper
) {
  fun recordMapper(study: StudyRecord): Study {
    return Study(
      studyId = study.studyId!!,
      userId = study.accountId!!,
      slideId = study.slideId!!,
      status = study.status!!,

      progress = study.progress?.let { objectMapper.readValue(it.data()) } ?: mapOf(),
      progressRate = study.progressRate!!,

      answer = study.answer?.let { objectMapper.readValue(it.data()) } ?: mapOf(),
      score = study.score?.let { objectMapper.readValue(it.data()) } ?: mapOf(),

      startAt = study.startAt,
      endAt = study.endAt
    )
  }

  fun recordMapper(study: Study): StudyRecord {
    val record = StudyRecord()
    record.studyId = study.studyId
    record.accountId = study.userId
    record.slideId = study.slideId
    record.progress = JSONB.jsonb(objectMapper.writeValueAsString(study.progress))
    record.progressRate = study.progressRate
    record.answer = JSONB.jsonb(objectMapper.writeValueAsString(study.answer))
    record.score = JSONB.jsonb(objectMapper.writeValueAsString(study.score))
    record.status = study.status
    study.startAt?.also { record.startAt = it }
    study.endAt?.also { record.endAt = it }
    return record
  }

  fun insert(study: Study) {
    dslContext.executeInsert(recordMapper(study))
  }

  fun update(study: Study) {
    dslContext.executeUpdate(recordMapper(study))
  }

  fun delete(studyId: UUID): Int {
    return dslContext.deleteFrom(STUDY).where(STUDY.STUDY_ID.equal(studyId)).execute()
  }

  fun selectById(studyId: UUID, userId: UUID): Study? {
    return dslContext.selectFrom(STUDY)
      .where(STUDY.STUDY_ID.equal(studyId).and(STUDY.ACCOUNT_ID.equal(userId)))
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
}