package io.github.novemdecillion.adapter.db

import io.github.novemdecillion.adapter.jooq.tables.pojos.RealmEntity
import io.github.novemdecillion.adapter.jooq.tables.records.RealmRecord
import io.github.novemdecillion.adapter.jooq.tables.references.*
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class RealmRepository(
  private val dslContext: DSLContext
  ) {
  companion object {
    fun recordMapper(realm: RealmEntity): RealmRecord {
      val record = RealmRecord()
      realm.realmId?.also { record.realmId = it }
      realm.realmName?.also { record.realmName = it }
      realm.enabled?.also { record.enabled = it }
      realm.syncAt?.also { record.syncAt = it }
      return record
    }
  }

  fun insert(realm: RealmEntity): Int {
    val record = recordMapper(realm)
    return dslContext.executeInsert(record)
  }

  fun update(realm: RealmEntity): Int {
    val record = recordMapper(realm)
    return dslContext.executeUpdate(record)
  }

  fun selectAll(): List<RealmEntity> {
    return dslContext.selectFrom(REALM).orderBy(REALM.REALM_NAME).fetchInto(RealmEntity::class.java)
  }
}