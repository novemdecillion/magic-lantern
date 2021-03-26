/*
 * Auto-generated file. Created by MyBatis Generator
 * Generation date: 2021-03-25T16:08:49.529197+09:00
 */
package io.github.novemdecillion.adapter.mybatis

import io.github.novemdecillion.adapter.mybatis.AccountDynamicSqlSupport.Account
import io.github.novemdecillion.adapter.mybatis.AccountDynamicSqlSupport.Account.accountId
import io.github.novemdecillion.adapter.mybatis.AccountDynamicSqlSupport.Account.eastern
import io.github.novemdecillion.adapter.mybatis.AccountDynamicSqlSupport.Account.email
import io.github.novemdecillion.adapter.mybatis.AccountDynamicSqlSupport.Account.enabled
import io.github.novemdecillion.adapter.mybatis.AccountDynamicSqlSupport.Account.familyName
import io.github.novemdecillion.adapter.mybatis.AccountDynamicSqlSupport.Account.givenName
import io.github.novemdecillion.adapter.mybatis.AccountDynamicSqlSupport.Account.password
import io.github.novemdecillion.adapter.mybatis.AccountDynamicSqlSupport.Account.realm
import io.github.novemdecillion.adapter.mybatis.AccountDynamicSqlSupport.Account.username
import io.github.novemdecillion.adapter.mybatis.AccountRecord
import java.lang.Object
import org.mybatis.dynamic.sql.SqlBuilder.isEqualTo
import org.mybatis.dynamic.sql.util.kotlin.*
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.*

fun AccountMapper.count(completer: CountCompleter) =
    countFrom(this::count, Account, completer)

fun AccountMapper.delete(completer: DeleteCompleter) =
    deleteFrom(this::delete, Account, completer)

fun AccountMapper.deleteByPrimaryKey(accountId_: Object) =
    delete {
        where(accountId, isEqualTo(accountId_))
    }

fun AccountMapper.insert(record: AccountRecord) =
    insert(this::insert, record, Account) {
        map(accountId).toProperty("accountId")
        map(username).toProperty("username")
        map(password).toProperty("password")
        map(givenName).toProperty("givenName")
        map(familyName).toProperty("familyName")
        map(eastern).toProperty("eastern")
        map(email).toProperty("email")
        map(realm).toProperty("realm")
        map(enabled).toProperty("enabled")
    }

fun AccountMapper.insertMultiple(records: Collection<AccountRecord>) =
    insertMultiple(this::insertMultiple, records, Account) {
        map(accountId).toProperty("accountId")
        map(username).toProperty("username")
        map(password).toProperty("password")
        map(givenName).toProperty("givenName")
        map(familyName).toProperty("familyName")
        map(eastern).toProperty("eastern")
        map(email).toProperty("email")
        map(realm).toProperty("realm")
        map(enabled).toProperty("enabled")
    }

fun AccountMapper.insertMultiple(vararg records: AccountRecord) =
    insertMultiple(records.toList())

fun AccountMapper.insertSelective(record: AccountRecord) =
    insert(this::insert, record, Account) {
        map(accountId).toPropertyWhenPresent("accountId", record::accountId)
        map(username).toPropertyWhenPresent("username", record::username)
        map(password).toPropertyWhenPresent("password", record::password)
        map(givenName).toPropertyWhenPresent("givenName", record::givenName)
        map(familyName).toPropertyWhenPresent("familyName", record::familyName)
        map(eastern).toPropertyWhenPresent("eastern", record::eastern)
        map(email).toPropertyWhenPresent("email", record::email)
        map(realm).toPropertyWhenPresent("realm", record::realm)
        map(enabled).toPropertyWhenPresent("enabled", record::enabled)
    }

private val columnList = listOf(accountId, username, password, givenName, familyName, eastern, email, realm, enabled)

fun AccountMapper.selectOne(completer: SelectCompleter) =
    selectOne(this::selectOne, columnList, Account, completer)

fun AccountMapper.select(completer: SelectCompleter) =
    selectList(this::selectMany, columnList, Account, completer)

fun AccountMapper.selectDistinct(completer: SelectCompleter) =
    selectDistinct(this::selectMany, columnList, Account, completer)

fun AccountMapper.selectByPrimaryKey(accountId_: Object) =
    selectOne {
        where(accountId, isEqualTo(accountId_))
    }

fun AccountMapper.update(completer: UpdateCompleter) =
    update(this::update, Account, completer)

fun KotlinUpdateBuilder.updateAllColumns(record: AccountRecord) =
    apply {
        set(accountId).equalTo(record::accountId)
        set(username).equalTo(record::username)
        set(password).equalTo(record::password)
        set(givenName).equalTo(record::givenName)
        set(familyName).equalTo(record::familyName)
        set(eastern).equalTo(record::eastern)
        set(email).equalTo(record::email)
        set(realm).equalTo(record::realm)
        set(enabled).equalTo(record::enabled)
    }

fun KotlinUpdateBuilder.updateSelectiveColumns(record: AccountRecord) =
    apply {
        set(accountId).equalToWhenPresent(record::accountId)
        set(username).equalToWhenPresent(record::username)
        set(password).equalToWhenPresent(record::password)
        set(givenName).equalToWhenPresent(record::givenName)
        set(familyName).equalToWhenPresent(record::familyName)
        set(eastern).equalToWhenPresent(record::eastern)
        set(email).equalToWhenPresent(record::email)
        set(realm).equalToWhenPresent(record::realm)
        set(enabled).equalToWhenPresent(record::enabled)
    }

fun AccountMapper.updateByPrimaryKey(record: AccountRecord) =
    update {
        set(username).equalTo(record::username)
        set(password).equalTo(record::password)
        set(givenName).equalTo(record::givenName)
        set(familyName).equalTo(record::familyName)
        set(eastern).equalTo(record::eastern)
        set(email).equalTo(record::email)
        set(realm).equalTo(record::realm)
        set(enabled).equalTo(record::enabled)
        where(accountId, isEqualTo(record::accountId))
    }

fun AccountMapper.updateByPrimaryKeySelective(record: AccountRecord) =
    update {
        set(username).equalToWhenPresent(record::username)
        set(password).equalToWhenPresent(record::password)
        set(givenName).equalToWhenPresent(record::givenName)
        set(familyName).equalToWhenPresent(record::familyName)
        set(eastern).equalToWhenPresent(record::eastern)
        set(email).equalToWhenPresent(record::email)
        set(realm).equalToWhenPresent(record::realm)
        set(enabled).equalToWhenPresent(record::enabled)
        where(accountId, isEqualTo(record::accountId))
    }