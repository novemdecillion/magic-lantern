package io.github.novemdecillion.adapter.db

import io.github.novemdecillion.adapter.jooq.tables.pojos.AccountEntity
import io.github.novemdecillion.adapter.jooq.tables.pojos.CurrentAccountGroupAuthorityEntity
import io.github.novemdecillion.adapter.jooq.tables.references.ACCOUNT
import io.github.novemdecillion.adapter.jooq.tables.references.CURRENT_ACCOUNT_GROUP_AUTHORITY
import io.github.novemdecillion.adapter.jooq.tables.references.GROUP_TRANSITION_WITH_PATH
import io.github.novemdecillion.domain.Authority
import io.github.novemdecillion.domain.User
import org.jetbrains.annotations.Nullable
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.ResultQuery
import org.jooq.SelectWhereStep
import org.springframework.stereotype.Repository
import javax.annotation.PostConstruct
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.transaction.support.TransactionTemplate


@Repository
class MaterializedViewRepository(private val dslContext: DSLContext) {
  fun refreshCurrentGroupTransitionTable() {
    dslContext.execute("refresh materialized view ${GROUP_TRANSITION_WITH_PATH.name}")
  }

  @EventListener(ContextRefreshedEvent::class)
  fun onApplicationEvent(event: ContextRefreshedEvent) {
    event.applicationContext.getBean(TransactionTemplate::class.java)
      .execute {
        refreshCurrentGroupTransitionTable()
      }
  }

}