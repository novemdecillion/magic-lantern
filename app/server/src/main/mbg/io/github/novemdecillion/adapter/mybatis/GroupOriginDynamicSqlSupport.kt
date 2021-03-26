/*
 * Auto-generated file. Created by MyBatis Generator
 * Generation date: 2021-03-25T16:08:49.521197+09:00
 */
package io.github.novemdecillion.adapter.mybatis

import java.lang.Object
import java.sql.JDBCType
import org.mybatis.dynamic.sql.SqlTable

object GroupOriginDynamicSqlSupport {
    object GroupOrigin : SqlTable("public.group_origin") {
        val groupOriginId = column<Object>("group_origin_id", JDBCType.OTHER)
    }
}