/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.adapter.jooq.tables.daos


import io.github.novemdecillion.adapter.jooq.tables.RealmTable
import io.github.novemdecillion.adapter.jooq.tables.pojos.RealmEntity
import io.github.novemdecillion.adapter.jooq.tables.records.RealmRecord

import java.time.LocalDateTime

import kotlin.collections.List

import org.jooq.Configuration
import org.jooq.impl.DAOImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class RealmDao(configuration: Configuration?) : DAOImpl<RealmRecord, RealmEntity, String>(RealmTable.REALM, RealmEntity::class.java, configuration) {

    /**
     * Create a new RealmDao without any configuration
     */
    constructor(): this(null)

    override fun getId(o: RealmEntity): String? = o.realmId

    /**
     * Fetch records that have <code>realm_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    fun fetchRangeOfRealmIdTable(lowerInclusive: String?, upperInclusive: String?): List<RealmEntity> = fetchRange(RealmTable.REALM.REALM_ID, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>realm_id IN (values)</code>
     */
    fun fetchByRealmIdTable(vararg values: String): List<RealmEntity> = fetch(RealmTable.REALM.REALM_ID, *values)

    /**
     * Fetch a unique record that has <code>realm_id = value</code>
     */
    fun fetchOneByRealmIdTable(value: String): RealmEntity? = fetchOne(RealmTable.REALM.REALM_ID, value)

    /**
     * Fetch records that have <code>realm_name BETWEEN lowerInclusive AND upperInclusive</code>
     */
    fun fetchRangeOfRealmNameTable(lowerInclusive: String?, upperInclusive: String?): List<RealmEntity> = fetchRange(RealmTable.REALM.REALM_NAME, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>realm_name IN (values)</code>
     */
    fun fetchByRealmNameTable(vararg values: String): List<RealmEntity> = fetch(RealmTable.REALM.REALM_NAME, *values)

    /**
     * Fetch records that have <code>enabled BETWEEN lowerInclusive AND upperInclusive</code>
     */
    fun fetchRangeOfEnabledTable(lowerInclusive: Boolean?, upperInclusive: Boolean?): List<RealmEntity> = fetchRange(RealmTable.REALM.ENABLED, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>enabled IN (values)</code>
     */
    fun fetchByEnabledTable(vararg values: Boolean): List<RealmEntity> = fetch(RealmTable.REALM.ENABLED, *values.toTypedArray())

    /**
     * Fetch records that have <code>sync_at BETWEEN lowerInclusive AND upperInclusive</code>
     */
    fun fetchRangeOfSyncAtTable(lowerInclusive: LocalDateTime?, upperInclusive: LocalDateTime?): List<RealmEntity> = fetchRange(RealmTable.REALM.SYNC_AT, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>sync_at IN (values)</code>
     */
    fun fetchBySyncAtTable(vararg values: LocalDateTime): List<RealmEntity> = fetch(RealmTable.REALM.SYNC_AT, *values)
}
