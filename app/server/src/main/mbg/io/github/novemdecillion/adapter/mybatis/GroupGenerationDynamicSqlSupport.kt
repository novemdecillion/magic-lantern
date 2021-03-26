/*
 * Auto-generated file. Created by MyBatis Generator
 * Generation date: 2021-03-25T16:08:49.523069+09:00
 */
package io.github.novemdecillion.adapter.mybatis

import java.lang.Object
import java.sql.JDBCType
import java.time.LocalDate
import org.mybatis.dynamic.sql.SqlTable

object GroupGenerationDynamicSqlSupport {
    object GroupGeneration : SqlTable("public.group_generation") {
        val groupGenerationId = column<Object>("group_generation_id", JDBCType.OTHER)

        val startDate = column<LocalDate>("start_date", JDBCType.DATE)
    }
}