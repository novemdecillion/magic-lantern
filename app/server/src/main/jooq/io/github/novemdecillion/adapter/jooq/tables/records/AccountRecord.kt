/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.adapter.jooq.tables.records


import io.github.novemdecillion.adapter.jooq.tables.AccountTable
import io.github.novemdecillion.adapter.jooq.tables.interfaces.IAccount

import java.util.UUID

import org.jooq.Field
import org.jooq.Record1
import org.jooq.Record8
import org.jooq.Row8
import org.jooq.impl.UpdatableRecordImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class AccountRecord() : UpdatableRecordImpl<AccountRecord>(AccountTable.ACCOUNT), Record8<UUID?, String?, String?, String?, String?, String?, String?, Boolean?>, IAccount {

    override var accountId: UUID?
        set(value) = set(0, value)
        get() = get(0) as UUID?

    override var accountName: String?
        set(value) = set(1, value)
        get() = get(1) as String?

    override var password: String?
        set(value) = set(2, value)
        get() = get(2) as String?

    override var userName: String?
        set(value) = set(3, value)
        get() = get(3) as String?

    override var email: String?
        set(value) = set(4, value)
        get() = get(4) as String?

    override var locale: String?
        set(value) = set(5, value)
        get() = get(5) as String?

    override var realmId: String?
        set(value) = set(6, value)
        get() = get(6) as String?

    override var enabled: Boolean?
        set(value) = set(7, value)
        get() = get(7) as Boolean?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<UUID?> = super.key() as Record1<UUID?>

    // -------------------------------------------------------------------------
    // Record8 type implementation
    // -------------------------------------------------------------------------

    override fun fieldsRow(): Row8<UUID?, String?, String?, String?, String?, String?, String?, Boolean?> = super.fieldsRow() as Row8<UUID?, String?, String?, String?, String?, String?, String?, Boolean?>
    override fun valuesRow(): Row8<UUID?, String?, String?, String?, String?, String?, String?, Boolean?> = super.valuesRow() as Row8<UUID?, String?, String?, String?, String?, String?, String?, Boolean?>
    override fun field1(): Field<UUID?> = AccountTable.ACCOUNT.ACCOUNT_ID
    override fun field2(): Field<String?> = AccountTable.ACCOUNT.ACCOUNT_NAME
    override fun field3(): Field<String?> = AccountTable.ACCOUNT.PASSWORD
    override fun field4(): Field<String?> = AccountTable.ACCOUNT.USER_NAME
    override fun field5(): Field<String?> = AccountTable.ACCOUNT.EMAIL
    override fun field6(): Field<String?> = AccountTable.ACCOUNT.LOCALE
    override fun field7(): Field<String?> = AccountTable.ACCOUNT.REALM_ID
    override fun field8(): Field<Boolean?> = AccountTable.ACCOUNT.ENABLED
    override fun component1(): UUID? = accountId
    override fun component2(): String? = accountName
    override fun component3(): String? = password
    override fun component4(): String? = userName
    override fun component5(): String? = email
    override fun component6(): String? = locale
    override fun component7(): String? = realmId
    override fun component8(): Boolean? = enabled
    override fun value1(): UUID? = accountId
    override fun value2(): String? = accountName
    override fun value3(): String? = password
    override fun value4(): String? = userName
    override fun value5(): String? = email
    override fun value6(): String? = locale
    override fun value7(): String? = realmId
    override fun value8(): Boolean? = enabled

    override fun value1(value: UUID?): AccountRecord {
        this.accountId = value
        return this
    }

    override fun value2(value: String?): AccountRecord {
        this.accountName = value
        return this
    }

    override fun value3(value: String?): AccountRecord {
        this.password = value
        return this
    }

    override fun value4(value: String?): AccountRecord {
        this.userName = value
        return this
    }

    override fun value5(value: String?): AccountRecord {
        this.email = value
        return this
    }

    override fun value6(value: String?): AccountRecord {
        this.locale = value
        return this
    }

    override fun value7(value: String?): AccountRecord {
        this.realmId = value
        return this
    }

    override fun value8(value: Boolean?): AccountRecord {
        this.enabled = value
        return this
    }

    override fun values(value1: UUID?, value2: String?, value3: String?, value4: String?, value5: String?, value6: String?, value7: String?, value8: Boolean?): AccountRecord {
        this.value1(value1)
        this.value2(value2)
        this.value3(value3)
        this.value4(value4)
        this.value5(value5)
        this.value6(value6)
        this.value7(value7)
        this.value8(value8)
        return this
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    override fun from(from: IAccount) {
        accountId = from.accountId
        accountName = from.accountName
        password = from.password
        userName = from.userName
        email = from.email
        locale = from.locale
        realmId = from.realmId
        enabled = from.enabled
    }

    override fun <E : IAccount> into(into: E): E {
        into.from(this)
        return into
    }

    /**
     * Create a detached, initialised AccountRecord
     */
    constructor(accountId: UUID? = null, accountName: String? = null, password: String? = null, userName: String? = null, email: String? = null, locale: String? = null, realmId: String? = null, enabled: Boolean? = null): this() {
        this.accountId = accountId
        this.accountName = accountName
        this.password = password
        this.userName = userName
        this.email = email
        this.locale = locale
        this.realmId = realmId
        this.enabled = enabled
    }
}
