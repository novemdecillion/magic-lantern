/*
 * Auto-generated file. Created by MyBatis Generator
 * Generation date: 2021-03-25T16:08:49.522393+09:00
 */
package io.github.novemdecillion.adapter.mybatis

import io.github.novemdecillion.adapter.mybatis.UserAggregateRecord
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
interface UserAggregateMapper {
    @SelectProvider(type=SqlProviderAdapter::class, method="select")
    fun count(selectStatement: SelectStatementProvider): Long

    @DeleteProvider(type=SqlProviderAdapter::class, method="delete")
    fun delete(deleteStatement: DeleteStatementProvider): Int

    @InsertProvider(type=SqlProviderAdapter::class, method="insert")
    fun insert(insertStatement: InsertStatementProvider<UserAggregateRecord>): Int

    @InsertProvider(type=SqlProviderAdapter::class, method="insertMultiple")
    fun insertMultiple(multipleInsertStatement: MultiRowInsertStatementProvider<UserAggregateRecord>): Int

    @SelectProvider(type=SqlProviderAdapter::class, method="select")
    @ResultMap("UserAggregateRecordResult")
    fun selectOne(selectStatement: SelectStatementProvider): UserAggregateRecord?

    @SelectProvider(type=SqlProviderAdapter::class, method="select")
    @Results(id="UserAggregateRecordResult", value = [
        Result(column="account_id", property="accountId", jdbcType=JdbcType.OTHER),
        Result(column="username", property="username", jdbcType=JdbcType.VARCHAR),
        Result(column="password", property="password", jdbcType=JdbcType.VARCHAR),
        Result(column="given_name", property="givenName", jdbcType=JdbcType.VARCHAR),
        Result(column="family_name", property="familyName", jdbcType=JdbcType.VARCHAR),
        Result(column="eastern", property="eastern", jdbcType=JdbcType.BIT),
        Result(column="email", property="email", jdbcType=JdbcType.VARCHAR),
        Result(column="realm", property="realm", jdbcType=JdbcType.VARCHAR),
        Result(column="enabled", property="enabled", jdbcType=JdbcType.BIT),
        Result(column="group_origin_id", property="groupOriginId", jdbcType=JdbcType.OTHER),
        Result(column="role", property="role", jdbcType=JdbcType.VARCHAR)
    ])
    fun selectMany(selectStatement: SelectStatementProvider): List<UserAggregateRecord>

    @UpdateProvider(type=SqlProviderAdapter::class, method="update")
    fun update(updateStatement: UpdateStatementProvider): Int
}