package io.github.novemdecillion.adapter.db

import io.github.novemdecillion.adapter.id.IdGeneratorService
import io.github.novemdecillion.adapter.jooq.tables.pojos.NoticeEntity
import io.github.novemdecillion.adapter.jooq.tables.records.NoticeRecord
import io.github.novemdecillion.adapter.jooq.tables.references.CURRENT_ACCOUNT_GROUP_AUTHORITY
import io.github.novemdecillion.adapter.jooq.tables.references.CURRENT_GROUP_TRANSITION
import io.github.novemdecillion.adapter.jooq.tables.references.LESSON
import io.github.novemdecillion.adapter.jooq.tables.references.NOTICE
import io.github.novemdecillion.domain.GroupWithPath
import io.github.novemdecillion.domain.Lesson
import io.github.novemdecillion.domain.LessonWithGroup
import io.github.novemdecillion.domain.User
import org.jetbrains.annotations.NotNull
import org.jooq.*
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.*

@Repository
class NoticeRepository(private val dslContext: DSLContext,
                       private val idGeneratorService: IdGeneratorService) {

  fun insert(notice: NoticeEntity): NoticeEntity {
    val updatedNotice = notice.copy(noticeId = idGeneratorService.generate())
    dslContext
      .executeInsert(NoticeRecord()
        .also { record ->
          record.noticeId = updatedNotice.noticeId
          record.message = updatedNotice.message
          updatedNotice.startAt?.let { record.startAt = it }
          updatedNotice.endAt?.let { record.endAt = it }
        })
    return updatedNotice
  }

  fun update(notice: NoticeEntity) {
    dslContext
      .executeUpdate(NoticeRecord()
        .also { record ->
          record.noticeId = notice.noticeId
          notice.message?.also { record.message = it }
          notice.startAt?.also { record.startAt = it }
          notice.endAt?.also { record.endAt = it }
        })
  }

  fun delete(noticeId: UUID): Int {
    return dslContext.deleteFrom(NOTICE).where(NOTICE.NOTICE_ID.equal(noticeId)).execute()
  }

  fun selectById(noticeId: UUID): NoticeEntity? {
    return dslContext.selectFrom(NOTICE).where(NOTICE.NOTICE_ID.equal(noticeId)).fetchOneInto(NoticeEntity::class.java)
  }

  fun selectAll(now: LocalDate?): List<NoticeEntity> {
    return dslContext.selectFrom(NOTICE)
      .let { selectFrom ->
        if (null != now) {
          selectFrom.where(
            NOTICE.START_AT.isNull.or(NOTICE.START_AT.lessOrEqual(now))
              .and(NOTICE.END_AT.isNull.or(NOTICE.END_AT.greaterOrEqual(now)))
          )
        } else selectFrom
      }
      .fetchInto(NoticeEntity::class.java)
  }
}