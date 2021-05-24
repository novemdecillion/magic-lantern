/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.adapter.jooq.tables.records


import io.github.novemdecillion.adapter.jooq.tables.StudyTable
import io.github.novemdecillion.adapter.jooq.tables.interfaces.IStudy
import io.github.novemdecillion.domain.StudyStatus

import java.time.OffsetDateTime
import java.util.UUID

import org.jooq.Field
import org.jooq.JSONB
import org.jooq.Record1
import org.jooq.Record10
import org.jooq.Row10
import org.jooq.impl.UpdatableRecordImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class StudyRecord() : UpdatableRecordImpl<StudyRecord>(StudyTable.STUDY), Record10<UUID?, UUID?, String?, StudyStatus?, JSONB?, Int?, JSONB?, JSONB?, OffsetDateTime?, OffsetDateTime?>, IStudy {

    override var studyId: UUID?
        set(value) = set(0, value)
        get() = get(0) as UUID?

    override var accountId: UUID?
        set(value) = set(1, value)
        get() = get(1) as UUID?

    override var slideId: String?
        set(value) = set(2, value)
        get() = get(2) as String?

    override var status: StudyStatus?
        set(value) = set(3, value)
        get() = get(3) as StudyStatus?

    override var progress: JSONB?
        set(value) = set(4, value)
        get() = get(4) as JSONB?

    override var progressRate: Int?
        set(value) = set(5, value)
        get() = get(5) as Int?

    override var answer: JSONB?
        set(value) = set(6, value)
        get() = get(6) as JSONB?

    override var score: JSONB?
        set(value) = set(7, value)
        get() = get(7) as JSONB?

    override var startAt: OffsetDateTime?
        set(value) = set(8, value)
        get() = get(8) as OffsetDateTime?

    override var endAt: OffsetDateTime?
        set(value) = set(9, value)
        get() = get(9) as OffsetDateTime?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<UUID?> = super.key() as Record1<UUID?>

    // -------------------------------------------------------------------------
    // Record10 type implementation
    // -------------------------------------------------------------------------

    override fun fieldsRow(): Row10<UUID?, UUID?, String?, StudyStatus?, JSONB?, Int?, JSONB?, JSONB?, OffsetDateTime?, OffsetDateTime?> = super.fieldsRow() as Row10<UUID?, UUID?, String?, StudyStatus?, JSONB?, Int?, JSONB?, JSONB?, OffsetDateTime?, OffsetDateTime?>
    override fun valuesRow(): Row10<UUID?, UUID?, String?, StudyStatus?, JSONB?, Int?, JSONB?, JSONB?, OffsetDateTime?, OffsetDateTime?> = super.valuesRow() as Row10<UUID?, UUID?, String?, StudyStatus?, JSONB?, Int?, JSONB?, JSONB?, OffsetDateTime?, OffsetDateTime?>
    override fun field1(): Field<UUID?> = StudyTable.STUDY.STUDY_ID
    override fun field2(): Field<UUID?> = StudyTable.STUDY.ACCOUNT_ID
    override fun field3(): Field<String?> = StudyTable.STUDY.SLIDE_ID
    override fun field4(): Field<StudyStatus?> = StudyTable.STUDY.STATUS
    override fun field5(): Field<JSONB?> = StudyTable.STUDY.PROGRESS
    override fun field6(): Field<Int?> = StudyTable.STUDY.PROGRESS_RATE
    override fun field7(): Field<JSONB?> = StudyTable.STUDY.ANSWER
    override fun field8(): Field<JSONB?> = StudyTable.STUDY.SCORE
    override fun field9(): Field<OffsetDateTime?> = StudyTable.STUDY.START_AT
    override fun field10(): Field<OffsetDateTime?> = StudyTable.STUDY.END_AT
    override fun component1(): UUID? = studyId
    override fun component2(): UUID? = accountId
    override fun component3(): String? = slideId
    override fun component4(): StudyStatus? = status
    override fun component5(): JSONB? = progress
    override fun component6(): Int? = progressRate
    override fun component7(): JSONB? = answer
    override fun component8(): JSONB? = score
    override fun component9(): OffsetDateTime? = startAt
    override fun component10(): OffsetDateTime? = endAt
    override fun value1(): UUID? = studyId
    override fun value2(): UUID? = accountId
    override fun value3(): String? = slideId
    override fun value4(): StudyStatus? = status
    override fun value5(): JSONB? = progress
    override fun value6(): Int? = progressRate
    override fun value7(): JSONB? = answer
    override fun value8(): JSONB? = score
    override fun value9(): OffsetDateTime? = startAt
    override fun value10(): OffsetDateTime? = endAt

    override fun value1(value: UUID?): StudyRecord {
        this.studyId = value
        return this
    }

    override fun value2(value: UUID?): StudyRecord {
        this.accountId = value
        return this
    }

    override fun value3(value: String?): StudyRecord {
        this.slideId = value
        return this
    }

    override fun value4(value: StudyStatus?): StudyRecord {
        this.status = value
        return this
    }

    override fun value5(value: JSONB?): StudyRecord {
        this.progress = value
        return this
    }

    override fun value6(value: Int?): StudyRecord {
        this.progressRate = value
        return this
    }

    override fun value7(value: JSONB?): StudyRecord {
        this.answer = value
        return this
    }

    override fun value8(value: JSONB?): StudyRecord {
        this.score = value
        return this
    }

    override fun value9(value: OffsetDateTime?): StudyRecord {
        this.startAt = value
        return this
    }

    override fun value10(value: OffsetDateTime?): StudyRecord {
        this.endAt = value
        return this
    }

    override fun values(value1: UUID?, value2: UUID?, value3: String?, value4: StudyStatus?, value5: JSONB?, value6: Int?, value7: JSONB?, value8: JSONB?, value9: OffsetDateTime?, value10: OffsetDateTime?): StudyRecord {
        this.value1(value1)
        this.value2(value2)
        this.value3(value3)
        this.value4(value4)
        this.value5(value5)
        this.value6(value6)
        this.value7(value7)
        this.value8(value8)
        this.value9(value9)
        this.value10(value10)
        return this
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    override fun from(from: IStudy) {
        studyId = from.studyId
        accountId = from.accountId
        slideId = from.slideId
        status = from.status
        progress = from.progress
        progressRate = from.progressRate
        answer = from.answer
        score = from.score
        startAt = from.startAt
        endAt = from.endAt
    }

    override fun <E : IStudy> into(into: E): E {
        into.from(this)
        return into
    }

    /**
     * Create a detached, initialised StudyRecord
     */
    constructor(studyId: UUID? = null, accountId: UUID? = null, slideId: String? = null, status: StudyStatus? = null, progress: JSONB? = null, progressRate: Int? = null, answer: JSONB? = null, score: JSONB? = null, startAt: OffsetDateTime? = null, endAt: OffsetDateTime? = null): this() {
        this.studyId = studyId
        this.accountId = accountId
        this.slideId = slideId
        this.status = status
        this.progress = progress
        this.progressRate = progressRate
        this.answer = answer
        this.score = score
        this.startAt = startAt
        this.endAt = endAt
    }
}
