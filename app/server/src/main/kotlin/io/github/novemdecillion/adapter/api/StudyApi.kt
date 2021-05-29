package io.github.novemdecillion.adapter.api

import graphql.kickstart.tools.GraphQLMutationResolver
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.kickstart.tools.GraphQLResolver
import graphql.schema.DataFetchingEnvironment
import io.github.novemdecillion.adapter.db.LessonRepository
import io.github.novemdecillion.adapter.db.StudyRepository
import io.github.novemdecillion.adapter.id.IdGeneratorService
import io.github.novemdecillion.domain.*
import io.github.novemdecillion.domain.Slide
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.util.*
import java.util.concurrent.CompletableFuture

@Component
class StudyApi(private val studyRepository: StudyRepository, private val idGeneratorService: IdGeneratorService): GraphQLQueryResolver, GraphQLMutationResolver {
  fun study(studyId: UUID, environment: DataFetchingEnvironment): Study? {
    val userId = environment.currentUser().userId
    return studyRepository.selectById(studyId, userId)
  }

  fun studiesByUser(environment: DataFetchingEnvironment): List<Study> {
    val userId = environment.currentUser().userId
    return studyRepository.selectByUserId(userId)
  }

  fun startStudy(slideId: String, environment: DataFetchingEnvironment): UUID {
    val userId = environment.currentUser().userId
    val startStudy = Study(
      studyId = idGeneratorService.generate(),
      slideId = slideId,
      userId = userId,
      startAt = OffsetDateTime.now(),
      status = StudyStatus.ON_GOING
    )
    studyRepository.insert(startStudy)
    return startStudy.studyId
  }
}

class StudyChapterRecord(
  val chapterIndex: Int,
  chapterRecord: IExamChapterRecord
): IExamChapterRecord by chapterRecord

data class StudyQuestionAnswer(
  val questionIndex: Int,
  val answers: List<Int>
)

data class StudyChapterAnswer(
  val chapterIndex: Int,
  val questions: List<StudyQuestionAnswer>
)

data class StudyProgress(
  val chapterIndex: Int,
  val pageIndexes: Set<Int>
)

@Component
class StudyResolver : GraphQLResolver<Study> {
  fun progressDetails(study: Study, environment: DataFetchingEnvironment): CompletableFuture<List<StudyProgress>> {
    return CompletableFuture.completedFuture(study.progress.map { StudyProgress(it.key, it.value) })
  }

  fun answerDetails(study: Study, environment: DataFetchingEnvironment): CompletableFuture<List<StudyChapterAnswer>> {
    return CompletableFuture.completedFuture(study.answer.map {
      StudyChapterAnswer(it.key, Study.convertForExamAnswer(it.value)
        .map { (questionIndex, answers) ->
          StudyQuestionAnswer(questionIndex, answers)
        })
    })
  }
  fun scoreDetails(study: Study, environment: DataFetchingEnvironment): CompletableFuture<List<StudyChapterRecord>> {
    return CompletableFuture.completedFuture(study.score.map { StudyChapterRecord(it.key, it.value) })
  }

  fun user(study: Study, environment: DataFetchingEnvironment): CompletableFuture<User> {
    val loader = environment.dataLoader(UserApi.UserLoader::class)
    return loader.load(study.userId)
  }

  fun slide(study: Study, environment: DataFetchingEnvironment): CompletableFuture<Slide> {
    val loader = environment.dataLoader(SlideApi.SlideLoader::class)
    return loader.load(study.slideId)
  }

  fun lessons(study: Study, environment: DataFetchingEnvironment): CompletableFuture<List<LessonWithGroup>> {
    val keys = environment.currentUser().authorities
      .filter { it.roles?.contains(Role.STUDY) == true }
      .map { it.groupId to study.slideId }
    val loader = environment.dataLoader(LessonApi.LessonWithGroupLoader::class)
    return loader.loadMany(keys)
  }

}
