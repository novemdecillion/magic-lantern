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
interface IGroupTransitionWithPath : Serializable {
    var groupTransitionId: UUID?
    var groupGenerationId: Int?
    var groupName: String?
    var parentGroupTransitionId: UUID?
    var layer: Int?
    var path: String?
    var pathName: String?

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common interface IGroupTransitionWithPath
     */
    fun from(from: IGroupTransitionWithPath)

    /**
     * Copy data into another generated Record/POJO implementing the common interface IGroupTransitionWithPath
     */
    fun <E : IGroupTransitionWithPath> into(into: E): E
}
