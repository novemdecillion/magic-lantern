/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.adapter.jooq.tables.daos


import io.github.novemdecillion.adapter.jooq.tables.Group_0Table
import io.github.novemdecillion.adapter.jooq.tables.pojos.Group_0Entity
import io.github.novemdecillion.adapter.jooq.tables.records.Group_0Record

import java.util.UUID

import kotlin.collections.List

import org.jooq.Configuration
import org.jooq.Record2
import org.jooq.impl.DAOImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class Group_0Dao(configuration: Configuration?) : DAOImpl<Group_0Record, Group_0Entity, Record2<UUID?, Int?>>(Group_0Table.GROUP_0, Group_0Entity::class.java, configuration) {

    /**
     * Create a new Group_0Dao without any configuration
     */
    constructor(): this(null)

    override fun getId(o: Group_0Entity): Record2<UUID?, Int?>? = compositeKeyRecord(o.groupTransitionId, o.groupGenerationId)

    /**
     * Fetch records that have <code>group_transition_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    fun fetchRangeOfGroupTransitionIdTable(lowerInclusive: UUID?, upperInclusive: UUID?): List<Group_0Entity> = fetchRange(Group_0Table.GROUP_0.GROUP_TRANSITION_ID, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>group_transition_id IN (values)</code>
     */
    fun fetchByGroupTransitionIdTable(vararg values: UUID): List<Group_0Entity> = fetch(Group_0Table.GROUP_0.GROUP_TRANSITION_ID, *values)

    /**
     * Fetch records that have <code>group_generation_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    fun fetchRangeOfGroupGenerationIdTable(lowerInclusive: Int?, upperInclusive: Int?): List<Group_0Entity> = fetchRange(Group_0Table.GROUP_0.GROUP_GENERATION_ID, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>group_generation_id IN (values)</code>
     */
    fun fetchByGroupGenerationIdTable(vararg values: Int): List<Group_0Entity> = fetch(Group_0Table.GROUP_0.GROUP_GENERATION_ID, *values.toTypedArray())

    /**
     * Fetch records that have <code>group_name BETWEEN lowerInclusive AND upperInclusive</code>
     */
    fun fetchRangeOfGroupNameTable(lowerInclusive: String?, upperInclusive: String?): List<Group_0Entity> = fetchRange(Group_0Table.GROUP_0.GROUP_NAME, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>group_name IN (values)</code>
     */
    fun fetchByGroupNameTable(vararg values: String): List<Group_0Entity> = fetch(Group_0Table.GROUP_0.GROUP_NAME, *values)

    /**
     * Fetch records that have <code>parent_group_transition_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    fun fetchRangeOfParentGroupTransitionIdTable(lowerInclusive: UUID?, upperInclusive: UUID?): List<Group_0Entity> = fetchRange(Group_0Table.GROUP_0.PARENT_GROUP_TRANSITION_ID, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>parent_group_transition_id IN (values)</code>
     */
    fun fetchByParentGroupTransitionIdTable(vararg values: UUID): List<Group_0Entity> = fetch(Group_0Table.GROUP_0.PARENT_GROUP_TRANSITION_ID, *values)
}