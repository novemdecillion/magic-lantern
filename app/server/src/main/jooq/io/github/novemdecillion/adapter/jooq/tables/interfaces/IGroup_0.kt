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
interface IGroup_0 : Serializable {
    var groupTransitionId: UUID?
    var groupGenerationId: Int?
    var groupName: String?
    var parentGroupTransitionId: UUID?

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common interface IGroup_0
     */
    fun from(from: IGroup_0)

    /**
     * Copy data into another generated Record/POJO implementing the common interface IGroup_0
     */
    fun <E : IGroup_0> into(into: E): E
}