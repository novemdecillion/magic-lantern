/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.adapter.jooq.tables.interfaces


import java.io.Serializable
import java.util.UUID


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
interface ICurrentGroupTransition : Serializable {
    var groupTransitionId: UUID?
    var groupGenerationId: UUID?
    var groupOriginId: UUID?
    var groupName: String?
    var parentGroupTransitionId: UUID?

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common interface ICurrentGroupTransition
     */
    fun from(from: ICurrentGroupTransition)

    /**
     * Copy data into another generated Record/POJO implementing the common interface ICurrentGroupTransition
     */
    fun <E : ICurrentGroupTransition> into(into: E): E
}
