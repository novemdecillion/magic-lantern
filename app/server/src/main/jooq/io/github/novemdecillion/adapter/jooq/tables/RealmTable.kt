/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.adapter.jooq.tables


import io.github.novemdecillion.adapter.jooq.DefaultSchema
import io.github.novemdecillion.adapter.jooq.keys.REALM_PKEY
import io.github.novemdecillion.adapter.jooq.tables.records.RealmRecord

import java.time.LocalDateTime

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
open class RealmTable(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, RealmRecord>?,
    aliased: Table<RealmRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<RealmRecord>(
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
         * The reference instance of <code>realm</code>
         */
        val REALM = RealmTable()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<RealmRecord> = RealmRecord::class.java

    /**
     * The column <code>realm.realm_id</code>.
     */
    val REALM_ID: TableField<RealmRecord, String?> = createField(DSL.name("realm_id"), SQLDataType.VARCHAR(255).nullable(false), this, "")

    /**
     * The column <code>realm.realm_name</code>.
     */
    val REALM_NAME: TableField<RealmRecord, String?> = createField(DSL.name("realm_name"), SQLDataType.VARCHAR(255), this, "")

    /**
     * The column <code>realm.enabled</code>.
     */
    val ENABLED: TableField<RealmRecord, Boolean?> = createField(DSL.name("enabled"), SQLDataType.BOOLEAN.nullable(false).defaultValue(DSL.field("true", SQLDataType.BOOLEAN)), this, "")

    /**
     * The column <code>realm.sync_at</code>.
     */
    val SYNC_AT: TableField<RealmRecord, LocalDateTime?> = createField(DSL.name("sync_at"), SQLDataType.LOCALDATETIME(6), this, "")

    private constructor(alias: Name, aliased: Table<RealmRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<RealmRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>realm</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>realm</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>realm</code> table reference
     */
    constructor(): this(DSL.name("realm"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, RealmRecord>): this(Internal.createPathAlias(child, key), child, key, REALM, null)
    override fun getSchema(): Schema = DefaultSchema.DEFAULT_SCHEMA
    override fun getPrimaryKey(): UniqueKey<RealmRecord> = REALM_PKEY
    override fun getKeys(): List<UniqueKey<RealmRecord>> = listOf(REALM_PKEY)
    override fun `as`(alias: String): RealmTable = RealmTable(DSL.name(alias), this)
    override fun `as`(alias: Name): RealmTable = RealmTable(alias, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): RealmTable = RealmTable(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): RealmTable = RealmTable(name, null)

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row4<String?, String?, Boolean?, LocalDateTime?> = super.fieldsRow() as Row4<String?, String?, Boolean?, LocalDateTime?>
}