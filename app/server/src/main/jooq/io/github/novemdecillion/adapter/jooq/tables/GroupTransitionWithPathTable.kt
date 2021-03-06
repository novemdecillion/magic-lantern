/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.adapter.jooq.tables


import io.github.novemdecillion.adapter.jooq.DefaultSchema
import io.github.novemdecillion.adapter.jooq.tables.records.GroupTransitionWithPathRecord

import java.util.UUID

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Name
import org.jooq.Record
import org.jooq.Row7
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
open class GroupTransitionWithPathTable(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, GroupTransitionWithPathRecord>?,
    aliased: Table<GroupTransitionWithPathRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<GroupTransitionWithPathRecord>(
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
         * The reference instance of <code>group_transition_with_path</code>
         */
        val GROUP_TRANSITION_WITH_PATH = GroupTransitionWithPathTable()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<GroupTransitionWithPathRecord> = GroupTransitionWithPathRecord::class.java

    /**
     * The column <code>group_transition_with_path.group_transition_id</code>.
     */
    val GROUP_TRANSITION_ID: TableField<GroupTransitionWithPathRecord, UUID?> = createField(DSL.name("group_transition_id"), SQLDataType.UUID, this, "")

    /**
     * The column <code>group_transition_with_path.group_generation_id</code>.
     */
    val GROUP_GENERATION_ID: TableField<GroupTransitionWithPathRecord, Int?> = createField(DSL.name("group_generation_id"), SQLDataType.INTEGER, this, "")

    /**
     * The column <code>group_transition_with_path.group_name</code>.
     */
    val GROUP_NAME: TableField<GroupTransitionWithPathRecord, String?> = createField(DSL.name("group_name"), SQLDataType.VARCHAR(255), this, "")

    /**
     * The column <code>group_transition_with_path.parent_group_transition_id</code>.
     */
    val PARENT_GROUP_TRANSITION_ID: TableField<GroupTransitionWithPathRecord, UUID?> = createField(DSL.name("parent_group_transition_id"), SQLDataType.UUID, this, "")

    /**
     * The column <code>group_transition_with_path.layer</code>.
     */
    val LAYER: TableField<GroupTransitionWithPathRecord, Int?> = createField(DSL.name("layer"), SQLDataType.INTEGER, this, "")

    /**
     * The column <code>group_transition_with_path.path</code>.
     */
    val PATH: TableField<GroupTransitionWithPathRecord, String?> = createField(DSL.name("path"), SQLDataType.CLOB, this, "")

    /**
     * The column <code>group_transition_with_path.path_name</code>.
     */
    val PATH_NAME: TableField<GroupTransitionWithPathRecord, String?> = createField(DSL.name("path_name"), SQLDataType.CLOB, this, "")

    private constructor(alias: Name, aliased: Table<GroupTransitionWithPathRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<GroupTransitionWithPathRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>group_transition_with_path</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>group_transition_with_path</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>group_transition_with_path</code> table reference
     */
    constructor(): this(DSL.name("group_transition_with_path"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, GroupTransitionWithPathRecord>): this(Internal.createPathAlias(child, key), child, key, GROUP_TRANSITION_WITH_PATH, null)
    override fun getSchema(): Schema = DefaultSchema.DEFAULT_SCHEMA
    override fun `as`(alias: String): GroupTransitionWithPathTable = GroupTransitionWithPathTable(DSL.name(alias), this)
    override fun `as`(alias: Name): GroupTransitionWithPathTable = GroupTransitionWithPathTable(alias, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): GroupTransitionWithPathTable = GroupTransitionWithPathTable(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): GroupTransitionWithPathTable = GroupTransitionWithPathTable(name, null)

    // -------------------------------------------------------------------------
    // Row7 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row7<UUID?, Int?, String?, UUID?, Int?, String?, String?> = super.fieldsRow() as Row7<UUID?, Int?, String?, UUID?, Int?, String?, String?>
}
