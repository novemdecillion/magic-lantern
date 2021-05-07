/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.adapter.jooq.tables


import io.github.novemdecillion.adapter.jooq.DefaultSchema
import io.github.novemdecillion.adapter.jooq.keys.SLIDE_PKEY
import io.github.novemdecillion.adapter.jooq.tables.records.SlideRecord

import kotlin.collections.List

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Name
import org.jooq.Record
import org.jooq.Row1
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
open class SlideTable(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, SlideRecord>?,
    aliased: Table<SlideRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<SlideRecord>(
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
         * The reference instance of <code>slide</code>
         */
        val SLIDE = SlideTable()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<SlideRecord> = SlideRecord::class.java

    /**
     * The column <code>slide.slide_id</code>.
     */
    val SLIDE_ID: TableField<SlideRecord, String?> = createField(DSL.name("slide_id"), SQLDataType.VARCHAR(255).nullable(false), this, "")

    private constructor(alias: Name, aliased: Table<SlideRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<SlideRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>slide</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>slide</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>slide</code> table reference
     */
    constructor(): this(DSL.name("slide"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, SlideRecord>): this(Internal.createPathAlias(child, key), child, key, SLIDE, null)
    override fun getSchema(): Schema = DefaultSchema.DEFAULT_SCHEMA
    override fun getPrimaryKey(): UniqueKey<SlideRecord> = SLIDE_PKEY
    override fun getKeys(): List<UniqueKey<SlideRecord>> = listOf(SLIDE_PKEY)
    override fun `as`(alias: String): SlideTable = SlideTable(DSL.name(alias), this)
    override fun `as`(alias: Name): SlideTable = SlideTable(alias, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): SlideTable = SlideTable(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): SlideTable = SlideTable(name, null)

    // -------------------------------------------------------------------------
    // Row1 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row1<String?> = super.fieldsRow() as Row1<String?>
}
