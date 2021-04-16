/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.adapter.jooq.tables.interfaces


import io.github.novemdecillion.domain.Role

import java.io.Serializable
import java.util.UUID


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
interface IAccountGroupAuthority : Serializable {
    var accountId: UUID?
    var groupTransitionId: UUID?
    var role: Role?

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common interface IAccountGroupAuthority
     */
    fun from(from: IAccountGroupAuthority)

    /**
     * Copy data into another generated Record/POJO implementing the common interface IAccountGroupAuthority
     */
    fun <E : IAccountGroupAuthority> into(into: E): E
}
