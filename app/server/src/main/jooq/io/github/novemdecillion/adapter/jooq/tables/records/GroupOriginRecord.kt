/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.adapter.jooq.tables.records


import io.github.novemdecillion.adapter.jooq.tables.GroupOriginTable
import io.github.novemdecillion.adapter.jooq.tables.interfaces.IGroupOrigin

import java.util.UUID

import org.jooq.Field
import org.jooq.Record1
import org.jooq.Row1
import org.jooq.impl.UpdatableRecordImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class GroupOriginRecord() : UpdatableRecordImpl<GroupOriginRecord>(GroupOriginTable.GROUP_ORIGIN), Record1<UUID?>, IGroupOrigin {

    override var groupOriginId: UUID?
        set(value) = set(0, value)
        get() = get(0) as UUID?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<UUID?> = super.key() as Record1<UUID?>

    // -------------------------------------------------------------------------
    // Record1 type implementation
    // -------------------------------------------------------------------------

    override fun fieldsRow(): Row1<UUID?> = super.fieldsRow() as Row1<UUID?>
    override fun valuesRow(): Row1<UUID?> = super.valuesRow() as Row1<UUID?>
    override fun field1(): Field<UUID?> = GroupOriginTable.GROUP_ORIGIN.GROUP_ORIGIN_ID
    override fun component1(): UUID? = groupOriginId
    override fun value1(): UUID? = groupOriginId

    override fun value1(value: UUID?): GroupOriginRecord {
        this.groupOriginId = value
        return this
    }

    override fun values(value1: UUID?): GroupOriginRecord {
        this.value1(value1)
        return this
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    override fun from(from: IGroupOrigin) {
        groupOriginId = from.groupOriginId
    }

    override fun <E : IGroupOrigin> into(into: E): E {
        into.from(this)
        return into
    }

    /**
     * Create a detached, initialised GroupOriginRecord
     */
    constructor(groupOriginId: UUID? = null): this() {
        this.groupOriginId = groupOriginId
    }
}