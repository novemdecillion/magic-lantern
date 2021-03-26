/*
 * Auto-generated file. Created by MyBatis Generator
 * Generation date: 2021-03-25T16:08:49.52552+09:00
 */
package io.github.novemdecillion.adapter.mybatis

import io.github.novemdecillion.adapter.mybatis.CurrentGroupTransitionDynamicSqlSupport.CurrentGroupTransition
import io.github.novemdecillion.adapter.mybatis.CurrentGroupTransitionDynamicSqlSupport.CurrentGroupTransition.groupGenerationId
import io.github.novemdecillion.adapter.mybatis.CurrentGroupTransitionDynamicSqlSupport.CurrentGroupTransition.groupName
import io.github.novemdecillion.adapter.mybatis.CurrentGroupTransitionDynamicSqlSupport.CurrentGroupTransition.groupOriginId
import io.github.novemdecillion.adapter.mybatis.CurrentGroupTransitionDynamicSqlSupport.CurrentGroupTransition.groupTransitionId
import io.github.novemdecillion.adapter.mybatis.CurrentGroupTransitionRecord
import org.mybatis.dynamic.sql.util.kotlin.*
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.*

fun CurrentGroupTransitionMapper.count(completer: CountCompleter) =
    countFrom(this::count, CurrentGroupTransition, completer)

fun CurrentGroupTransitionMapper.delete(completer: DeleteCompleter) =
    deleteFrom(this::delete, CurrentGroupTransition, completer)

fun CurrentGroupTransitionMapper.insert(record: CurrentGroupTransitionRecord) =
    insert(this::insert, record, CurrentGroupTransition) {
        map(groupTransitionId).toProperty("groupTransitionId")
        map(groupGenerationId).toProperty("groupGenerationId")
        map(groupOriginId).toProperty("groupOriginId")
        map(groupName).toProperty("groupName")
    }

fun CurrentGroupTransitionMapper.insertMultiple(records: Collection<CurrentGroupTransitionRecord>) =
    insertMultiple(this::insertMultiple, records, CurrentGroupTransition) {
        map(groupTransitionId).toProperty("groupTransitionId")
        map(groupGenerationId).toProperty("groupGenerationId")
        map(groupOriginId).toProperty("groupOriginId")
        map(groupName).toProperty("groupName")
    }

fun CurrentGroupTransitionMapper.insertMultiple(vararg records: CurrentGroupTransitionRecord) =
    insertMultiple(records.toList())

fun CurrentGroupTransitionMapper.insertSelective(record: CurrentGroupTransitionRecord) =
    insert(this::insert, record, CurrentGroupTransition) {
        map(groupTransitionId).toPropertyWhenPresent("groupTransitionId", record::groupTransitionId)
        map(groupGenerationId).toPropertyWhenPresent("groupGenerationId", record::groupGenerationId)
        map(groupOriginId).toPropertyWhenPresent("groupOriginId", record::groupOriginId)
        map(groupName).toPropertyWhenPresent("groupName", record::groupName)
    }

private val columnList = listOf(groupTransitionId, groupGenerationId, groupOriginId, groupName)

fun CurrentGroupTransitionMapper.selectOne(completer: SelectCompleter) =
    selectOne(this::selectOne, columnList, CurrentGroupTransition, completer)

fun CurrentGroupTransitionMapper.select(completer: SelectCompleter) =
    selectList(this::selectMany, columnList, CurrentGroupTransition, completer)

fun CurrentGroupTransitionMapper.selectDistinct(completer: SelectCompleter) =
    selectDistinct(this::selectMany, columnList, CurrentGroupTransition, completer)

fun CurrentGroupTransitionMapper.update(completer: UpdateCompleter) =
    update(this::update, CurrentGroupTransition, completer)

fun KotlinUpdateBuilder.updateAllColumns(record: CurrentGroupTransitionRecord) =
    apply {
        set(groupTransitionId).equalTo(record::groupTransitionId)
        set(groupGenerationId).equalTo(record::groupGenerationId)
        set(groupOriginId).equalTo(record::groupOriginId)
        set(groupName).equalTo(record::groupName)
    }

fun KotlinUpdateBuilder.updateSelectiveColumns(record: CurrentGroupTransitionRecord) =
    apply {
        set(groupTransitionId).equalToWhenPresent(record::groupTransitionId)
        set(groupGenerationId).equalToWhenPresent(record::groupGenerationId)
        set(groupOriginId).equalToWhenPresent(record::groupOriginId)
        set(groupName).equalToWhenPresent(record::groupName)
    }