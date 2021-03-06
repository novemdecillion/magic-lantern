/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.adapter.jooq.tables.pojos


import io.github.novemdecillion.adapter.jooq.tables.interfaces.IGroupTransitionWithPath

import java.util.UUID


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
data class GroupTransitionWithPathEntity(
    override var groupTransitionId: UUID? = null, 
    override var groupGenerationId: Int? = null, 
    override var groupName: String? = null, 
    override var parentGroupTransitionId: UUID? = null, 
    override var layer: Int? = null, 
    override var path: String? = null, 
    override var pathName: String? = null
): IGroupTransitionWithPath {


    override fun toString(): String {
        val sb = StringBuilder("GroupTransitionWithPathEntity (")

        sb.append(groupTransitionId)
        sb.append(", ").append(groupGenerationId)
        sb.append(", ").append(groupName)
        sb.append(", ").append(parentGroupTransitionId)
        sb.append(", ").append(layer)
        sb.append(", ").append(path)
        sb.append(", ").append(pathName)

        sb.append(")")
        return sb.toString()
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    override fun from(from: IGroupTransitionWithPath) {
        groupTransitionId = from.groupTransitionId
        groupGenerationId = from.groupGenerationId
        groupName = from.groupName
        parentGroupTransitionId = from.parentGroupTransitionId
        layer = from.layer
        path = from.path
        pathName = from.pathName
    }

    override fun <E : IGroupTransitionWithPath> into(into: E): E {
        into.from(this)
        return into
    }
}
