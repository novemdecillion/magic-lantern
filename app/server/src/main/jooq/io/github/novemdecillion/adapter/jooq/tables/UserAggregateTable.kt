/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.adapter.jooq.tables


import io.github.novemdecillion.adapter.jooq.DefaultSchema
import io.github.novemdecillion.adapter.jooq.tables.records.UserAggregateRecord

import java.util.UUID

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Name
import org.jooq.Record
import org.jooq.Row12
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
open class UserAggregateTable(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, UserAggregateRecord>?,
    aliased: Table<UserAggregateRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<UserAggregateRecord>(
    alias,
    DefaultSchema.DEFAULT_SCHEMA,
    child,
    path,
    aliased,
    parameters,
    DSL.comment(""),
    TableOptions.view("create view \"user_aggregate\" as  SELECT account.account_id,\n    account.account_name,\n    account.password,\n    account.user_name,\n    account.given_name,\n    account.family_name,\n    account.eastern,\n    account.email,\n    account.realm,\n    account.enabled,\n    current_account_group_authority.group_origin_id,\n    current_account_group_authority.role\n   FROM (account\n     LEFT JOIN current_account_group_authority ON ((current_account_group_authority.account_id = account.account_id)));")
) {
    companion object {

        /**
         * The reference instance of <code>user_aggregate</code>
         */
        val USER_AGGREGATE = UserAggregateTable()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<UserAggregateRecord> = UserAggregateRecord::class.java

    /**
     * The column <code>user_aggregate.account_id</code>.
     */
    val ACCOUNT_ID: TableField<UserAggregateRecord, UUID?> = createField(DSL.name("account_id"), SQLDataType.UUID, this, "")

    /**
     * The column <code>user_aggregate.account_name</code>.
     */
    val ACCOUNT_NAME: TableField<UserAggregateRecord, String?> = createField(DSL.name("account_name"), SQLDataType.VARCHAR(255), this, "")

    /**
     * The column <code>user_aggregate.password</code>.
     */
    val PASSWORD: TableField<UserAggregateRecord, String?> = createField(DSL.name("password"), SQLDataType.VARCHAR(255), this, "")

    /**
     * The column <code>user_aggregate.user_name</code>.
     */
    val USER_NAME: TableField<UserAggregateRecord, String?> = createField(DSL.name("user_name"), SQLDataType.VARCHAR(255), this, "")

    /**
     * The column <code>user_aggregate.given_name</code>.
     */
    val GIVEN_NAME: TableField<UserAggregateRecord, String?> = createField(DSL.name("given_name"), SQLDataType.VARCHAR(255), this, "")

    /**
     * The column <code>user_aggregate.family_name</code>.
     */
    val FAMILY_NAME: TableField<UserAggregateRecord, String?> = createField(DSL.name("family_name"), SQLDataType.VARCHAR(255), this, "")

    /**
     * The column <code>user_aggregate.eastern</code>.
     */
    val EASTERN: TableField<UserAggregateRecord, Boolean?> = createField(DSL.name("eastern"), SQLDataType.BOOLEAN, this, "")

    /**
     * The column <code>user_aggregate.email</code>.
     */
    val EMAIL: TableField<UserAggregateRecord, String?> = createField(DSL.name("email"), SQLDataType.VARCHAR(255), this, "")

    /**
     * The column <code>user_aggregate.realm</code>.
     */
    val REALM: TableField<UserAggregateRecord, String?> = createField(DSL.name("realm"), SQLDataType.VARCHAR(255), this, "")

    /**
     * The column <code>user_aggregate.enabled</code>.
     */
    val ENABLED: TableField<UserAggregateRecord, Boolean?> = createField(DSL.name("enabled"), SQLDataType.BOOLEAN, this, "")

    /**
     * The column <code>user_aggregate.group_origin_id</code>.
     */
    val GROUP_ORIGIN_ID: TableField<UserAggregateRecord, UUID?> = createField(DSL.name("group_origin_id"), SQLDataType.UUID, this, "")

    /**
     * The column <code>user_aggregate.role</code>.
     */
    val ROLE: TableField<UserAggregateRecord, String?> = createField(DSL.name("role"), SQLDataType.VARCHAR(255), this, "")

    private constructor(alias: Name, aliased: Table<UserAggregateRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<UserAggregateRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>user_aggregate</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>user_aggregate</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>user_aggregate</code> table reference
     */
    constructor(): this(DSL.name("user_aggregate"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, UserAggregateRecord>): this(Internal.createPathAlias(child, key), child, key, USER_AGGREGATE, null)
    override fun getSchema(): Schema = DefaultSchema.DEFAULT_SCHEMA
    override fun `as`(alias: String): UserAggregateTable = UserAggregateTable(DSL.name(alias), this)
    override fun `as`(alias: Name): UserAggregateTable = UserAggregateTable(alias, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): UserAggregateTable = UserAggregateTable(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): UserAggregateTable = UserAggregateTable(name, null)

    // -------------------------------------------------------------------------
    // Row12 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row12<UUID?, String?, String?, String?, String?, String?, Boolean?, String?, String?, Boolean?, UUID?, String?> = super.fieldsRow() as Row12<UUID?, String?, String?, String?, String?, String?, Boolean?, String?, String?, Boolean?, UUID?, String?>
}
