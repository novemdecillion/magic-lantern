package io.github.novemdecillion.adapter.api

import graphql.kickstart.tools.GraphQLMutationResolver
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.kickstart.tools.GraphQLResolver
import graphql.schema.DataFetchingEnvironment
import io.github.novemdecillion.adapter.db.LessonRepository
import io.github.novemdecillion.adapter.db.NoticeRepository
import io.github.novemdecillion.adapter.jooq.tables.pojos.NoticeEntity
import io.github.novemdecillion.domain.*
import io.github.novemdecillion.domain.Slide
import org.dataloader.BatchLoaderEnvironment
import org.dataloader.MappedBatchLoaderWithContext
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

@Component
class NoticeApi(private val noticeRepository: NoticeRepository): GraphQLQueryResolver, GraphQLMutationResolver {

  data class AddNoticeCommand(
    val message: String,
    val startAt: LocalDate?,
    val endAt: LocalDate?)

  data class UpdateNoticeCommand(
    val noticeId: UUID,
    val message: String,
    val startAt: LocalDate?,
    val endAt: LocalDate?,
  )

  fun addNotice(command: AddNoticeCommand, environment: DataFetchingEnvironment): Boolean {
    noticeRepository.insert(NoticeEntity(message = command.message,
      startAt = command.startAt, endAt = command.endAt, updateAt=environment.now()))
    return true
  }

  fun updateNotice(command: UpdateNoticeCommand, environment: DataFetchingEnvironment): Boolean {
    noticeRepository.update(NoticeEntity(noticeId=command.noticeId, message = command.message,
      startAt = command.startAt, endAt = command.endAt, updateAt=environment.now()))
    return true
  }

  fun deleteNotice(noticeId: UUID): Boolean {
    noticeRepository.delete(noticeId)
    return true
  }

  fun notices(): List<NoticeEntity> {
    return noticeRepository.selectAll(null)
  }

  fun effectiveNotices(environment: DataFetchingEnvironment): List<NoticeEntity> {
    environment.currentUser().isAdmin()
    return noticeRepository.selectAll(environment.now().toLocalDate())
  }

}
