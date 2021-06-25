package io.github.novemdecillion.adapter.db

import io.github.novemdecillion.adapter.jooq.tables.pojos.NoticeEntity
import io.github.novemdecillion.adapter.jooq.tables.records.NoticeRecord
import io.github.novemdecillion.adapter.jooq.tables.references.NOTICE
import org.jooq.*
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.*

@Repository
class NoticeRepository(private val dslContext: DSLContext) {

  fun insert(notice: NoticeEntity): Int {
    require(notice.noticeId != null)
    return dslContext
      .executeInsert(NoticeRecord()
        .also { record ->
          record.noticeId = notice.noticeId
          record.message = notice.message
          record.updateAt = notice.updateAt
          notice.startAt?.let { record.startAt = it }
          notice.endAt?.let { record.endAt = it }
        })
  }

  fun update(notice: NoticeEntity) {
    require(notice.noticeId != null)
    dslContext
      .executeUpdate(NoticeRecord()
        .also { record ->
          record.noticeId = notice.noticeId
          record.updateAt = notice.updateAt
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