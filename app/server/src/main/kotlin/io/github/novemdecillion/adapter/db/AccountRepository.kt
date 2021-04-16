package io.github.novemdecillion.adapter.db

import io.github.novemdecillion.adapter.jooq.tables.pojos.AccountEntity
import io.github.novemdecillion.adapter.jooq.tables.pojos.RealmEntity
import io.github.novemdecillion.adapter.jooq.tables.records.AccountRecord
import io.github.novemdecillion.adapter.jooq.tables.records.RealmRecord
import io.github.novemdecillion.adapter.jooq.tables.references.ACCOUNT
import io.github.novemdecillion.adapter.jooq.tables.references.REALM
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class AccountRepository(private val dslContext: DSLContext) {
  fun insert(account: AccountEntity) {
    val record = AccountRecord()
      .also { it.from(account) }
    dslContext.insertQuery(ACCOUNT)
      .also {
        it.setRecord(record)
        it.execute()
      }
  }

  fun update(account: AccountEntity) {
    val record = AccountRecord()
      .also { it.from(account) }
    dslContext.updateQuery(ACCOUNT)
      .also {
        it.setRecord(record)
        it.execute()
      }
  }

  fun updateEnableByAccountNameAndRealm(accountNames: Collection<String>, realm: String?, enabled: Boolean) {
    dslContext.updateQuery(ACCOUNT)
      .also {
        it.addConditions(ACCOUNT.ACCOUNT_NAME.`in`(accountNames)
          .and(realm
            ?.let { ACCOUNT.REALM_ID.equal(it) }
            ?: ACCOUNT.REALM_ID.isNull))
        it.addValue(ACCOUNT.ENABLED, enabled)
        it.execute()
      }
  }


  fun selectAccountNames(realm: String?): List<String> {
    return dslContext.select(ACCOUNT.ACCOUNT_NAME).from(ACCOUNT)
      .where(realm
        ?.let { ACCOUNT.REALM_ID.equal(it) }
        ?: ACCOUNT.REALM_ID.isNull)
      .fetchInto(String::class.java)
  }

  fun selectByAccountNameAndRealm(accountNames: Collection<String>, realm: String?): List<AccountEntity> {
    return dslContext.selectFrom(ACCOUNT)
      .where(ACCOUNT.ACCOUNT_NAME.`in`(accountNames))
      .and(realm
        ?.let { ACCOUNT.REALM_ID.equal(it) }
        ?: ACCOUNT.REALM_ID.isNull)
      .fetchInto(AccountEntity::class.java)
  }

  fun insert(realm: RealmEntity) {
    val record = RealmRecord()
    record.realmId = realm.realmId
    realm.realmName?.also { record.realmName = it }
    realm.enabled?.also { record.enabled = it }
    realm.syncAt?.also { record.syncAt = it }

    dslContext.insertQuery(REALM)
      .also {
        it.setRecord(record)
        it.execute()
      }
  }

  fun update(realm: RealmEntity) {
    val record = RealmRecord()
      .also { it.from(realm) }
    dslContext.updateQuery(REALM)
      .also {
        it.setRecord(record)
        it.execute()
      }
  }

  fun selectRealm(): List<RealmEntity> {
    return dslContext.selectFrom(REALM).orderBy(REALM.REALM_NAME).fetchInto(RealmEntity::class.java)
  }

}