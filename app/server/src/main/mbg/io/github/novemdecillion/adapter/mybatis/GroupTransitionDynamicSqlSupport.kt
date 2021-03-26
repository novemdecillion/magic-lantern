/*
 * Auto-generated file. Created by MyBatis Generator
 * Generation date: 2021-03-25T16:08:49.52615+09:00
 */
package io.github.novemdecillion.adapter.mybatis

import java.lang.Object
import java.sql.JDBCType
import org.mybatis.dynamic.sql.SqlTable

object GroupTransitionDynamicSqlSupport {
    object GroupTransition : SqlTable("public.group_transition") {
        val groupTransitionId = column<Object>("group_transition_id", JDBCType.OTHER)

        val groupGenerationId = column<Object>("group_generation_id", JDBCType.OTHER)

        val groupOriginId = column<Object>("group_origin_id", JDBCType.OTHER)

        val groupName = column<String>("group_name", JDBCType.VARCHAR)
    }
}