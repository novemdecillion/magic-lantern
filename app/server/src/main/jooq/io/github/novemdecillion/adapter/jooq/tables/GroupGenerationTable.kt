/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.adapter.jooq.tables


import io.github.novemdecillion.adapter.jooq.DefaultSchema
import io.github.novemdecillion.adapter.jooq.keys.GROUP_GENERATION_PKEY
import io.github.novemdecillion.adapter.jooq.tables.records.GroupGenerationRecord

import java.time.OffsetDateTime
import java.util.UUID

import kotlin.collections.List

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Name
import org.jooq.Record
import org.jooq.Row2
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
open class GroupGenerationTable(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, GroupGenerationRecord>?,
    aliased: Table<GroupGenerationRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<GroupGenerationRecord>(
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
         * The reference instance of <code>group_generation</code>
         */
        val GROUP_GENERATION = GroupGenerationTable()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<GroupGenerationRecord> = GroupGenerationRecord::class.java

    /**
     * The column <code>group_generation.group_generation_id</code>.
     */
    val GROUP_GENERATION_ID: TableField<GroupGenerationRecord, UUID?> = createField(DSL.name("group_generation_id"), SQLDataType.UUID.nullable(false), this, "")

    /**
     * The column <code>group_generation.start_date</code>.
     */
    val START_DATE: TableField<GroupGenerationRecord, OffsetDateTime?> = createField(DSL.name("start_date"), SQLDataType.TIMESTAMPWITHTIMEZONE(6), this, "")

    private constructor(alias: Name, aliased: Table<GroupGenerationRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<GroupGenerationRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>group_generation</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>group_generation</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>group_generation</code> table reference
     */
    constructor(): this(DSL.name("group_generation"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, GroupGenerationRecord>): this(Internal.createPathAlias(child, key), child, key, GROUP_GENERATION, null)
    override fun getSchema(): Schema = DefaultSchema.DEFAULT_SCHEMA
    override fun getPrimaryKey(): UniqueKey<GroupGenerationRecord> = GROUP_GENERATION_PKEY
    override fun getKeys(): List<UniqueKey<GroupGenerationRecord>> = listOf(GROUP_GENERATION_PKEY)
    override fun `as`(alias: String): GroupGenerationTable = GroupGenerationTable(DSL.name(alias), this)
    override fun `as`(alias: Name): GroupGenerationTable = GroupGenerationTable(alias, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): GroupGenerationTable = GroupGenerationTable(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): GroupGenerationTable = GroupGenerationTable(name, null)

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row2<UUID?, OffsetDateTime?> = super.fieldsRow() as Row2<UUID?, OffsetDateTime?>
}
