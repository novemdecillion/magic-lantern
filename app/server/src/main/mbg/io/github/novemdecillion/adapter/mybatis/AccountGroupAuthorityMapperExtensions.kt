/*
 * Auto-generated file. Created by MyBatis Generator
 * Generation date: 2021-03-25T16:08:49.524257+09:00
 */
package io.github.novemdecillion.adapter.mybatis

import io.github.novemdecillion.adapter.mybatis.AccountGroupAuthorityDynamicSqlSupport.AccountGroupAuthority
import io.github.novemdecillion.adapter.mybatis.AccountGroupAuthorityDynamicSqlSupport.AccountGroupAuthority.accountId
import io.github.novemdecillion.adapter.mybatis.AccountGroupAuthorityDynamicSqlSupport.AccountGroupAuthority.groupTransitionId
import io.github.novemdecillion.adapter.mybatis.AccountGroupAuthorityDynamicSqlSupport.AccountGroupAuthority.role
import io.github.novemdecillion.adapter.mybatis.AccountGroupAuthorityRecord
import java.lang.Object
import org.mybatis.dynamic.sql.SqlBuilder.isEqualTo
import org.mybatis.dynamic.sql.util.kotlin.*
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.*

fun AccountGroupAuthorityMapper.count(completer: CountCompleter) =
    countFrom(this::count, AccountGroupAuthority, completer)

fun AccountGroupAuthorityMapper.delete(completer: DeleteCompleter) =
    deleteFrom(this::delete, AccountGroupAuthority, completer)

fun AccountGroupAuthorityMapper.deleteByPrimaryKey(accountId_: Object, groupTransitionId_: Object) =
    delete {
        where(accountId, isEqualTo(accountId_))
        and(groupTransitionId, isEqualTo(groupTransitionId_))
    }

fun AccountGroupAuthorityMapper.insert(record: AccountGroupAuthorityRecord) =
    insert(this::insert, record, AccountGroupAuthority) {
        map(accountId).toProperty("accountId")
        map(groupTransitionId).toProperty("groupTransitionId")
        map(role).toProperty("role")
    }

fun AccountGroupAuthorityMapper.insertMultiple(records: Collection<AccountGroupAuthorityRecord>) =
    insertMultiple(this::insertMultiple, records, AccountGroupAuthority) {
        map(accountId).toProperty("accountId")
        map(groupTransitionId).toProperty("groupTransitionId")
        map(role).toProperty("role")
    }

fun AccountGroupAuthorityMapper.insertMultiple(vararg records: AccountGroupAuthorityRecord) =
    insertMultiple(records.toList())

fun AccountGroupAuthorityMapper.insertSelective(record: AccountGroupAuthorityRecord) =
    insert(this::insert, record, AccountGroupAuthority) {
        map(accountId).toPropertyWhenPresent("accountId", record::accountId)
        map(groupTransitionId).toPropertyWhenPresent("groupTransitionId", record::groupTransitionId)
        map(role).toPropertyWhenPresent("role", record::role)
    }

private val columnList = listOf(accountId, groupTransitionId, role)

fun AccountGroupAuthorityMapper.selectOne(completer: SelectCompleter) =
    selectOne(this::selectOne, columnList, AccountGroupAuthority, completer)

fun AccountGroupAuthorityMapper.select(completer: SelectCompleter) =
    selectList(this::selectMany, columnList, AccountGroupAuthority, completer)

fun AccountGroupAuthorityMapper.selectDistinct(completer: SelectCompleter) =
    selectDistinct(this::selectMany, columnList, AccountGroupAuthority, completer)

fun AccountGroupAuthorityMapper.selectByPrimaryKey(accountId_: Object, groupTransitionId_: Object) =
    selectOne {
        where(accountId, isEqualTo(accountId_))
        and(groupTransitionId, isEqualTo(groupTransitionId_))
    }

fun AccountGroupAuthorityMapper.update(completer: UpdateCompleter) =
    update(this::update, AccountGroupAuthority, completer)

fun KotlinUpdateBuilder.updateAllColumns(record: AccountGroupAuthorityRecord) =
    apply {
        set(accountId).equalTo(record::accountId)
        set(groupTransitionId).equalTo(record::groupTransitionId)
        set(role).equalTo(record::role)
    }

fun KotlinUpdateBuilder.updateSelectiveColumns(record: AccountGroupAuthorityRecord) =
    apply {
        set(accountId).equalToWhenPresent(record::accountId)
        set(groupTransitionId).equalToWhenPresent(record::groupTransitionId)
        set(role).equalToWhenPresent(record::role)
    }

fun AccountGroupAuthorityMapper.updateByPrimaryKey(record: AccountGroupAuthorityRecord) =
    update {
        set(role).equalTo(record::role)
        where(accountId, isEqualTo(record::accountId))
        and(groupTransitionId, isEqualTo(record::groupTransitionId))
    }

fun AccountGroupAuthorityMapper.updateByPrimaryKeySelective(record: AccountGroupAuthorityRecord) =
    update {
        set(role).equalToWhenPresent(record::role)
        where(accountId, isEqualTo(record::accountId))
        and(groupTransitionId, isEqualTo(record::groupTransitionId))
    }