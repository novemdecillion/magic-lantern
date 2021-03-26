/*
 * Auto-generated file. Created by MyBatis Generator
 * Generation date: 2021-03-25T16:08:49.520672+09:00
 */
package io.github.novemdecillion.adapter.mybatis

import io.github.novemdecillion.adapter.mybatis.CurrentAccountGroupAuthorityDynamicSqlSupport.CurrentAccountGroupAuthority
import io.github.novemdecillion.adapter.mybatis.CurrentAccountGroupAuthorityDynamicSqlSupport.CurrentAccountGroupAuthority.accountId
import io.github.novemdecillion.adapter.mybatis.CurrentAccountGroupAuthorityDynamicSqlSupport.CurrentAccountGroupAuthority.groupOriginId
import io.github.novemdecillion.adapter.mybatis.CurrentAccountGroupAuthorityDynamicSqlSupport.CurrentAccountGroupAuthority.groupTransitionId
import io.github.novemdecillion.adapter.mybatis.CurrentAccountGroupAuthorityDynamicSqlSupport.CurrentAccountGroupAuthority.role
import io.github.novemdecillion.adapter.mybatis.CurrentAccountGroupAuthorityRecord
import org.mybatis.dynamic.sql.util.kotlin.*
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.*

fun CurrentAccountGroupAuthorityMapper.count(completer: CountCompleter) =
    countFrom(this::count, CurrentAccountGroupAuthority, completer)

fun CurrentAccountGroupAuthorityMapper.delete(completer: DeleteCompleter) =
    deleteFrom(this::delete, CurrentAccountGroupAuthority, completer)

fun CurrentAccountGroupAuthorityMapper.insert(record: CurrentAccountGroupAuthorityRecord) =
    insert(this::insert, record, CurrentAccountGroupAuthority) {
        map(accountId).toProperty("accountId")
        map(groupTransitionId).toProperty("groupTransitionId")
        map(role).toProperty("role")
        map(groupOriginId).toProperty("groupOriginId")
    }

fun CurrentAccountGroupAuthorityMapper.insertMultiple(records: Collection<CurrentAccountGroupAuthorityRecord>) =
    insertMultiple(this::insertMultiple, records, CurrentAccountGroupAuthority) {
        map(accountId).toProperty("accountId")
        map(groupTransitionId).toProperty("groupTransitionId")
        map(role).toProperty("role")
        map(groupOriginId).toProperty("groupOriginId")
    }

fun CurrentAccountGroupAuthorityMapper.insertMultiple(vararg records: CurrentAccountGroupAuthorityRecord) =
    insertMultiple(records.toList())

fun CurrentAccountGroupAuthorityMapper.insertSelective(record: CurrentAccountGroupAuthorityRecord) =
    insert(this::insert, record, CurrentAccountGroupAuthority) {
        map(accountId).toPropertyWhenPresent("accountId", record::accountId)
        map(groupTransitionId).toPropertyWhenPresent("groupTransitionId", record::groupTransitionId)
        map(role).toPropertyWhenPresent("role", record::role)
        map(groupOriginId).toPropertyWhenPresent("groupOriginId", record::groupOriginId)
    }

private val columnList = listOf(accountId, groupTransitionId, role, groupOriginId)

fun CurrentAccountGroupAuthorityMapper.selectOne(completer: SelectCompleter) =
    selectOne(this::selectOne, columnList, CurrentAccountGroupAuthority, completer)

fun CurrentAccountGroupAuthorityMapper.select(completer: SelectCompleter) =
    selectList(this::selectMany, columnList, CurrentAccountGroupAuthority, completer)

fun CurrentAccountGroupAuthorityMapper.selectDistinct(completer: SelectCompleter) =
    selectDistinct(this::selectMany, columnList, CurrentAccountGroupAuthority, completer)

fun CurrentAccountGroupAuthorityMapper.update(completer: UpdateCompleter) =
    update(this::update, CurrentAccountGroupAuthority, completer)

fun KotlinUpdateBuilder.updateAllColumns(record: CurrentAccountGroupAuthorityRecord) =
    apply {
        set(accountId).equalTo(record::accountId)
        set(groupTransitionId).equalTo(record::groupTransitionId)
        set(role).equalTo(record::role)
        set(groupOriginId).equalTo(record::groupOriginId)
    }

fun KotlinUpdateBuilder.updateSelectiveColumns(record: CurrentAccountGroupAuthorityRecord) =
    apply {
        set(accountId).equalToWhenPresent(record::accountId)
        set(groupTransitionId).equalToWhenPresent(record::groupTransitionId)
        set(role).equalToWhenPresent(record::role)
        set(groupOriginId).equalToWhenPresent(record::groupOriginId)
    }