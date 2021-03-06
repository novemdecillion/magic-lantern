/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.adapter.jooq.tables


import io.github.novemdecillion.adapter.jooq.DefaultSchema
import io.github.novemdecillion.adapter.jooq.keys.GROUP_0_PKEY
import io.github.novemdecillion.adapter.jooq.keys.GROUP_0__GROUP_TRANSITION_GROUP_GENERATION_ID_FKEY
import io.github.novemdecillion.adapter.jooq.tables.records.Group_0Record

import java.util.UUID

import kotlin.collections.List

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Name
import org.jooq.Record
import org.jooq.Row4
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
open class Group_0Table(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, Group_0Record>?,
    aliased: Table<Group_0Record>?,
    parameters: Array<Field<*>?>?
): TableImpl<Group_0Record>(
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
         * The reference instance of <code>group_0</code>
         */
        val GROUP_0 = Group_0Table()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<Group_0Record> = Group_0Record::class.java

    /**
     * The column <code>group_0.group_transition_id</code>.
     */
    val GROUP_TRANSITION_ID: TableField<Group_0Record, UUID?> = createField(DSL.name("group_transition_id"), SQLDataType.UUID.nullable(false), this, "")

    /**
     * The column <code>group_0.group_generation_id</code>.
     */
    val GROUP_GENERATION_ID: TableField<Group_0Record, Int?> = createField(DSL.name("group_generation_id"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>group_0.group_name</code>.
     */
    val GROUP_NAME: TableField<Group_0Record, String?> = createField(DSL.name("group_name"), SQLDataType.VARCHAR(255).nullable(false), this, "")

    /**
     * The column <code>group_0.parent_group_transition_id</code>.
     */
    val PARENT_GROUP_TRANSITION_ID: TableField<Group_0Record, UUID?> = createField(DSL.name("parent_group_transition_id"), SQLDataType.UUID, this, "")

    private constructor(alias: Name, aliased: Table<Group_0Record>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<Group_0Record>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>group_0</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>group_0</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>group_0</code> table reference
     */
    constructor(): this(DSL.name("group_0"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, Group_0Record>): this(Internal.createPathAlias(child, key), child, key, GROUP_0, null)
    override fun getSchema(): Schema = DefaultSchema.DEFAULT_SCHEMA
    override fun getPrimaryKey(): UniqueKey<Group_0Record> = GROUP_0_PKEY
    override fun getKeys(): List<UniqueKey<Group_0Record>> = listOf(GROUP_0_PKEY)
    override fun getReferences(): List<ForeignKey<Group_0Record, *>> = listOf(GROUP_0__GROUP_TRANSITION_GROUP_GENERATION_ID_FKEY)

    private lateinit var _groupGeneration: GroupGenerationTable
    fun groupGeneration(): GroupGenerationTable {
        if (!this::_groupGeneration.isInitialized)
            _groupGeneration = GroupGenerationTable(this, GROUP_0__GROUP_TRANSITION_GROUP_GENERATION_ID_FKEY)

        return _groupGeneration;
    }
    override fun `as`(alias: String): Group_0Table = Group_0Table(DSL.name(alias), this)
    override fun `as`(alias: Name): Group_0Table = Group_0Table(alias, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): Group_0Table = Group_0Table(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): Group_0Table = Group_0Table(name, null)

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row4<UUID?, Int?, String?, UUID?> = super.fieldsRow() as Row4<UUID?, Int?, String?, UUID?>
}
