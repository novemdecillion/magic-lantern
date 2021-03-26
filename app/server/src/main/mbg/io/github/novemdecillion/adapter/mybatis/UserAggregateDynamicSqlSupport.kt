/*
 * Auto-generated file. Created by MyBatis Generator
 * Generation date: 2021-03-25T16:08:49.522028+09:00
 */
package io.github.novemdecillion.adapter.mybatis

import java.lang.Object
import java.sql.JDBCType
import org.mybatis.dynamic.sql.SqlTable

object UserAggregateDynamicSqlSupport {
    object UserAggregate : SqlTable("public.user_aggregate") {
        val accountId = column<Object>("account_id", JDBCType.OTHER)

        val username = column<String>("username", JDBCType.VARCHAR)

        val password = column<String>("password", JDBCType.VARCHAR)

        val givenName = column<String>("given_name", JDBCType.VARCHAR)

        val familyName = column<String>("family_name", JDBCType.VARCHAR)

        val eastern = column<Boolean>("eastern", JDBCType.BIT)

        val email = column<String>("email", JDBCType.VARCHAR)

        val realm = column<String>("realm", JDBCType.VARCHAR)

        val enabled = column<Boolean>("enabled", JDBCType.BIT)

        val groupOriginId = column<Object>("group_origin_id", JDBCType.OTHER)

        val role = column<String>("role", JDBCType.VARCHAR)
    }
}