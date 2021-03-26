/*
 * Auto-generated file. Created by MyBatis Generator
 * Generation date: 2021-03-25T16:08:49.522564+09:00
 */
package io.github.novemdecillion.adapter.mybatis

import io.github.novemdecillion.adapter.mybatis.UserAggregateDynamicSqlSupport.UserAggregate
import io.github.novemdecillion.adapter.mybatis.UserAggregateDynamicSqlSupport.UserAggregate.accountId
import io.github.novemdecillion.adapter.mybatis.UserAggregateDynamicSqlSupport.UserAggregate.eastern
import io.github.novemdecillion.adapter.mybatis.UserAggregateDynamicSqlSupport.UserAggregate.email
import io.github.novemdecillion.adapter.mybatis.UserAggregateDynamicSqlSupport.UserAggregate.enabled
import io.github.novemdecillion.adapter.mybatis.UserAggregateDynamicSqlSupport.UserAggregate.familyName
import io.github.novemdecillion.adapter.mybatis.UserAggregateDynamicSqlSupport.UserAggregate.givenName
import io.github.novemdecillion.adapter.mybatis.UserAggregateDynamicSqlSupport.UserAggregate.groupOriginId
import io.github.novemdecillion.adapter.mybatis.UserAggregateDynamicSqlSupport.UserAggregate.password
import io.github.novemdecillion.adapter.mybatis.UserAggregateDynamicSqlSupport.UserAggregate.realm
import io.github.novemdecillion.adapter.mybatis.UserAggregateDynamicSqlSupport.UserAggregate.role
import io.github.novemdecillion.adapter.mybatis.UserAggregateDynamicSqlSupport.UserAggregate.username
import io.github.novemdecillion.adapter.mybatis.UserAggregateRecord
import org.mybatis.dynamic.sql.util.kotlin.*
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.*

fun UserAggregateMapper.count(completer: CountCompleter) =
    countFrom(this::count, UserAggregate, completer)

fun UserAggregateMapper.delete(completer: DeleteCompleter) =
    deleteFrom(this::delete, UserAggregate, completer)

fun UserAggregateMapper.insert(record: UserAggregateRecord) =
    insert(this::insert, record, UserAggregate) {
        map(accountId).toProperty("accountId")
        map(username).toProperty("username")
        map(password).toProperty("password")
        map(givenName).toProperty("givenName")
        map(familyName).toProperty("familyName")
        map(eastern).toProperty("eastern")
        map(email).toProperty("email")
        map(realm).toProperty("realm")
        map(enabled).toProperty("enabled")
        map(groupOriginId).toProperty("groupOriginId")
        map(role).toProperty("role")
    }

fun UserAggregateMapper.insertMultiple(records: Collection<UserAggregateRecord>) =
    insertMultiple(this::insertMultiple, records, UserAggregate) {
        map(accountId).toProperty("accountId")
        map(username).toProperty("username")
        map(password).toProperty("password")
        map(givenName).toProperty("givenName")
        map(familyName).toProperty("familyName")
        map(eastern).toProperty("eastern")
        map(email).toProperty("email")
        map(realm).toProperty("realm")
        map(enabled).toProperty("enabled")
        map(groupOriginId).toProperty("groupOriginId")
        map(role).toProperty("role")
    }

fun UserAggregateMapper.insertMultiple(vararg records: UserAggregateRecord) =
    insertMultiple(records.toList())

fun UserAggregateMapper.insertSelective(record: UserAggregateRecord) =
    insert(this::insert, record, UserAggregate) {
        map(accountId).toPropertyWhenPresent("accountId", record::accountId)
        map(username).toPropertyWhenPresent("username", record::username)
        map(password).toPropertyWhenPresent("password", record::password)
        map(givenName).toPropertyWhenPresent("givenName", record::givenName)
        map(familyName).toPropertyWhenPresent("familyName", record::familyName)
        map(eastern).toPropertyWhenPresent("eastern", record::eastern)
        map(email).toPropertyWhenPresent("email", record::email)
        map(realm).toPropertyWhenPresent("realm", record::realm)
        map(enabled).toPropertyWhenPresent("enabled", record::enabled)
        map(groupOriginId).toPropertyWhenPresent("groupOriginId", record::groupOriginId)
        map(role).toPropertyWhenPresent("role", record::role)
    }

private val columnList = listOf(accountId, username, password, givenName, familyName, eastern, email, realm, enabled, groupOriginId, role)

fun UserAggregateMapper.selectOne(completer: SelectCompleter) =
    selectOne(this::selectOne, columnList, UserAggregate, completer)

fun UserAggregateMapper.select(completer: SelectCompleter) =
    selectList(this::selectMany, columnList, UserAggregate, completer)

fun UserAggregateMapper.selectDistinct(completer: SelectCompleter) =
    selectDistinct(this::selectMany, columnList, UserAggregate, completer)

fun UserAggregateMapper.update(completer: UpdateCompleter) =
    update(this::update, UserAggregate, completer)

fun KotlinUpdateBuilder.updateAllColumns(record: UserAggregateRecord) =
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
        set(groupOriginId).equalTo(record::groupOriginId)
        set(role).equalTo(record::role)
    }

fun KotlinUpdateBuilder.updateSelectiveColumns(record: UserAggregateRecord) =
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
        set(groupOriginId).equalToWhenPresent(record::groupOriginId)
        set(role).equalToWhenPresent(record::role)
    }