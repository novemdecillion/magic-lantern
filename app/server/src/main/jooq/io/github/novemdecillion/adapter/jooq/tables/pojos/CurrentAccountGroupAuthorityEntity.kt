/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.adapter.jooq.tables.pojos


import io.github.novemdecillion.adapter.jooq.tables.interfaces.ICurrentAccountGroupAuthority
import io.github.novemdecillion.domain.Role

import java.util.UUID


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
data class CurrentAccountGroupAuthorityEntity(
    override var accountId: UUID? = null, 
    override var groupTransitionId: UUID? = null, 
    override var role: Role? = null, 
    override var groupOriginId: UUID? = null
): ICurrentAccountGroupAuthority {


    override fun toString(): String {
        val sb = StringBuilder("CurrentAccountGroupAuthorityEntity (")

        sb.append(accountId)
        sb.append(", ").append(groupTransitionId)
        sb.append(", ").append(role)
        sb.append(", ").append(groupOriginId)

        sb.append(")")
        return sb.toString()
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    override fun from(from: ICurrentAccountGroupAuthority) {
        accountId = from.accountId
        groupTransitionId = from.groupTransitionId
        role = from.role
        groupOriginId = from.groupOriginId
    }

    override fun <E : ICurrentAccountGroupAuthority> into(into: E): E {
        into.from(this)
        return into
    }
}
