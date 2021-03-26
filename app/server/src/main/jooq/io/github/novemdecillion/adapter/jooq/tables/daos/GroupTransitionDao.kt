/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.adapter.jooq.tables.daos


import io.github.novemdecillion.adapter.jooq.tables.GroupTransitionTable
import io.github.novemdecillion.adapter.jooq.tables.pojos.GroupTransitionEntity
import io.github.novemdecillion.adapter.jooq.tables.records.GroupTransitionRecord

import java.util.UUID

import kotlin.collections.List

import org.jooq.Configuration
import org.jooq.impl.DAOImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class GroupTransitionDao(configuration: Configuration?) : DAOImpl<GroupTransitionRecord, GroupTransitionEntity, UUID>(GroupTransitionTable.GROUP_TRANSITION, GroupTransitionEntity::class.java, configuration) {

    /**
     * Create a new GroupTransitionDao without any configuration
     */
    constructor(): this(null)

    override fun getId(o: GroupTransitionEntity): UUID? = o.groupTransitionId

    /**
     * Fetch records that have <code>group_transition_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    fun fetchRangeOfGroupTransitionIdTable(lowerInclusive: UUID?, upperInclusive: UUID?): List<GroupTransitionEntity> = fetchRange(GroupTransitionTable.GROUP_TRANSITION.GROUP_TRANSITION_ID, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>group_transition_id IN (values)</code>
     */
    fun fetchByGroupTransitionIdTable(vararg values: UUID): List<GroupTransitionEntity> = fetch(GroupTransitionTable.GROUP_TRANSITION.GROUP_TRANSITION_ID, *values)

    /**
     * Fetch a unique record that has <code>group_transition_id = value</code>
     */
    fun fetchOneByGroupTransitionIdTable(value: UUID): GroupTransitionEntity? = fetchOne(GroupTransitionTable.GROUP_TRANSITION.GROUP_TRANSITION_ID, value)

    /**
     * Fetch records that have <code>group_generation_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    fun fetchRangeOfGroupGenerationIdTable(lowerInclusive: UUID?, upperInclusive: UUID?): List<GroupTransitionEntity> = fetchRange(GroupTransitionTable.GROUP_TRANSITION.GROUP_GENERATION_ID, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>group_generation_id IN (values)</code>
     */
    fun fetchByGroupGenerationIdTable(vararg values: UUID): List<GroupTransitionEntity> = fetch(GroupTransitionTable.GROUP_TRANSITION.GROUP_GENERATION_ID, *values)

    /**
     * Fetch records that have <code>group_origin_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    fun fetchRangeOfGroupOriginIdTable(lowerInclusive: UUID?, upperInclusive: UUID?): List<GroupTransitionEntity> = fetchRange(GroupTransitionTable.GROUP_TRANSITION.GROUP_ORIGIN_ID, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>group_origin_id IN (values)</code>
     */
    fun fetchByGroupOriginIdTable(vararg values: UUID): List<GroupTransitionEntity> = fetch(GroupTransitionTable.GROUP_TRANSITION.GROUP_ORIGIN_ID, *values)

    /**
     * Fetch records that have <code>group_name BETWEEN lowerInclusive AND upperInclusive</code>
     */
    fun fetchRangeOfGroupNameTable(lowerInclusive: String?, upperInclusive: String?): List<GroupTransitionEntity> = fetchRange(GroupTransitionTable.GROUP_TRANSITION.GROUP_NAME, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>group_name IN (values)</code>
     */
    fun fetchByGroupNameTable(vararg values: String): List<GroupTransitionEntity> = fetch(GroupTransitionTable.GROUP_TRANSITION.GROUP_NAME, *values)
}
