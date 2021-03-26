/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.adapter.jooq.tables


import io.github.novemdecillion.adapter.jooq.DefaultSchema
import io.github.novemdecillion.adapter.jooq.tables.records.CurrentGroupTransitionRecord

import java.util.UUID

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Name
import org.jooq.Record
import org.jooq.Row4
import org.jooq.Schema
import org.jooq.Table
import org.jooq.TableField
import org.jooq.TableOptions
import org.jooq.impl.DSL
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class CurrentGroupTransitionTable(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, CurrentGroupTransitionRecord>?,
    aliased: Table<CurrentGroupTransitionRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<CurrentGroupTransitionRecord>(
    alias,
    DefaultSchema.DEFAULT_SCHEMA,
    child,
    path,
    aliased,
    parameters,
    DSL.comment(""),
    TableOptions.materializedView()
) {
    companion object {

        /**
         * The reference instance of <code>current_group_transition</code>
         */
        val CURRENT_GROUP_TRANSITION = CurrentGroupTransitionTable()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<CurrentGroupTransitionRecord> = CurrentGroupTransitionRecord::class.java

    /**
     * The column <code>current_group_transition.group_transition_id</code>.
     */
    val GROUP_TRANSITION_ID: TableField<CurrentGroupTransitionRecord, UUID?> = createField(DSL.name("group_transition_id"), SQLDataType.UUID, this, "")

    /**
     * The column <code>current_group_transition.group_generation_id</code>.
     */
    val GROUP_GENERATION_ID: TableField<CurrentGroupTransitionRecord, UUID?> = createField(DSL.name("group_generation_id"), SQLDataType.UUID, this, "")

    /**
     * The column <code>current_group_transition.group_origin_id</code>.
     */
    val GROUP_ORIGIN_ID: TableField<CurrentGroupTransitionRecord, UUID?> = createField(DSL.name("group_origin_id"), SQLDataType.UUID, this, "")

    /**
     * The column <code>current_group_transition.group_name</code>.
     */
    val GROUP_NAME: TableField<CurrentGroupTransitionRecord, String?> = createField(DSL.name("group_name"), SQLDataType.VARCHAR(255), this, "")

    private constructor(alias: Name, aliased: Table<CurrentGroupTransitionRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<CurrentGroupTransitionRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>current_group_transition</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>current_group_transition</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>current_group_transition</code> table reference
     */
    constructor(): this(DSL.name("current_group_transition"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, CurrentGroupTransitionRecord>): this(Internal.createPathAlias(child, key), child, key, CURRENT_GROUP_TRANSITION, null)
    override fun getSchema(): Schema = DefaultSchema.DEFAULT_SCHEMA
    override fun `as`(alias: String): CurrentGroupTransitionTable = CurrentGroupTransitionTable(DSL.name(alias), this)
    override fun `as`(alias: Name): CurrentGroupTransitionTable = CurrentGroupTransitionTable(alias, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): CurrentGroupTransitionTable = CurrentGroupTransitionTable(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): CurrentGroupTransitionTable = CurrentGroupTransitionTable(name, null)

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row4<UUID?, UUID?, UUID?, String?> = super.fieldsRow() as Row4<UUID?, UUID?, UUID?, String?>
}
