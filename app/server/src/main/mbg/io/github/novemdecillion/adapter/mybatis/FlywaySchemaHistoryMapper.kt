/*
 * Auto-generated file. Created by MyBatis Generator
 * Generation date: 2021-03-25T16:08:49.527356+09:00
 */
package io.github.novemdecillion.adapter.mybatis

import io.github.novemdecillion.adapter.mybatis.FlywaySchemaHistoryRecord
import org.apache.ibatis.annotations.DeleteProvider
import org.apache.ibatis.annotations.InsertProvider
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.ResultMap
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.SelectProvider
import org.apache.ibatis.annotations.UpdateProvider
import org.apache.ibatis.type.JdbcType
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider
import org.mybatis.dynamic.sql.insert.render.MultiRowInsertStatementProvider
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider
import org.mybatis.dynamic.sql.util.SqlProviderAdapter

@Mapper
interface FlywaySchemaHistoryMapper {
    @SelectProvider(type=SqlProviderAdapter::class, method="select")
    fun count(selectStatement: SelectStatementProvider): Long

    @DeleteProvider(type=SqlProviderAdapter::class, method="delete")
    fun delete(deleteStatement: DeleteStatementProvider): Int

    @InsertProvider(type=SqlProviderAdapter::class, method="insert")
    fun insert(insertStatement: InsertStatementProvider<FlywaySchemaHistoryRecord>): Int

    @InsertProvider(type=SqlProviderAdapter::class, method="insertMultiple")
    fun insertMultiple(multipleInsertStatement: MultiRowInsertStatementProvider<FlywaySchemaHistoryRecord>): Int

    @SelectProvider(type=SqlProviderAdapter::class, method="select")
    @ResultMap("FlywaySchemaHistoryRecordResult")
    fun selectOne(selectStatement: SelectStatementProvider): FlywaySchemaHistoryRecord?

    @SelectProvider(type=SqlProviderAdapter::class, method="select")
    @Results(id="FlywaySchemaHistoryRecordResult", value = [
        Result(column="installed_rank", property="installedRank", jdbcType=JdbcType.INTEGER, id=true),
        Result(column="version", property="version", jdbcType=JdbcType.VARCHAR),
        Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        Result(column="type", property="type", jdbcType=JdbcType.VARCHAR),
        Result(column="script", property="script", jdbcType=JdbcType.VARCHAR),
        Result(column="checksum", property="checksum", jdbcType=JdbcType.INTEGER),
        Result(column="installed_by", property="installedBy", jdbcType=JdbcType.VARCHAR),
        Result(column="installed_on", property="installedOn", jdbcType=JdbcType.TIMESTAMP),
        Result(column="execution_time", property="executionTime", jdbcType=JdbcType.INTEGER),
        Result(column="success", property="success", jdbcType=JdbcType.BIT)
    ])
    fun selectMany(selectStatement: SelectStatementProvider): List<FlywaySchemaHistoryRecord>

    @UpdateProvider(type=SqlProviderAdapter::class, method="update")
    fun update(updateStatement: UpdateStatementProvider): Int
}