/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.adapter.jooq.tables.records


import io.github.novemdecillion.adapter.jooq.tables.LessonTable
import io.github.novemdecillion.adapter.jooq.tables.interfaces.ILesson

import java.util.UUID

import org.jooq.Field
import org.jooq.Record1
import org.jooq.Record3
import org.jooq.Row3
import org.jooq.impl.UpdatableRecordImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class LessonRecord() : UpdatableRecordImpl<LessonRecord>(LessonTable.LESSON), Record3<UUID?, UUID?, String?>, ILesson {

    override var lessonId: UUID?
        set(value) = set(0, value)
        get() = get(0) as UUID?

    override var groupOriginId: UUID?
        set(value) = set(1, value)
        get() = get(1) as UUID?

    override var slideId: String?
        set(value) = set(2, value)
        get() = get(2) as String?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<UUID?> = super.key() as Record1<UUID?>

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    override fun fieldsRow(): Row3<UUID?, UUID?, String?> = super.fieldsRow() as Row3<UUID?, UUID?, String?>
    override fun valuesRow(): Row3<UUID?, UUID?, String?> = super.valuesRow() as Row3<UUID?, UUID?, String?>
    override fun field1(): Field<UUID?> = LessonTable.LESSON.LESSON_ID
    override fun field2(): Field<UUID?> = LessonTable.LESSON.GROUP_ORIGIN_ID
    override fun field3(): Field<String?> = LessonTable.LESSON.SLIDE_ID
    override fun component1(): UUID? = lessonId
    override fun component2(): UUID? = groupOriginId
    override fun component3(): String? = slideId
    override fun value1(): UUID? = lessonId
    override fun value2(): UUID? = groupOriginId
    override fun value3(): String? = slideId

    override fun value1(value: UUID?): LessonRecord {
        this.lessonId = value
        return this
    }

    override fun value2(value: UUID?): LessonRecord {
        this.groupOriginId = value
        return this
    }

    override fun value3(value: String?): LessonRecord {
        this.slideId = value
        return this
    }

    override fun values(value1: UUID?, value2: UUID?, value3: String?): LessonRecord {
        this.value1(value1)
        this.value2(value2)
        this.value3(value3)
        return this
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    override fun from(from: ILesson) {
        lessonId = from.lessonId
        groupOriginId = from.groupOriginId
        slideId = from.slideId
    }

    override fun <E : ILesson> into(into: E): E {
        into.from(this)
        return into
    }

    /**
     * Create a detached, initialised LessonRecord
     */
    constructor(lessonId: UUID? = null, groupOriginId: UUID? = null, slideId: String? = null): this() {
        this.lessonId = lessonId
        this.groupOriginId = groupOriginId
        this.slideId = slideId
    }
}
