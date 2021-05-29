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
interface IUserAggregate : Serializable {
    var accountId: UUID?
    var accountName: String?
    var password: String?
    var userName: String?
    var email: String?
    var locale: String?
    var realmId: String?
    var enabled: Boolean?
    var role: JSONB?

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common interface IUserAggregate
     */
    fun from(from: IUserAggregate)

    /**
     * Copy data into another generated Record/POJO implementing the common interface IUserAggregate
     */
    fun <E : IUserAggregate> into(into: E): E
}
