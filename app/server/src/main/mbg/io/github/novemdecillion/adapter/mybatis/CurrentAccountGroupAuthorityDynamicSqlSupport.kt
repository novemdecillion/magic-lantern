/*
 * Auto-generated file. Created by MyBatis Generator
 * Generation date: 2021-03-25T16:08:49.52013+09:00
 */
package io.github.novemdecillion.adapter.mybatis

import java.lang.Object
import java.sql.JDBCType
import org.mybatis.dynamic.sql.SqlTable

object CurrentAccountGroupAuthorityDynamicSqlSupport {
    object CurrentAccountGroupAuthority : SqlTable("public.current_account_group_authority") {
        val accountId = column<Object>("account_id", JDBCType.OTHER)

        val groupTransitionId = column<Object>("group_transition_id", JDBCType.OTHER)

        val role = column<String>("role", JDBCType.VARCHAR)

        val groupOriginId = column<Object>("group_origin_id", JDBCType.OTHER)
    }
}