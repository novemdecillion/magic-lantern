/*
 * Auto-generated file. Created by MyBatis Generator
 * Generation date: 2021-03-25T16:08:49.521495+09:00
 */
package io.github.novemdecillion.adapter.mybatis

import io.github.novemdecillion.adapter.mybatis.GroupOriginDynamicSqlSupport.GroupOrigin
import io.github.novemdecillion.adapter.mybatis.GroupOriginDynamicSqlSupport.GroupOrigin.groupOriginId
import io.github.novemdecillion.adapter.mybatis.GroupOriginRecord
import java.lang.Object
import org.mybatis.dynamic.sql.SqlBuilder.isEqualTo
import org.mybatis.dynamic.sql.util.kotlin.*
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.*

fun GroupOriginMapper.count(completer: CountCompleter) =
    countFrom(this::count, GroupOrigin, completer)

fun GroupOriginMapper.delete(completer: DeleteCompleter) =
    deleteFrom(this::delete, GroupOrigin, completer)

fun GroupOriginMapper.deleteByPrimaryKey(groupOriginId_: Object) =
    delete {
        where(groupOriginId, isEqualTo(groupOriginId_))
    }

fun GroupOriginMapper.insert(record: GroupOriginRecord) =
    insert(this::insert, record, GroupOrigin) {
        map(groupOriginId).toProperty("groupOriginId")
    }

fun GroupOriginMapper.insertMultiple(records: Collection<GroupOriginRecord>) =
    insertMultiple(this::insertMultiple, records, GroupOrigin) {
        map(groupOriginId).toProperty("groupOriginId")
    }

fun GroupOriginMapper.insertMultiple(vararg records: GroupOriginRecord) =
    insertMultiple(records.toList())

fun GroupOriginMapper.insertSelective(record: GroupOriginRecord) =
    insert(this::insert, record, GroupOrigin) {
        map(groupOriginId).toPropertyWhenPresent("groupOriginId", record::groupOriginId)
    }

private val columnList = listOf(groupOriginId)

fun GroupOriginMapper.selectOne(completer: SelectCompleter) =
    selectOne(this::selectOne, columnList, GroupOrigin, completer)

fun GroupOriginMapper.select(completer: SelectCompleter) =
    selectList(this::selectMany, columnList, GroupOrigin, completer)

fun GroupOriginMapper.selectDistinct(completer: SelectCompleter) =
    selectDistinct(this::selectMany, columnList, GroupOrigin, completer)

fun GroupOriginMapper.update(completer: UpdateCompleter) =
    update(this::update, GroupOrigin, completer)

fun KotlinUpdateBuilder.updateAllColumns(record: GroupOriginRecord) =
    apply {
        set(groupOriginId).equalTo(record::groupOriginId)
    }

fun KotlinUpdateBuilder.updateSelectiveColumns(record: GroupOriginRecord) =
    apply {
        set(groupOriginId).equalToWhenPresent(record::groupOriginId)
    }