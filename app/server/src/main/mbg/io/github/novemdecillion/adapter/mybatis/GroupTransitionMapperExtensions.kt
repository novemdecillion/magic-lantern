/*
 * Auto-generated file. Created by MyBatis Generator
 * Generation date: 2021-03-25T16:08:49.526476+09:00
 */
package io.github.novemdecillion.adapter.mybatis

import io.github.novemdecillion.adapter.mybatis.GroupTransitionDynamicSqlSupport.GroupTransition
import io.github.novemdecillion.adapter.mybatis.GroupTransitionDynamicSqlSupport.GroupTransition.groupGenerationId
import io.github.novemdecillion.adapter.mybatis.GroupTransitionDynamicSqlSupport.GroupTransition.groupName
import io.github.novemdecillion.adapter.mybatis.GroupTransitionDynamicSqlSupport.GroupTransition.groupOriginId
import io.github.novemdecillion.adapter.mybatis.GroupTransitionDynamicSqlSupport.GroupTransition.groupTransitionId
import io.github.novemdecillion.adapter.mybatis.GroupTransitionRecord
import java.lang.Object
import org.mybatis.dynamic.sql.SqlBuilder.isEqualTo
import org.mybatis.dynamic.sql.util.kotlin.*
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.*

fun GroupTransitionMapper.count(completer: CountCompleter) =
    countFrom(this::count, GroupTransition, completer)

fun GroupTransitionMapper.delete(completer: DeleteCompleter) =
    deleteFrom(this::delete, GroupTransition, completer)

fun GroupTransitionMapper.deleteByPrimaryKey(groupTransitionId_: Object) =
    delete {
        where(groupTransitionId, isEqualTo(groupTransitionId_))
    }

fun GroupTransitionMapper.insert(record: GroupTransitionRecord) =
    insert(this::insert, record, GroupTransition) {
        map(groupTransitionId).toProperty("groupTransitionId")
        map(groupGenerationId).toProperty("groupGenerationId")
        map(groupOriginId).toProperty("groupOriginId")
        map(groupName).toProperty("groupName")
    }

fun GroupTransitionMapper.insertMultiple(records: Collection<GroupTransitionRecord>) =
    insertMultiple(this::insertMultiple, records, GroupTransition) {
        map(groupTransitionId).toProperty("groupTransitionId")
        map(groupGenerationId).toProperty("groupGenerationId")
        map(groupOriginId).toProperty("groupOriginId")
        map(groupName).toProperty("groupName")
    }

fun GroupTransitionMapper.insertMultiple(vararg records: GroupTransitionRecord) =
    insertMultiple(records.toList())

fun GroupTransitionMapper.insertSelective(record: GroupTransitionRecord) =
    insert(this::insert, record, GroupTransition) {
        map(groupTransitionId).toPropertyWhenPresent("groupTransitionId", record::groupTransitionId)
        map(groupGenerationId).toPropertyWhenPresent("groupGenerationId", record::groupGenerationId)
        map(groupOriginId).toPropertyWhenPresent("groupOriginId", record::groupOriginId)
        map(groupName).toPropertyWhenPresent("groupName", record::groupName)
    }

private val columnList = listOf(groupTransitionId, groupGenerationId, groupOriginId, groupName)

fun GroupTransitionMapper.selectOne(completer: SelectCompleter) =
    selectOne(this::selectOne, columnList, GroupTransition, completer)

fun GroupTransitionMapper.select(completer: SelectCompleter) =
    selectList(this::selectMany, columnList, GroupTransition, completer)

fun GroupTransitionMapper.selectDistinct(completer: SelectCompleter) =
    selectDistinct(this::selectMany, columnList, GroupTransition, completer)

fun GroupTransitionMapper.selectByPrimaryKey(groupTransitionId_: Object) =
    selectOne {
        where(groupTransitionId, isEqualTo(groupTransitionId_))
    }

fun GroupTransitionMapper.update(completer: UpdateCompleter) =
    update(this::update, GroupTransition, completer)

fun KotlinUpdateBuilder.updateAllColumns(record: GroupTransitionRecord) =
    apply {
        set(groupTransitionId).equalTo(record::groupTransitionId)
        set(groupGenerationId).equalTo(record::groupGenerationId)
        set(groupOriginId).equalTo(record::groupOriginId)
        set(groupName).equalTo(record::groupName)
    }

fun KotlinUpdateBuilder.updateSelectiveColumns(record: GroupTransitionRecord) =
    apply {
        set(groupTransitionId).equalToWhenPresent(record::groupTransitionId)
        set(groupGenerationId).equalToWhenPresent(record::groupGenerationId)
        set(groupOriginId).equalToWhenPresent(record::groupOriginId)
        set(groupName).equalToWhenPresent(record::groupName)
    }

fun GroupTransitionMapper.updateByPrimaryKey(record: GroupTransitionRecord) =
    update {
        set(groupGenerationId).equalTo(record::groupGenerationId)
        set(groupOriginId).equalTo(record::groupOriginId)
        set(groupName).equalTo(record::groupName)
        where(groupTransitionId, isEqualTo(record::groupTransitionId))
    }

fun GroupTransitionMapper.updateByPrimaryKeySelective(record: GroupTransitionRecord) =
    update {
        set(groupGenerationId).equalToWhenPresent(record::groupGenerationId)
        set(groupOriginId).equalToWhenPresent(record::groupOriginId)
        set(groupName).equalToWhenPresent(record::groupName)
        where(groupTransitionId, isEqualTo(record::groupTransitionId))
    }