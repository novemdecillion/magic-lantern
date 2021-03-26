/*
 * Auto-generated file. Created by MyBatis Generator
 * Generation date: 2021-03-25T16:08:49.523344+09:00
 */
package io.github.novemdecillion.adapter.mybatis

import io.github.novemdecillion.adapter.mybatis.GroupGenerationDynamicSqlSupport.GroupGeneration
import io.github.novemdecillion.adapter.mybatis.GroupGenerationDynamicSqlSupport.GroupGeneration.groupGenerationId
import io.github.novemdecillion.adapter.mybatis.GroupGenerationDynamicSqlSupport.GroupGeneration.startDate
import io.github.novemdecillion.adapter.mybatis.GroupGenerationRecord
import java.lang.Object
import org.mybatis.dynamic.sql.SqlBuilder.isEqualTo
import org.mybatis.dynamic.sql.util.kotlin.*
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.*

fun GroupGenerationMapper.count(completer: CountCompleter) =
    countFrom(this::count, GroupGeneration, completer)

fun GroupGenerationMapper.delete(completer: DeleteCompleter) =
    deleteFrom(this::delete, GroupGeneration, completer)

fun GroupGenerationMapper.deleteByPrimaryKey(groupGenerationId_: Object) =
    delete {
        where(groupGenerationId, isEqualTo(groupGenerationId_))
    }

fun GroupGenerationMapper.insert(record: GroupGenerationRecord) =
    insert(this::insert, record, GroupGeneration) {
        map(groupGenerationId).toProperty("groupGenerationId")
        map(startDate).toProperty("startDate")
    }

fun GroupGenerationMapper.insertMultiple(records: Collection<GroupGenerationRecord>) =
    insertMultiple(this::insertMultiple, records, GroupGeneration) {
        map(groupGenerationId).toProperty("groupGenerationId")
        map(startDate).toProperty("startDate")
    }

fun GroupGenerationMapper.insertMultiple(vararg records: GroupGenerationRecord) =
    insertMultiple(records.toList())

fun GroupGenerationMapper.insertSelective(record: GroupGenerationRecord) =
    insert(this::insert, record, GroupGeneration) {
        map(groupGenerationId).toPropertyWhenPresent("groupGenerationId", record::groupGenerationId)
        map(startDate).toPropertyWhenPresent("startDate", record::startDate)
    }

private val columnList = listOf(groupGenerationId, startDate)

fun GroupGenerationMapper.selectOne(completer: SelectCompleter) =
    selectOne(this::selectOne, columnList, GroupGeneration, completer)

fun GroupGenerationMapper.select(completer: SelectCompleter) =
    selectList(this::selectMany, columnList, GroupGeneration, completer)

fun GroupGenerationMapper.selectDistinct(completer: SelectCompleter) =
    selectDistinct(this::selectMany, columnList, GroupGeneration, completer)

fun GroupGenerationMapper.selectByPrimaryKey(groupGenerationId_: Object) =
    selectOne {
        where(groupGenerationId, isEqualTo(groupGenerationId_))
    }

fun GroupGenerationMapper.update(completer: UpdateCompleter) =
    update(this::update, GroupGeneration, completer)

fun KotlinUpdateBuilder.updateAllColumns(record: GroupGenerationRecord) =
    apply {
        set(groupGenerationId).equalTo(record::groupGenerationId)
        set(startDate).equalTo(record::startDate)
    }

fun KotlinUpdateBuilder.updateSelectiveColumns(record: GroupGenerationRecord) =
    apply {
        set(groupGenerationId).equalToWhenPresent(record::groupGenerationId)
        set(startDate).equalToWhenPresent(record::startDate)
    }

fun GroupGenerationMapper.updateByPrimaryKey(record: GroupGenerationRecord) =
    update {
        set(startDate).equalTo(record::startDate)
        where(groupGenerationId, isEqualTo(record::groupGenerationId))
    }

fun GroupGenerationMapper.updateByPrimaryKeySelective(record: GroupGenerationRecord) =
    update {
        set(startDate).equalToWhenPresent(record::startDate)
        where(groupGenerationId, isEqualTo(record::groupGenerationId))
    }