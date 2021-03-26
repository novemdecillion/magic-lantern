/*
 * Auto-generated file. Created by MyBatis Generator
 * Generation date: 2021-03-25T16:08:49.528741+09:00
 */
package io.github.novemdecillion.adapter.mybatis

import java.lang.Object
import java.sql.JDBCType
import org.mybatis.dynamic.sql.SqlTable

object AccountDynamicSqlSupport {
    object Account : SqlTable("public.account") {
        val accountId = column<Object>("account_id", JDBCType.OTHER)

        val username = column<String>("username", JDBCType.VARCHAR)

        val password = column<String>("password", JDBCType.VARCHAR)

        val givenName = column<String>("given_name", JDBCType.VARCHAR)

        val familyName = column<String>("family_name", JDBCType.VARCHAR)

        val eastern = column<Boolean>("eastern", JDBCType.BIT)

        val email = column<String>("email", JDBCType.VARCHAR)

        val realm = column<String>("realm", JDBCType.VARCHAR)

        val enabled = column<Boolean>("enabled", JDBCType.BIT)
    }
}