package io.github.novemdecillion.adapter.api

import graphql.kickstart.tools.GraphQLMutationResolver
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.kickstart.tools.GraphQLResolver
import graphql.schema.DataFetchingEnvironment
import io.github.novemdecillion.adapter.db.StudyRepository
import io.github.novemdecillion.adapter.id.IdGeneratorService
import io.github.novemdecillion.domain.*
import io.github.novemdecillion.domain.Slide
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.CompletableFuture

@Component
class StudyApi(private val studyRepository: StudyRepository, private val idGeneratorService: IdGeneratorService): GraphQLQueryResolver, GraphQLMutationResolver {
  data class ChangeStudyStatus (
    val userId: UUID,
    val slideId: String,
    val studyId: UUID?,
    val status: StudyStatus
  )

  @GraphQLApi
  fun study(studyId: UUID, environment: DataFetchingEnvironment): Study? {
    // TODO API権限チェック
    return studyRepository.selectById(studyId)
  }

  @GraphQLApi
  fun studiesByUser(environment: DataFetchingEnvironment): List<INotStartStudy> {
    val userId = environment.currentUser().userId
    val list = mutableListOf<INotStartStudy>()
    list.addAll(studyRepository.selectByUserIdAndExcludeStatus(userId, StudyStatus.EXCLUDED))
    list.addAll(studyRepository.selectNotStartStudyByUserId(userId))
    return list
  }

  @GraphQLApi
  fun startStudy(slideId: String, environment: DataFetchingEnvironment): UUID {
    val userId = environment.currentUser().userId
    val startStudy = Study(
      studyId = idGeneratorService.generate(),
      slideId = slideId,
      userId = userId,
      status = StudyStatus.ON_GOING
    )
    studyRepository.insert(startStudy)
    return startStudy.studyId
  }

  @GraphQLApi
  fun changeStudyStatus(command: ChangeStudyStatus, environment: DataFetchingEnvironment): Boolean {
    require((command.status == StudyStatus.NOT_START) || (command.status == StudyStatus.EXCLUDED))

    when (command.status) {
      StudyStatus.NOT_START -> studyRepository.deleteBySlideIdAndUserId(command.slideId, command.userId)
      StudyStatus.EXCLUDED ->
        if (command.studyId == null) {
          val startStudy = Study(
            studyId = idGeneratorService.generate(),
            slideId = command.slideId,
            userId = command.userId,
            status = command.status
          )
          studyRepository.saveStatus(startStudy)
        } else {
          studyRepository.updateStatus(command.studyId, command.userId, command.status)
        }
    }
    return true
  }
}

class StudyChapterRecord(
  val chapterIndex: Int,
  chapterRecord: IExamChapterRecord
): IExamChapterRecord by chapterRecord

data class StudyQuestionAnswer(
  val questionIndex: Int,
  val answers: List<String>
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
class StudyResolver : AbstractNotStartStudyResolver<Study>(), GraphQLResolver<Study> {
  fun progressDetails(study: Study, environment: DataFetchingEnvironment): CompletableFuture<List<StudyProgress>> {
    return CompletableFuture.completedFuture(study.progress.map { StudyProgress(it.key, it.value) })
  }

  fun answerDetails(study: Study, environment: DataFetchingEnvironment): CompletableFuture<List<StudyChapterAnswer>> {
    return CompletableFuture.completedFuture(study.answer.map {
      StudyChapterAnswer(it.key, it.value
        .map { (questionIndex, answers) ->
          StudyQuestionAnswer(questionIndex, answers)
        })
    })
  }

  fun scoreDetails(study: Study, environment: DataFetchingEnvironment): CompletableFuture<List<StudyChapterRecord>> {
    return CompletableFuture.completedFuture(study.score.map { StudyChapterRecord(it.key, it.value) })
  }
}

@Component
class NotStartStudyResolver : AbstractNotStartStudyResolver<NotStartStudy>(), GraphQLResolver<NotStartStudy>


abstract class AbstractNotStartStudyResolver<T: INotStartStudy> {
  fun user(study: T, environment: DataFetchingEnvironment): CompletableFuture<User> {
    val loader = environment.dataLoader(UserApi.UserLoader::class)
    return loader.load(study.userId)
  }

  fun slide(study: T, environment: DataFetchingEnvironment): CompletableFuture<Slide> {
    val loader = environment.dataLoader(SlideApi.SlideLoader::class)
    return loader.load(study.slideId)
  }
}