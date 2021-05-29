/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.adapter.jooq.tables.interfaces


import java.io.Serializable
import java.util.UUID

import org.jooq.JSONB


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
interface IAccountGroupAuthority_0 : Serializable {
    var accountId: UUID?
    var groupTransitionId: UUID?
    var groupGenerationId: Int?
    var role: JSONB?

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common interface IAccountGroupAuthority_0
     */
    fun from(from: IAccountGroupAuthority_0)

    /**
     * Copy data into another generated Record/POJO implementing the common interface IAccountGroupAuthority_0
     */
    fun <E : IAccountGroupAuthority_0> into(into: E): E
}
