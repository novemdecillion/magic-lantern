package io.github.novemdecillion.adapter.api

import graphql.kickstart.tools.GraphQLMutationResolver
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.kickstart.tools.GraphQLResolver
import graphql.schema.DataFetchingEnvironment
import io.github.novemdecillion.adapter.db.StudyRepository
import io.github.novemdecillion.adapter.id.IdGeneratorService
import io.github.novemdecillion.domain.*
import io.github.novemdecillion.domain.Slide
import io.github.novemdecillion.utils.lang.logger
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.CompletableFuture

@Component
class StudyApi(private val studyRepository: StudyRepository, private val idGeneratorService: IdGeneratorService): GraphQLQueryResolver, GraphQLMutationResolver {
  companion object {
    val log = logger()
  }

  data class ChangeStudyStatus (
    val userId: UUID,
    val slideId: String,
    val index: Int,
    val studyId: UUID?,
    val status: StudyStatus
  )

  @GraphQLApi
  fun study(studyId: UUID, environment: DataFetchingEnvironment): Study? {
    // TODO API権限チェック
    return studyRepository.selectById(studyId)
  }

  @GraphQLApi
  fun studiesByUser(slideId: String?, environment: DataFetchingEnvironment): List<INotStartStudy> {
    val userId = environment.currentUser().userId
    val list = mutableListOf<INotStartStudy>()
    list.addAll(studyRepository.selectByUserIdAndExcludeStatusAndSlideId(userId, StudyStatus.EXCLUDED, slideId))
    list.addAll(studyRepository.selectNotStartStudyByUserIdAndSlideId(userId, slideId))
    return list
  }

  @GraphQLApi
  fun latestStudiesByUser(environment: DataFetchingEnvironment): List<INotStartStudy> {
    val userId = environment.currentUser().userId
    val list = mutableListOf<INotStartStudy>()
    list.addAll(studyRepository.selectLatestByUserIdAndExcludeStatus(userId, StudyStatus.EXCLUDED))
    list.addAll(studyRepository.selectNotStartStudyByUserIdAndSlideId(userId))
    return list
  }

  @GraphQLApi
  fun startStudy(slideId: String, environment: DataFetchingEnvironment): UUID {
    val userId = environment.currentUser().userId

    // TODO SQLで最大index値を取得する
    val lastStudy = studyRepository.selectBySlideIdAndUserId(slideId, userId)
      .maxByOrNull { it.index }

    val index = if (lastStudy == null) {
      Study.START_INDEX
    } else {
      if (lastStudy.isComplete()) {
        lastStudy.index + 1
      } else {
        val apiException = ApiException("未完了の受講があるため再受講できません。")
        log.error(apiException.message)
        throw apiException
      }
    }

    val startStudy = Study(
      studyId = idGeneratorService.generate(),
      slideId = slideId,
      userId = userId,
      index = index,
      status = StudyStatus.ON_GOING
    )
    studyRepository.insert(startStudy)
    return startStudy.studyId
  }

  @GraphQLApi
  fun changeStudyStatus(command: ChangeStudyStatus, environment: DataFetchingEnvironment): Boolean {
    when (command.status) {
      StudyStatus.NOT_START -> studyRepository.deleteBySlideIdAndUserId(command.slideId, command.userId, command.index)
      StudyStatus.EXCLUDED ->
        if (command.studyId == null) {
          // この条件は1回も受講していない時だけ。
          val startStudy = Study(
            studyId = idGeneratorService.generate(),
            slideId = command.slideId,
            userId = command.userId,
            /* studyIdがnullなのでindexはデフォルトの1 */
            status = command.status
          )
          studyRepository.saveStatus(startStudy)
        } else {
          studyRepository.updateStatus(command.studyId, command.status)
        }
      else -> {
        val apiException = ApiException("無効な要求です。")
        log.error("${apiException.message} status=${command.status}")
        throw apiException
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