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
import io.github.novemdecillion.domain.StudyStatus.*
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.CompletableFuture

@Component
class LessonApi(private val lessonRepository: LessonRepository,
                private val studyRepository: StudyRepository,
                private val idGeneratorService: IdGeneratorService
): GraphQLQueryResolver, GraphQLMutationResolver {

  data class AddLessonCommand(
    val slideId: String,
    val groupIds: List<UUID>)

  @GraphQLApi
  fun lesson(lessonId: UUID): LessonWithGroup? {
    return lessonRepository.selectById(lessonId)
  }

  @GraphQLApi
  fun manageableLessons(environment: DataFetchingEnvironment): List<LessonWithGroup> {
    return lessonsByRole(Role.LESSON, environment)
  }

  @GraphQLApi
  fun lessonsByRole(role: Role, environment: DataFetchingEnvironment): List<LessonWithGroup> {
    return environment.currentUser().authorities
      .filter { it.roles?.contains(Role.LESSON) == true }
      .map { it.groupId }
      .let {
        lessonRepository.selectByGroupTransitionIds(it)
      }
  }

  @GraphQLApi
  fun addLesson(command: AddLessonCommand): Boolean {
    lessonRepository.insert(
      command.groupIds
        .map { Lesson(lessonId = idGeneratorService.generate(), groupId = it, slideId = command.slideId)  }
    )
    return true
  }

  @GraphQLApi
  fun deleteLesson(lessonId: UUID): Boolean {
    return 1 == lessonRepository.delete(lessonId)
  }

  @GraphQLApi
  fun lessonStudies(lessonId: UUID): List<INotStartStudy> {
    val list = mutableListOf<INotStartStudy>()
    list.addAll(studyRepository.selectByLessonId(lessonId))
    list.addAll(studyRepository.selectNotStartStudyByLessonId(lessonId))
    return list
  }
}

data class LessonStatistics(val onGoingCount: Int, val passCount: Int, val failCount: Int)


@Component
class LessonWithGroupResolver(val studyRepository: StudyRepository) : GraphQLResolver<LessonWithGroup> {
  fun slide(lesson: LessonWithGroup, environment: DataFetchingEnvironment): CompletableFuture<Slide> {
    val loader = environment.dataLoader(SlideApi.SlideLoader::class)
    return loader.load(lesson.slideId)
  }

  fun studentCount(lesson: LessonWithGroup, environment: DataFetchingEnvironment): CompletableFuture<Int> {
    val loader = environment.dataLoader(GroupApi.GroupStudentCountLoader::class)
    return loader.load(lesson.group)
  }

  fun statistics(lesson: LessonWithGroup): CompletableFuture<LessonStatistics> {
    val lessonStatistics = studyRepository.selectBySlideIdAndGroupId(lesson.slideId, lesson.group.groupId)
      .let { studies ->
        var onGoingCount = 0
        var passCount = 0
        var failCount = 0
        studies
          .forEach {
            when(it.status) {
              ON_GOING -> onGoingCount++
              PASS -> passCount++
              FAILED -> failCount++
              NOT_START -> { /* 何もしない */ }
            }
          }
        LessonStatistics(onGoingCount, passCount, failCount)
      }
    return CompletableFuture.completedFuture(lessonStatistics)
  }
}

@Component
class LessonResolver : GraphQLResolver<Lesson> {
  fun slide(lesson: Lesson, environment: DataFetchingEnvironment): CompletableFuture<Slide> {
    val loader = environment.dataLoader(SlideApi.SlideLoader::class)
    return loader.load(lesson.slideId)
  }
}
