/*
 * Auto-generated file. Created by MyBatis Generator
 * Generation date: 2021-03-23T16:18:07.204227+09:00
 */
package io.github.novemdecillion.adapter.mybatis

import java.sql.JDBCType
import java.time.LocalDateTime
import org.mybatis.dynamic.sql.SqlTable

object TopicDynamicSqlSupport {
    object Topic : SqlTable("public.topic") {
        val topicId = column<Long>("topic_id", JDBCType.BIGINT)

        val createAt = column<LocalDateTime>("create_at", JDBCType.TIMESTAMP)

        val updateAt = column<LocalDateTime>("update_at", JDBCType.TIMESTAMP)

        val title = column<String>("title", JDBCType.VARCHAR)

        val filename = column<String>("filename", JDBCType.VARCHAR)
    }
}