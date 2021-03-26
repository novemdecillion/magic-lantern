/*
 * Auto-generated file. Created by MyBatis Generator
 * Generation date: 2021-03-23T16:18:07.204539+09:00
 */
package io.github.novemdecillion.adapter.mybatis

import io.github.novemdecillion.adapter.mybatis.TopicDynamicSqlSupport.Topic
import io.github.novemdecillion.adapter.mybatis.TopicDynamicSqlSupport.Topic.createAt
import io.github.novemdecillion.adapter.mybatis.TopicDynamicSqlSupport.Topic.filename
import io.github.novemdecillion.adapter.mybatis.TopicDynamicSqlSupport.Topic.title
import io.github.novemdecillion.adapter.mybatis.TopicDynamicSqlSupport.Topic.topicId
import io.github.novemdecillion.adapter.mybatis.TopicDynamicSqlSupport.Topic.updateAt
import org.mybatis.dynamic.sql.SqlBuilder.isEqualTo
import org.mybatis.dynamic.sql.util.kotlin.*
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.*

fun TopicMapper.count(completer: CountCompleter) =
    countFrom(this::count, Topic, completer)

fun TopicMapper.delete(completer: DeleteCompleter) =
    deleteFrom(this::delete, Topic, completer)

fun TopicMapper.deleteByPrimaryKey(topicId_: Long) =
    delete {
        where(topicId, isEqualTo(topicId_))
    }

fun TopicMapper.insert(record: TopicRecord) =
    insert(this::insert, record, Topic) {
        map(topicId).toProperty("topicId")
        map(createAt).toProperty("createAt")
        map(updateAt).toProperty("updateAt")
        map(title).toProperty("title")
        map(filename).toProperty("filename")
    }

fun TopicMapper.insertMultiple(records: Collection<TopicRecord>) =
    insertMultiple(this::insertMultiple, records, Topic) {
        map(topicId).toProperty("topicId")
        map(createAt).toProperty("createAt")
        map(updateAt).toProperty("updateAt")
        map(title).toProperty("title")
        map(filename).toProperty("filename")
    }

fun TopicMapper.insertMultiple(vararg records: TopicRecord) =
    insertMultiple(records.toList())

fun TopicMapper.insertSelective(record: TopicRecord) =
    insert(this::insert, record, Topic) {
        map(topicId).toPropertyWhenPresent("topicId", record::topicId)
        map(createAt).toPropertyWhenPresent("createAt", record::createAt)
        map(updateAt).toPropertyWhenPresent("updateAt", record::updateAt)
        map(title).toPropertyWhenPresent("title", record::title)
        map(filename).toPropertyWhenPresent("filename", record::filename)
    }

private val columnList = listOf(topicId, createAt, updateAt, title, filename)

fun TopicMapper.selectOne(completer: SelectCompleter) =
    selectOne(this::selectOne, columnList, Topic, completer)

fun TopicMapper.select(completer: SelectCompleter) =
    selectList(this::selectMany, columnList, Topic, completer)

fun TopicMapper.selectDistinct(completer: SelectCompleter) =
    selectDistinct(this::selectMany, columnList, Topic, completer)

fun TopicMapper.selectByPrimaryKey(topicId_: Long) =
    selectOne {
        where(topicId, isEqualTo(topicId_))
    }

fun TopicMapper.update(completer: UpdateCompleter) =
    update(this::update, Topic, completer)

fun KotlinUpdateBuilder.updateAllColumns(record: TopicRecord) =
    apply {
        set(topicId).equalTo(record::topicId)
        set(createAt).equalTo(record::createAt)
        set(updateAt).equalTo(record::updateAt)
        set(title).equalTo(record::title)
        set(filename).equalTo(record::filename)
    }

fun KotlinUpdateBuilder.updateSelectiveColumns(record: TopicRecord) =
    apply {
        set(topicId).equalToWhenPresent(record::topicId)
        set(createAt).equalToWhenPresent(record::createAt)
        set(updateAt).equalToWhenPresent(record::updateAt)
        set(title).equalToWhenPresent(record::title)
        set(filename).equalToWhenPresent(record::filename)
    }

fun TopicMapper.updateByPrimaryKey(record: TopicRecord) =
    update {
        set(createAt).equalTo(record::createAt)
        set(updateAt).equalTo(record::updateAt)
        set(title).equalTo(record::title)
        set(filename).equalTo(record::filename)
        where(topicId, isEqualTo(record::topicId))
    }

fun TopicMapper.updateByPrimaryKeySelective(record: TopicRecord) =
    update {
        set(createAt).equalToWhenPresent(record::createAt)
        set(updateAt).equalToWhenPresent(record::updateAt)
        set(title).equalToWhenPresent(record::title)
        set(filename).equalToWhenPresent(record::filename)
        where(topicId, isEqualTo(record::topicId))
    }