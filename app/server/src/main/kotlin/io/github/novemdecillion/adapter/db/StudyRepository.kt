package io.github.novemdecillion.adapter.db

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.novemdecillion.adapter.id.IdGeneratorService
import io.github.novemdecillion.adapter.jooq.tables.records.StudyRecord
import io.github.novemdecillion.adapter.jooq.tables.references.STUDY
import io.github.novemdecillion.domain.Study
import org.jooq.DSLContext
import org.jooq.JSONB
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class StudyRepository(
  private val dslContext: DSLContext,
  private val idGeneratorService: IdGeneratorService,
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
    study.startAt?.also { record.startAt = it }
    study.endAt?.also { record.endAt = it }
    return record
  }

  fun insert(study: Study): Study {
    val newStudy = study.copy(studyId = idGeneratorService.generate())
    dslContext.executeInsert(recordMapper(newStudy))
    return newStudy
  }

  fun update(study: Study) {
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
}