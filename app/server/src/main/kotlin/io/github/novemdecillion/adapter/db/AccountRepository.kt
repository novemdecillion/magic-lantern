package io.github.novemdecillion.adapter.db

import io.github.novemdecillion.adapter.jooq.tables.pojos.AccountEntity
import io.github.novemdecillion.adapter.jooq.tables.pojos.RealmEntity
import io.github.novemdecillion.adapter.jooq.tables.records.AccountRecord
import io.github.novemdecillion.adapter.jooq.tables.records.RealmRecord
import io.github.novemdecillion.adapter.jooq.tables.references.ACCOUNT
import io.github.novemdecillion.adapter.jooq.tables.references.REALM
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class AccountRepository(private val dslContext: DSLContext) {

  fun recordMapper(account: AccountEntity): AccountRecord {
    val record = AccountRecord()
    account.accountId?.also { record.accountId = it }
    account.accountName?.also { record.accountName = it }
    account.password?.also { record.password = it }
    account.userName?.also { record.userName = it }
    account.email?.also { record.email = it }
    account.locale?.also { record.locale = it }
    account.realmId?.also { record.realmId = it }
    account.enabled?.also { record.enabled = it }
    return record
  }

  fun insert(account: AccountEntity) {
    val record = recordMapper(account)
    dslContext.insertInto(ACCOUNT)
      .set(record)
      .execute()
  }

  fun update(account: AccountEntity): Int {
    val record = recordMapper(account)
    return dslContext.update(ACCOUNT)
      .set(record)
      .let {
        if (null != account.accountId) {
          it.where(ACCOUNT.ACCOUNT_ID.equal(account.accountId))
        } else it
      }
      .execute()
  }

  fun changePassword(accountId: UUID, encodedNewPassword: String): Int {
    return dslContext.update(ACCOUNT)
      .set(ACCOUNT.PASSWORD, encodedNewPassword)
      .where(ACCOUNT.ACCOUNT_ID.equal(accountId))
      .execute()
  }

  fun delete(userId: UUID): Int {
    return dslContext.deleteFrom(ACCOUNT).where(ACCOUNT.ACCOUNT_ID.equal(userId)).execute()
  }

  fun updateEnableByAccountNameAndRealm(accountNames: Collection<String>, realm: String, enabled: Boolean) {
    dslContext.updateQuery(ACCOUNT)
      .also {
        it.addConditions(ACCOUNT.ACCOUNT_NAME.`in`(accountNames)
          .and(ACCOUNT.REALM_ID.equal(realm)))
        it.addValue(ACCOUNT.ENABLED, enabled)
        it.execute()
      }
  }


  fun selectAccountNamesByRealmId(realm: String): List<String> {
    return dslContext.select(ACCOUNT.ACCOUNT_NAME).from(ACCOUNT)
      .where(ACCOUNT.REALM_ID.equal(realm))
      .fetchInto(String::class.java)
  }

  fun selectByAccountNameAndRealmId(accountName: String, realmId: String): AccountEntity? {
    return dslContext.selectFrom(ACCOUNT)
      .where(ACCOUNT.ACCOUNT_NAME.equal(accountName))
      .and(ACCOUNT.REALM_ID.equal(realmId))
      .fetchOneInto(AccountEntity::class.java)
  }

  fun selectByAccountNamesAndRealmId(accountNames: Collection<String>, realmId: String): List<AccountEntity> {
    return dslContext.selectFrom(ACCOUNT)
      .where(ACCOUNT.ACCOUNT_NAME.`in`(accountNames))
      .and(ACCOUNT.REALM_ID.equal(realmId))
      .fetchInto(AccountEntity::class.java)
  }

  fun recordMapper(realm: RealmEntity): RealmRecord {
    val record = RealmRecord()
    realm.realmId?.also { record.realmId = it }
    realm.realmName?.also { record.realmName = it }
    realm.enabled?.also { record.enabled = it }
    realm.syncAt?.also { record.syncAt = it }
    return record
  }

  fun insert(realm: RealmEntity) {
    val record = recordMapper(realm)
    dslContext.executeInsert(record)
  }

  fun update(realm: RealmEntity) {
    val record = recordMapper(realm)
    dslContext.executeUpdate(record)
  }

  fun selectRealm(): List<RealmEntity> {
    return dslContext.selectFrom(REALM).orderBy(REALM.REALM_NAME).fetchInto(RealmEntity::class.java)
  }

}