/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.adapter.jooq.tables.daos


import io.github.novemdecillion.adapter.jooq.tables.StudyTable
import io.github.novemdecillion.adapter.jooq.tables.pojos.StudyEntity
import io.github.novemdecillion.adapter.jooq.tables.records.StudyRecord

import java.time.OffsetDateTime
import java.util.UUID

import kotlin.collections.List

import org.jooq.Configuration
import org.jooq.impl.DAOImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class StudyDao(configuration: Configuration?) : DAOImpl<StudyRecord, StudyEntity, UUID>(StudyTable.STUDY, StudyEntity::class.java, configuration) {

    /**
     * Create a new StudyDao without any configuration
     */
    constructor(): this(null)

    override fun getId(o: StudyEntity): UUID? = o.studyId

    /**
     * Fetch records that have <code>study_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    fun fetchRangeOfStudyIdTable(lowerInclusive: UUID?, upperInclusive: UUID?): List<StudyEntity> = fetchRange(StudyTable.STUDY.STUDY_ID, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>study_id IN (values)</code>
     */
    fun fetchByStudyIdTable(vararg values: UUID): List<StudyEntity> = fetch(StudyTable.STUDY.STUDY_ID, *values)

    /**
     * Fetch a unique record that has <code>study_id = value</code>
     */
    fun fetchOneByStudyIdTable(value: UUID): StudyEntity? = fetchOne(StudyTable.STUDY.STUDY_ID, value)

    /**
     * Fetch records that have <code>account_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    fun fetchRangeOfAccountIdTable(lowerInclusive: UUID?, upperInclusive: UUID?): List<StudyEntity> = fetchRange(StudyTable.STUDY.ACCOUNT_ID, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>account_id IN (values)</code>
     */
    fun fetchByAccountIdTable(vararg values: UUID): List<StudyEntity> = fetch(StudyTable.STUDY.ACCOUNT_ID, *values)

    /**
     * Fetch records that have <code>slide_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    fun fetchRangeOfSlideIdTable(lowerInclusive: String?, upperInclusive: String?): List<StudyEntity> = fetchRange(StudyTable.STUDY.SLIDE_ID, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>slide_id IN (values)</code>
     */
    fun fetchBySlideIdTable(vararg values: String): List<StudyEntity> = fetch(StudyTable.STUDY.SLIDE_ID, *values)

    /**
     * Fetch records that have <code>progress BETWEEN lowerInclusive AND upperInclusive</code>
     */
    fun fetchRangeOfProgressTable(lowerInclusive: String?, upperInclusive: String?): List<StudyEntity> = fetchRange(StudyTable.STUDY.PROGRESS, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>progress IN (values)</code>
     */
    fun fetchByProgressTable(vararg values: String): List<StudyEntity> = fetch(StudyTable.STUDY.PROGRESS, *values)

    /**
     * Fetch records that have <code>progress_rate BETWEEN lowerInclusive AND upperInclusive</code>
     */
    fun fetchRangeOfProgressRateTable(lowerInclusive: Int?, upperInclusive: Int?): List<StudyEntity> = fetchRange(StudyTable.STUDY.PROGRESS_RATE, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>progress_rate IN (values)</code>
     */
    fun fetchByProgressRateTable(vararg values: Int): List<StudyEntity> = fetch(StudyTable.STUDY.PROGRESS_RATE, *values.toTypedArray())

    /**
     * Fetch records that have <code>answer BETWEEN lowerInclusive AND upperInclusive</code>
     */
    fun fetchRangeOfAnswerTable(lowerInclusive: String?, upperInclusive: String?): List<StudyEntity> = fetchRange(StudyTable.STUDY.ANSWER, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>answer IN (values)</code>
     */
    fun fetchByAnswerTable(vararg values: String): List<StudyEntity> = fetch(StudyTable.STUDY.ANSWER, *values)

    /**
     * Fetch records that have <code>score BETWEEN lowerInclusive AND upperInclusive</code>
     */
    fun fetchRangeOfScoreTable(lowerInclusive: Int?, upperInclusive: Int?): List<StudyEntity> = fetchRange(StudyTable.STUDY.SCORE, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>score IN (values)</code>
     */
    fun fetchByScoreTable(vararg values: Int): List<StudyEntity> = fetch(StudyTable.STUDY.SCORE, *values.toTypedArray())

    /**
     * Fetch records that have <code>start_at BETWEEN lowerInclusive AND upperInclusive</code>
     */
    fun fetchRangeOfStartAtTable(lowerInclusive: OffsetDateTime?, upperInclusive: OffsetDateTime?): List<StudyEntity> = fetchRange(StudyTable.STUDY.START_AT, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>start_at IN (values)</code>
     */
    fun fetchByStartAtTable(vararg values: OffsetDateTime): List<StudyEntity> = fetch(StudyTable.STUDY.START_AT, *values)

    /**
     * Fetch records that have <code>end_at BETWEEN lowerInclusive AND upperInclusive</code>
     */
    fun fetchRangeOfEndAtTable(lowerInclusive: OffsetDateTime?, upperInclusive: OffsetDateTime?): List<StudyEntity> = fetchRange(StudyTable.STUDY.END_AT, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>end_at IN (values)</code>
     */
    fun fetchByEndAtTable(vararg values: OffsetDateTime): List<StudyEntity> = fetch(StudyTable.STUDY.END_AT, *values)
}
