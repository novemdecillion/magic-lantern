/*
 * Auto-generated file. Created by MyBatis Generator
 * Generation date: 2021-03-25T16:08:49.527508+09:00
 */
package io.github.novemdecillion.adapter.mybatis

import io.github.novemdecillion.adapter.mybatis.FlywaySchemaHistoryDynamicSqlSupport.FlywaySchemaHistory
import io.github.novemdecillion.adapter.mybatis.FlywaySchemaHistoryDynamicSqlSupport.FlywaySchemaHistory.checksum
import io.github.novemdecillion.adapter.mybatis.FlywaySchemaHistoryDynamicSqlSupport.FlywaySchemaHistory.description
import io.github.novemdecillion.adapter.mybatis.FlywaySchemaHistoryDynamicSqlSupport.FlywaySchemaHistory.executionTime
import io.github.novemdecillion.adapter.mybatis.FlywaySchemaHistoryDynamicSqlSupport.FlywaySchemaHistory.installedBy
import io.github.novemdecillion.adapter.mybatis.FlywaySchemaHistoryDynamicSqlSupport.FlywaySchemaHistory.installedOn
import io.github.novemdecillion.adapter.mybatis.FlywaySchemaHistoryDynamicSqlSupport.FlywaySchemaHistory.installedRank
import io.github.novemdecillion.adapter.mybatis.FlywaySchemaHistoryDynamicSqlSupport.FlywaySchemaHistory.script
import io.github.novemdecillion.adapter.mybatis.FlywaySchemaHistoryDynamicSqlSupport.FlywaySchemaHistory.success
import io.github.novemdecillion.adapter.mybatis.FlywaySchemaHistoryDynamicSqlSupport.FlywaySchemaHistory.type
import io.github.novemdecillion.adapter.mybatis.FlywaySchemaHistoryDynamicSqlSupport.FlywaySchemaHistory.version
import io.github.novemdecillion.adapter.mybatis.FlywaySchemaHistoryRecord
import org.mybatis.dynamic.sql.SqlBuilder.isEqualTo
import org.mybatis.dynamic.sql.util.kotlin.*
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.*

fun FlywaySchemaHistoryMapper.count(completer: CountCompleter) =
    countFrom(this::count, FlywaySchemaHistory, completer)

fun FlywaySchemaHistoryMapper.delete(completer: DeleteCompleter) =
    deleteFrom(this::delete, FlywaySchemaHistory, completer)

fun FlywaySchemaHistoryMapper.deleteByPrimaryKey(installedRank_: Int) =
    delete {
        where(installedRank, isEqualTo(installedRank_))
    }

fun FlywaySchemaHistoryMapper.insert(record: FlywaySchemaHistoryRecord) =
    insert(this::insert, record, FlywaySchemaHistory) {
        map(installedRank).toProperty("installedRank")
        map(version).toProperty("version")
        map(description).toProperty("description")
        map(type).toProperty("type")
        map(script).toProperty("script")
        map(checksum).toProperty("checksum")
        map(installedBy).toProperty("installedBy")
        map(installedOn).toProperty("installedOn")
        map(executionTime).toProperty("executionTime")
        map(success).toProperty("success")
    }

fun FlywaySchemaHistoryMapper.insertMultiple(records: Collection<FlywaySchemaHistoryRecord>) =
    insertMultiple(this::insertMultiple, records, FlywaySchemaHistory) {
        map(installedRank).toProperty("installedRank")
        map(version).toProperty("version")
        map(description).toProperty("description")
        map(type).toProperty("type")
        map(script).toProperty("script")
        map(checksum).toProperty("checksum")
        map(installedBy).toProperty("installedBy")
        map(installedOn).toProperty("installedOn")
        map(executionTime).toProperty("executionTime")
        map(success).toProperty("success")
    }

fun FlywaySchemaHistoryMapper.insertMultiple(vararg records: FlywaySchemaHistoryRecord) =
    insertMultiple(records.toList())

fun FlywaySchemaHistoryMapper.insertSelective(record: FlywaySchemaHistoryRecord) =
    insert(this::insert, record, FlywaySchemaHistory) {
        map(installedRank).toPropertyWhenPresent("installedRank", record::installedRank)
        map(version).toPropertyWhenPresent("version", record::version)
        map(description).toPropertyWhenPresent("description", record::description)
        map(type).toPropertyWhenPresent("type", record::type)
        map(script).toPropertyWhenPresent("script", record::script)
        map(checksum).toPropertyWhenPresent("checksum", record::checksum)
        map(installedBy).toPropertyWhenPresent("installedBy", record::installedBy)
        map(installedOn).toPropertyWhenPresent("installedOn", record::installedOn)
        map(executionTime).toPropertyWhenPresent("executionTime", record::executionTime)
        map(success).toPropertyWhenPresent("success", record::success)
    }

private val columnList = listOf(installedRank, version, description, type, script, checksum, installedBy, installedOn, executionTime, success)

fun FlywaySchemaHistoryMapper.selectOne(completer: SelectCompleter) =
    selectOne(this::selectOne, columnList, FlywaySchemaHistory, completer)

fun FlywaySchemaHistoryMapper.select(completer: SelectCompleter) =
    selectList(this::selectMany, columnList, FlywaySchemaHistory, completer)

fun FlywaySchemaHistoryMapper.selectDistinct(completer: SelectCompleter) =
    selectDistinct(this::selectMany, columnList, FlywaySchemaHistory, completer)

fun FlywaySchemaHistoryMapper.selectByPrimaryKey(installedRank_: Int) =
    selectOne {
        where(installedRank, isEqualTo(installedRank_))
    }

fun FlywaySchemaHistoryMapper.update(completer: UpdateCompleter) =
    update(this::update, FlywaySchemaHistory, completer)

fun KotlinUpdateBuilder.updateAllColumns(record: FlywaySchemaHistoryRecord) =
    apply {
        set(installedRank).equalTo(record::installedRank)
        set(version).equalTo(record::version)
        set(description).equalTo(record::description)
        set(type).equalTo(record::type)
        set(script).equalTo(record::script)
        set(checksum).equalTo(record::checksum)
        set(installedBy).equalTo(record::installedBy)
        set(installedOn).equalTo(record::installedOn)
        set(executionTime).equalTo(record::executionTime)
        set(success).equalTo(record::success)
    }

fun KotlinUpdateBuilder.updateSelectiveColumns(record: FlywaySchemaHistoryRecord) =
    apply {
        set(installedRank).equalToWhenPresent(record::installedRank)
        set(version).equalToWhenPresent(record::version)
        set(description).equalToWhenPresent(record::description)
        set(type).equalToWhenPresent(record::type)
        set(script).equalToWhenPresent(record::script)
        set(checksum).equalToWhenPresent(record::checksum)
        set(installedBy).equalToWhenPresent(record::installedBy)
        set(installedOn).equalToWhenPresent(record::installedOn)
        set(executionTime).equalToWhenPresent(record::executionTime)
        set(success).equalToWhenPresent(record::success)
    }

fun FlywaySchemaHistoryMapper.updateByPrimaryKey(record: FlywaySchemaHistoryRecord) =
    update {
        set(version).equalTo(record::version)
        set(description).equalTo(record::description)
        set(type).equalTo(record::type)
        set(script).equalTo(record::script)
        set(checksum).equalTo(record::checksum)
        set(installedBy).equalTo(record::installedBy)
        set(installedOn).equalTo(record::installedOn)
        set(executionTime).equalTo(record::executionTime)
        set(success).equalTo(record::success)
        where(installedRank, isEqualTo(record::installedRank))
    }

fun FlywaySchemaHistoryMapper.updateByPrimaryKeySelective(record: FlywaySchemaHistoryRecord) =
    update {
        set(version).equalToWhenPresent(record::version)
        set(description).equalToWhenPresent(record::description)
        set(type).equalToWhenPresent(record::type)
        set(script).equalToWhenPresent(record::script)
        set(checksum).equalToWhenPresent(record::checksum)
        set(installedBy).equalToWhenPresent(record::installedBy)
        set(installedOn).equalToWhenPresent(record::installedOn)
        set(executionTime).equalToWhenPresent(record::executionTime)
        set(success).equalToWhenPresent(record::success)
        where(installedRank, isEqualTo(record::installedRank))
    }