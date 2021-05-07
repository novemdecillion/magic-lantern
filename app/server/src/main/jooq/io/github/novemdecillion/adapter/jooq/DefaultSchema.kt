/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.adapter.jooq


import io.github.novemdecillion.adapter.jooq.tables.AccountGroupAuthorityTable
import io.github.novemdecillion.adapter.jooq.tables.AccountTable
import io.github.novemdecillion.adapter.jooq.tables.CurrentAccountGroupAuthorityTable
import io.github.novemdecillion.adapter.jooq.tables.CurrentGroupTransitionTable
import io.github.novemdecillion.adapter.jooq.tables.FlywaySchemaHistoryTable
import io.github.novemdecillion.adapter.jooq.tables.GroupGenerationPeriodTable
import io.github.novemdecillion.adapter.jooq.tables.GroupGenerationTable
import io.github.novemdecillion.adapter.jooq.tables.GroupOriginTable
import io.github.novemdecillion.adapter.jooq.tables.GroupTransitionTable
import io.github.novemdecillion.adapter.jooq.tables.LessonTable
import io.github.novemdecillion.adapter.jooq.tables.RealmTable
import io.github.novemdecillion.adapter.jooq.tables.SlideTable
import io.github.novemdecillion.adapter.jooq.tables.UserAggregateTable

import kotlin.collections.List

import org.jooq.Catalog
import org.jooq.Table
import org.jooq.impl.SchemaImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class DefaultSchema : SchemaImpl("", DefaultCatalog.DEFAULT_CATALOG) {
    companion object {

        /**
         * The reference instance of <code>DEFAULT_SCHEMA</code>
         */
        val DEFAULT_SCHEMA = DefaultSchema()
    }

    /**
     * The table <code>account</code>.
     */
    val ACCOUNT get() = AccountTable.ACCOUNT

    /**
     * The table <code>account_group_authority</code>.
     */
    val ACCOUNT_GROUP_AUTHORITY get() = AccountGroupAuthorityTable.ACCOUNT_GROUP_AUTHORITY

    /**
     * The table <code>current_account_group_authority</code>.
     */
    val CURRENT_ACCOUNT_GROUP_AUTHORITY get() = CurrentAccountGroupAuthorityTable.CURRENT_ACCOUNT_GROUP_AUTHORITY

    /**
     * The table <code>current_group_transition</code>.
     */
    val CURRENT_GROUP_TRANSITION get() = CurrentGroupTransitionTable.CURRENT_GROUP_TRANSITION

    /**
     * The table <code>flyway_schema_history</code>.
     */
    val FLYWAY_SCHEMA_HISTORY get() = FlywaySchemaHistoryTable.FLYWAY_SCHEMA_HISTORY

    /**
     * The table <code>group_generation</code>.
     */
    val GROUP_GENERATION get() = GroupGenerationTable.GROUP_GENERATION

    /**
     * The table <code>group_generation_period</code>.
     */
    val GROUP_GENERATION_PERIOD get() = GroupGenerationPeriodTable.GROUP_GENERATION_PERIOD

    /**
     * The table <code>group_origin</code>.
     */
    val GROUP_ORIGIN get() = GroupOriginTable.GROUP_ORIGIN

    /**
     * The table <code>group_transition</code>.
     */
    val GROUP_TRANSITION get() = GroupTransitionTable.GROUP_TRANSITION

    /**
     * The table <code>lesson</code>.
     */
    val LESSON get() = LessonTable.LESSON

    /**
     * The table <code>realm</code>.
     */
    val REALM get() = RealmTable.REALM

    /**
     * The table <code>slide</code>.
     */
    val SLIDE get() = SlideTable.SLIDE

    /**
     * The table <code>user_aggregate</code>.
     */
    val USER_AGGREGATE get() = UserAggregateTable.USER_AGGREGATE

    override fun getCatalog(): Catalog = DefaultCatalog.DEFAULT_CATALOG

    override fun getTables(): List<Table<*>> = listOf(
        AccountTable.ACCOUNT,
        AccountGroupAuthorityTable.ACCOUNT_GROUP_AUTHORITY,
        CurrentAccountGroupAuthorityTable.CURRENT_ACCOUNT_GROUP_AUTHORITY,
        CurrentGroupTransitionTable.CURRENT_GROUP_TRANSITION,
        FlywaySchemaHistoryTable.FLYWAY_SCHEMA_HISTORY,
        GroupGenerationTable.GROUP_GENERATION,
        GroupGenerationPeriodTable.GROUP_GENERATION_PERIOD,
        GroupOriginTable.GROUP_ORIGIN,
        GroupTransitionTable.GROUP_TRANSITION,
        LessonTable.LESSON,
        RealmTable.REALM,
        SlideTable.SLIDE,
        UserAggregateTable.USER_AGGREGATE
    )
}
