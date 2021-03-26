/*
 * Auto-generated file. Created by MyBatis Generator
 * Generation date: 2021-03-25T16:08:49.527076+09:00
 */
package io.github.novemdecillion.adapter.mybatis

import java.sql.JDBCType
import java.time.LocalDateTime
import org.mybatis.dynamic.sql.SqlTable

object FlywaySchemaHistoryDynamicSqlSupport {
    object FlywaySchemaHistory : SqlTable("public.flyway_schema_history") {
        val installedRank = column<Int>("installed_rank", JDBCType.INTEGER)

        val version = column<String>("version", JDBCType.VARCHAR)

        val description = column<String>("description", JDBCType.VARCHAR)

        val type = column<String>("type", JDBCType.VARCHAR)

        val script = column<String>("script", JDBCType.VARCHAR)

        val checksum = column<Int>("checksum", JDBCType.INTEGER)

        val installedBy = column<String>("installed_by", JDBCType.VARCHAR)

        val installedOn = column<LocalDateTime>("installed_on", JDBCType.TIMESTAMP)

        val executionTime = column<Int>("execution_time", JDBCType.INTEGER)

        val success = column<Boolean>("success", JDBCType.BIT)
    }
}