/*
 * Auto-generated file. Created by MyBatis Generator
 * Generation date: 2021-03-25T16:08:49.528096+09:00
 */
package io.github.novemdecillion.adapter.mybatis

import io.github.novemdecillion.adapter.mybatis.GroupGenerationPeriodRecord
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
interface GroupGenerationPeriodMapper {
    @SelectProvider(type=SqlProviderAdapter::class, method="select")
    fun count(selectStatement: SelectStatementProvider): Long

    @DeleteProvider(type=SqlProviderAdapter::class, method="delete")
    fun delete(deleteStatement: DeleteStatementProvider): Int

    @InsertProvider(type=SqlProviderAdapter::class, method="insert")
    fun insert(insertStatement: InsertStatementProvider<GroupGenerationPeriodRecord>): Int

    @InsertProvider(type=SqlProviderAdapter::class, method="insertMultiple")
    fun insertMultiple(multipleInsertStatement: MultiRowInsertStatementProvider<GroupGenerationPeriodRecord>): Int

    @SelectProvider(type=SqlProviderAdapter::class, method="select")
    @ResultMap("GroupGenerationPeriodRecordResult")
    fun selectOne(selectStatement: SelectStatementProvider): GroupGenerationPeriodRecord?

    @SelectProvider(type=SqlProviderAdapter::class, method="select")
    @Results(id="GroupGenerationPeriodRecordResult", value = [
        Result(column="group_generation_id", property="groupGenerationId", jdbcType=JdbcType.OTHER),
        Result(column="start_date", property="startDate", jdbcType=JdbcType.DATE),
        Result(column="end_date", property="endDate", jdbcType=JdbcType.DATE)
    ])
    fun selectMany(selectStatement: SelectStatementProvider): List<GroupGenerationPeriodRecord>

    @UpdateProvider(type=SqlProviderAdapter::class, method="update")
    fun update(updateStatement: UpdateStatementProvider): Int
}