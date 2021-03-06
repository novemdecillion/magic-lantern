/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.adapter.jooq.tables


import io.github.novemdecillion.adapter.jooq.DefaultSchema
import io.github.novemdecillion.adapter.jooq.keys.LESSON_GROUP_TRANSITION_ID_SLIDE_ID_KEY
import io.github.novemdecillion.adapter.jooq.keys.LESSON_PKEY
import io.github.novemdecillion.adapter.jooq.tables.records.LessonRecord

import java.util.UUID

import kotlin.collections.List

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Name
import org.jooq.Record
import org.jooq.Row3
import org.jooq.Schema
import org.jooq.Table
import org.jooq.TableField
import org.jooq.TableOptions
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class LessonTable(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, LessonRecord>?,
    aliased: Table<LessonRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<LessonRecord>(
    alias,
    DefaultSchema.DEFAULT_SCHEMA,
    child,
    path,
    aliased,
    parameters,
    DSL.comment(""),
    TableOptions.table()
) {
    companion object {

        /**
         * The reference instance of <code>lesson</code>
         */
        val LESSON = LessonTable()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<LessonRecord> = LessonRecord::class.java

    /**
     * The column <code>lesson.lesson_id</code>.
     */
    val LESSON_ID: TableField<LessonRecord, UUID?> = createField(DSL.name("lesson_id"), SQLDataType.UUID.nullable(false), this, "")

    /**
     * The column <code>lesson.group_transition_id</code>.
     */
    val GROUP_TRANSITION_ID: TableField<LessonRecord, UUID?> = createField(DSL.name("group_transition_id"), SQLDataType.UUID.nullable(false), this, "")

    /**
     * The column <code>lesson.slide_id</code>.
     */
    val SLIDE_ID: TableField<LessonRecord, String?> = createField(DSL.name("slide_id"), SQLDataType.VARCHAR(255), this, "")

    private constructor(alias: Name, aliased: Table<LessonRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<LessonRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>lesson</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>lesson</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>lesson</code> table reference
     */
    constructor(): this(DSL.name("lesson"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, LessonRecord>): this(Internal.createPathAlias(child, key), child, key, LESSON, null)
    override fun getSchema(): Schema = DefaultSchema.DEFAULT_SCHEMA
    override fun getPrimaryKey(): UniqueKey<LessonRecord> = LESSON_PKEY
    override fun getKeys(): List<UniqueKey<LessonRecord>> = listOf(LESSON_PKEY, LESSON_GROUP_TRANSITION_ID_SLIDE_ID_KEY)
    override fun `as`(alias: String): LessonTable = LessonTable(DSL.name(alias), this)
    override fun `as`(alias: Name): LessonTable = LessonTable(alias, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): LessonTable = LessonTable(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): LessonTable = LessonTable(name, null)

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row3<UUID?, UUID?, String?> = super.fieldsRow() as Row3<UUID?, UUID?, String?>
}
