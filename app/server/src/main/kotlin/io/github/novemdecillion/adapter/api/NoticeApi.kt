package io.github.novemdecillion.adapter.api

import graphql.kickstart.tools.GraphQLMutationResolver
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.schema.DataFetchingEnvironment
import io.github.novemdecillion.adapter.db.NoticeRepository
import io.github.novemdecillion.adapter.id.IdGeneratorService
import io.github.novemdecillion.adapter.jooq.tables.pojos.NoticeEntity
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*

@Component
class NoticeApi(private val noticeRepository: NoticeRepository,
                private val idGeneratorService: IdGeneratorService
): GraphQLQueryResolver, GraphQLMutationResolver {

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

  @GraphQLApi
  fun addNotice(command: AddNoticeCommand, environment: DataFetchingEnvironment): Boolean {
    noticeRepository.insert(NoticeEntity(
      noticeId = idGeneratorService.generate(), message = command.message,
      startAt = command.startAt, endAt = command.endAt, updateAt=environment.now()))
    return true
  }

  @GraphQLApi
  fun updateNotice(command: UpdateNoticeCommand, environment: DataFetchingEnvironment): Boolean {
    noticeRepository.update(NoticeEntity(noticeId=command.noticeId, message = command.message,
      startAt = command.startAt, endAt = command.endAt, updateAt=environment.now()))
    return true
  }

  @GraphQLApi
  fun deleteNotice(noticeId: UUID): Boolean {
    noticeRepository.delete(noticeId)
    return true
  }

  @GraphQLApi
  fun notices(): List<NoticeEntity> {
    return noticeRepository.selectAll(null)
  }

  @GraphQLApi
  fun effectiveNotices(environment: DataFetchingEnvironment): List<NoticeEntity> {
    environment.currentUser().isAdmin()
    return noticeRepository.selectAll(environment.now().toLocalDate())
  }

}
