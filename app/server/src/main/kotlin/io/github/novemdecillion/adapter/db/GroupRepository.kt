package io.github.novemdecillion.adapter.db

import io.github.novemdecillion.adapter.jooq.tables.records.AccountRecord
import io.github.novemdecillion.adapter.jooq.tables.records.CurrentGroupTransitionRecord
import io.github.novemdecillion.adapter.jooq.tables.references.CURRENT_GROUP_TRANSITION
import io.github.novemdecillion.domain.Group
import io.github.novemdecillion.domain.User
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class GroupRepository(private val dslContext: DSLContext) {
  fun selectAll(): List<Group> {
    return dslContext.selectFrom(CURRENT_GROUP_TRANSITION)
      .fetch(::recordMapper)
  }

  fun recordMapper(record: CurrentGroupTransitionRecord): Group {
    return Group(
      record.groupTransitionId!!, record.groupOriginId!!, record.groupGenerationId!!, record.groupName!!, record.parentGroupTransitionId)
  }

}