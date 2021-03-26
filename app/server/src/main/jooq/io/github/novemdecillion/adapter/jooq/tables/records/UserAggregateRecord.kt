/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.adapter.jooq.tables.records


import io.github.novemdecillion.adapter.jooq.tables.UserAggregateTable
import io.github.novemdecillion.adapter.jooq.tables.interfaces.IUserAggregate

import java.util.UUID

import org.jooq.Field
import org.jooq.Record12
import org.jooq.Row12
import org.jooq.impl.TableRecordImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class UserAggregateRecord() : TableRecordImpl<UserAggregateRecord>(UserAggregateTable.USER_AGGREGATE), Record12<UUID?, String?, String?, String?, String?, String?, Boolean?, String?, String?, Boolean?, UUID?, String?>, IUserAggregate {

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

    override var givenName: String?
        set(value) = set(4, value)
        get() = get(4) as String?

    override var familyName: String?
        set(value) = set(5, value)
        get() = get(5) as String?

    override var eastern: Boolean?
        set(value) = set(6, value)
        get() = get(6) as Boolean?

    override var email: String?
        set(value) = set(7, value)
        get() = get(7) as String?

    override var realm: String?
        set(value) = set(8, value)
        get() = get(8) as String?

    override var enabled: Boolean?
        set(value) = set(9, value)
        get() = get(9) as Boolean?

    override var groupOriginId: UUID?
        set(value) = set(10, value)
        get() = get(10) as UUID?

    override var role: String?
        set(value) = set(11, value)
        get() = get(11) as String?

    // -------------------------------------------------------------------------
    // Record12 type implementation
    // -------------------------------------------------------------------------

    override fun fieldsRow(): Row12<UUID?, String?, String?, String?, String?, String?, Boolean?, String?, String?, Boolean?, UUID?, String?> = super.fieldsRow() as Row12<UUID?, String?, String?, String?, String?, String?, Boolean?, String?, String?, Boolean?, UUID?, String?>
    override fun valuesRow(): Row12<UUID?, String?, String?, String?, String?, String?, Boolean?, String?, String?, Boolean?, UUID?, String?> = super.valuesRow() as Row12<UUID?, String?, String?, String?, String?, String?, Boolean?, String?, String?, Boolean?, UUID?, String?>
    override fun field1(): Field<UUID?> = UserAggregateTable.USER_AGGREGATE.ACCOUNT_ID
    override fun field2(): Field<String?> = UserAggregateTable.USER_AGGREGATE.ACCOUNT_NAME
    override fun field3(): Field<String?> = UserAggregateTable.USER_AGGREGATE.PASSWORD
    override fun field4(): Field<String?> = UserAggregateTable.USER_AGGREGATE.USER_NAME
    override fun field5(): Field<String?> = UserAggregateTable.USER_AGGREGATE.GIVEN_NAME
    override fun field6(): Field<String?> = UserAggregateTable.USER_AGGREGATE.FAMILY_NAME
    override fun field7(): Field<Boolean?> = UserAggregateTable.USER_AGGREGATE.EASTERN
    override fun field8(): Field<String?> = UserAggregateTable.USER_AGGREGATE.EMAIL
    override fun field9(): Field<String?> = UserAggregateTable.USER_AGGREGATE.REALM
    override fun field10(): Field<Boolean?> = UserAggregateTable.USER_AGGREGATE.ENABLED
    override fun field11(): Field<UUID?> = UserAggregateTable.USER_AGGREGATE.GROUP_ORIGIN_ID
    override fun field12(): Field<String?> = UserAggregateTable.USER_AGGREGATE.ROLE
    override fun component1(): UUID? = accountId
    override fun component2(): String? = accountName
    override fun component3(): String? = password
    override fun component4(): String? = userName
    override fun component5(): String? = givenName
    override fun component6(): String? = familyName
    override fun component7(): Boolean? = eastern
    override fun component8(): String? = email
    override fun component9(): String? = realm
    override fun component10(): Boolean? = enabled
    override fun component11(): UUID? = groupOriginId
    override fun component12(): String? = role
    override fun value1(): UUID? = accountId
    override fun value2(): String? = accountName
    override fun value3(): String? = password
    override fun value4(): String? = userName
    override fun value5(): String? = givenName
    override fun value6(): String? = familyName
    override fun value7(): Boolean? = eastern
    override fun value8(): String? = email
    override fun value9(): String? = realm
    override fun value10(): Boolean? = enabled
    override fun value11(): UUID? = groupOriginId
    override fun value12(): String? = role

    override fun value1(value: UUID?): UserAggregateRecord {
        this.accountId = value
        return this
    }

    override fun value2(value: String?): UserAggregateRecord {
        this.accountName = value
        return this
    }

    override fun value3(value: String?): UserAggregateRecord {
        this.password = value
        return this
    }

    override fun value4(value: String?): UserAggregateRecord {
        this.userName = value
        return this
    }

    override fun value5(value: String?): UserAggregateRecord {
        this.givenName = value
        return this
    }

    override fun value6(value: String?): UserAggregateRecord {
        this.familyName = value
        return this
    }

    override fun value7(value: Boolean?): UserAggregateRecord {
        this.eastern = value
        return this
    }

    override fun value8(value: String?): UserAggregateRecord {
        this.email = value
        return this
    }

    override fun value9(value: String?): UserAggregateRecord {
        this.realm = value
        return this
    }

    override fun value10(value: Boolean?): UserAggregateRecord {
        this.enabled = value
        return this
    }

    override fun value11(value: UUID?): UserAggregateRecord {
        this.groupOriginId = value
        return this
    }

    override fun value12(value: String?): UserAggregateRecord {
        this.role = value
        return this
    }

    override fun values(value1: UUID?, value2: String?, value3: String?, value4: String?, value5: String?, value6: String?, value7: Boolean?, value8: String?, value9: String?, value10: Boolean?, value11: UUID?, value12: String?): UserAggregateRecord {
        this.value1(value1)
        this.value2(value2)
        this.value3(value3)
        this.value4(value4)
        this.value5(value5)
        this.value6(value6)
        this.value7(value7)
        this.value8(value8)
        this.value9(value9)
        this.value10(value10)
        this.value11(value11)
        this.value12(value12)
        return this
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    override fun from(from: IUserAggregate) {
        accountId = from.accountId
        accountName = from.accountName
        password = from.password
        userName = from.userName
        givenName = from.givenName
        familyName = from.familyName
        eastern = from.eastern
        email = from.email
        realm = from.realm
        enabled = from.enabled
        groupOriginId = from.groupOriginId
        role = from.role
    }

    override fun <E : IUserAggregate> into(into: E): E {
        into.from(this)
        return into
    }

    /**
     * Create a detached, initialised UserAggregateRecord
     */
    constructor(accountId: UUID? = null, accountName: String? = null, password: String? = null, userName: String? = null, givenName: String? = null, familyName: String? = null, eastern: Boolean? = null, email: String? = null, realm: String? = null, enabled: Boolean? = null, groupOriginId: UUID? = null, role: String? = null): this() {
        this.accountId = accountId
        this.accountName = accountName
        this.password = password
        this.userName = userName
        this.givenName = givenName
        this.familyName = familyName
        this.eastern = eastern
        this.email = email
        this.realm = realm
        this.enabled = enabled
        this.groupOriginId = groupOriginId
        this.role = role
    }
}
