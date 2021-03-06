/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.adapter.jooq.tables


import io.github.novemdecillion.adapter.jooq.DefaultSchema
import io.github.novemdecillion.adapter.jooq.keys.NOTICE_PKEY
import io.github.novemdecillion.adapter.jooq.tables.records.NoticeRecord

import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

import kotlin.collections.List

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Name
import org.jooq.Record
import org.jooq.Row5
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
open class NoticeTable(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, NoticeRecord>?,
    aliased: Table<NoticeRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<NoticeRecord>(
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
         * The reference instance of <code>notice</code>
         */
        val NOTICE = NoticeTable()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<NoticeRecord> = NoticeRecord::class.java

    /**
     * The column <code>notice.notice_id</code>.
     */
    val NOTICE_ID: TableField<NoticeRecord, UUID?> = createField(DSL.name("notice_id"), SQLDataType.UUID.nullable(false), this, "")

    /**
     * The column <code>notice.message</code>.
     */
    val MESSAGE: TableField<NoticeRecord, String?> = createField(DSL.name("message"), SQLDataType.CLOB.nullable(false), this, "")

    /**
     * The column <code>notice.start_at</code>.
     */
    val START_AT: TableField<NoticeRecord, LocalDate?> = createField(DSL.name("start_at"), SQLDataType.LOCALDATE, this, "")

    /**
     * The column <code>notice.end_at</code>.
     */
    val END_AT: TableField<NoticeRecord, LocalDate?> = createField(DSL.name("end_at"), SQLDataType.LOCALDATE, this, "")

    /**
     * The column <code>notice.update_at</code>.
     */
    val UPDATE_AT: TableField<NoticeRecord, OffsetDateTime?> = createField(DSL.name("update_at"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).nullable(false), this, "")

    private constructor(alias: Name, aliased: Table<NoticeRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<NoticeRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>notice</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>notice</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>notice</code> table reference
     */
    constructor(): this(DSL.name("notice"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, NoticeRecord>): this(Internal.createPathAlias(child, key), child, key, NOTICE, null)
    override fun getSchema(): Schema = DefaultSchema.DEFAULT_SCHEMA
    override fun getPrimaryKey(): UniqueKey<NoticeRecord> = NOTICE_PKEY
    override fun getKeys(): List<UniqueKey<NoticeRecord>> = listOf(NOTICE_PKEY)
    override fun `as`(alias: String): NoticeTable = NoticeTable(DSL.name(alias), this)
    override fun `as`(alias: Name): NoticeTable = NoticeTable(alias, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): NoticeTable = NoticeTable(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): NoticeTable = NoticeTable(name, null)

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row5<UUID?, String?, LocalDate?, LocalDate?, OffsetDateTime?> = super.fieldsRow() as Row5<UUID?, String?, LocalDate?, LocalDate?, OffsetDateTime?>
}
