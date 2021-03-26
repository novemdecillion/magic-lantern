/*
 * Auto-generated file. Created by MyBatis Generator
 * Generation date: 2021-03-25T16:08:49.528235+09:00
 */
package io.github.novemdecillion.adapter.mybatis

import io.github.novemdecillion.adapter.mybatis.GroupGenerationPeriodDynamicSqlSupport.GroupGenerationPeriod
import io.github.novemdecillion.adapter.mybatis.GroupGenerationPeriodDynamicSqlSupport.GroupGenerationPeriod.endDate
import io.github.novemdecillion.adapter.mybatis.GroupGenerationPeriodDynamicSqlSupport.GroupGenerationPeriod.groupGenerationId
import io.github.novemdecillion.adapter.mybatis.GroupGenerationPeriodDynamicSqlSupport.GroupGenerationPeriod.startDate
import io.github.novemdecillion.adapter.mybatis.GroupGenerationPeriodRecord
import org.mybatis.dynamic.sql.util.kotlin.*
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.*

fun GroupGenerationPeriodMapper.count(completer: CountCompleter) =
    countFrom(this::count, GroupGenerationPeriod, completer)

fun GroupGenerationPeriodMapper.delete(completer: DeleteCompleter) =
    deleteFrom(this::delete, GroupGenerationPeriod, completer)

fun GroupGenerationPeriodMapper.insert(record: GroupGenerationPeriodRecord) =
    insert(this::insert, record, GroupGenerationPeriod) {
        map(groupGenerationId).toProperty("groupGenerationId")
        map(startDate).toProperty("startDate")
        map(endDate).toProperty("endDate")
    }

fun GroupGenerationPeriodMapper.insertMultiple(records: Collection<GroupGenerationPeriodRecord>) =
    insertMultiple(this::insertMultiple, records, GroupGenerationPeriod) {
        map(groupGenerationId).toProperty("groupGenerationId")
        map(startDate).toProperty("startDate")
        map(endDate).toProperty("endDate")
    }

fun GroupGenerationPeriodMapper.insertMultiple(vararg records: GroupGenerationPeriodRecord) =
    insertMultiple(records.toList())

fun GroupGenerationPeriodMapper.insertSelective(record: GroupGenerationPeriodRecord) =
    insert(this::insert, record, GroupGenerationPeriod) {
        map(groupGenerationId).toPropertyWhenPresent("groupGenerationId", record::groupGenerationId)
        map(startDate).toPropertyWhenPresent("startDate", record::startDate)
        map(endDate).toPropertyWhenPresent("endDate", record::endDate)
    }

private val columnList = listOf(groupGenerationId, startDate, endDate)

fun GroupGenerationPeriodMapper.selectOne(completer: SelectCompleter) =
    selectOne(this::selectOne, columnList, GroupGenerationPeriod, completer)

fun GroupGenerationPeriodMapper.select(completer: SelectCompleter) =
    selectList(this::selectMany, columnList, GroupGenerationPeriod, completer)

fun GroupGenerationPeriodMapper.selectDistinct(completer: SelectCompleter) =
    selectDistinct(this::selectMany, columnList, GroupGenerationPeriod, completer)

fun GroupGenerationPeriodMapper.update(completer: UpdateCompleter) =
    update(this::update, GroupGenerationPeriod, completer)

fun KotlinUpdateBuilder.updateAllColumns(record: GroupGenerationPeriodRecord) =
    apply {
        set(groupGenerationId).equalTo(record::groupGenerationId)
        set(startDate).equalTo(record::startDate)
        set(endDate).equalTo(record::endDate)
    }

fun KotlinUpdateBuilder.updateSelectiveColumns(record: GroupGenerationPeriodRecord) =
    apply {
        set(groupGenerationId).equalToWhenPresent(record::groupGenerationId)
        set(startDate).equalToWhenPresent(record::startDate)
        set(endDate).equalToWhenPresent(record::endDate)
    }