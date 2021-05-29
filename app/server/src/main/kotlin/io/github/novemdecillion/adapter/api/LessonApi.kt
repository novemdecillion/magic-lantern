package io.github.novemdecillion.adapter.api

import graphql.kickstart.tools.GraphQLMutationResolver
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.kickstart.tools.GraphQLResolver
import graphql.schema.DataFetchingEnvironment
import io.github.novemdecillion.adapter.db.LessonRepository
import io.github.novemdecillion.adapter.db.StudyRepository
import io.github.novemdecillion.adapter.db.UserRepository
import io.github.novemdecillion.domain.*
import io.github.novemdecillion.domain.Slide
import org.dataloader.BatchLoaderEnvironment
import org.dataloader.MappedBatchLoaderWithContext
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

@Component
class LessonApi(private val lessonRepository: LessonRepository,
                private val userRepository: UserRepository,
                private val studyRepository: StudyRepository): GraphQLQueryResolver, GraphQLMutationResolver {
  @Component
  class LessonWithGroupLoader(private val lessonRepository: LessonRepository): MappedBatchLoaderWithContext<Pair<UUID, String>, LessonWithGroup>, LoaderFunctionMaker<Pair<UUID, String>, LessonWithGroup> {
    override fun load(groupIdAndSlideIds: Set<Pair<UUID, String>>, environment: BatchLoaderEnvironment): CompletionStage<Map<Pair<UUID, String>, LessonWithGroup>> {
      val result = lessonRepository.selectByGroupIdsAndSlideIds(groupIdAndSlideIds.map { it.first }.toSet(), groupIdAndSlideIds.map { it.second }.toSet())
        .associateBy { it.groupId to it.slideId }
      return CompletableFuture.completedFuture(result)
    }
  }

  data class AddLessonCommand(
    val slideId: String,
    val groupIds: List<UUID>)

  fun lesson(lessonId: UUID): LessonWithGroup? {
    return lessonRepository.selectById(lessonId)
  }

  fun manageableLessons(environment: DataFetchingEnvironment): List<LessonWithGroup> {
    return lessonsByRole(Role.LESSON, environment)
  }

  fun lessonsByRole(role: Role, environment: DataFetchingEnvironment): List<LessonWithGroup> {
    return environment.currentUser().authorities
      .filter { it.roles?.contains(Role.LESSON) == true }
      .map { it.groupId }
      .let {
        lessonRepository.selectByGroupTransitionIds(it)
      }
  }

  fun notStartStudyByUser(environment: DataFetchingEnvironment): List<Lesson> {
    val userId = environment.currentUser().userId
    return lessonRepository.selectByUserIdAndNotExistStudy(userId)
  }

  fun addLesson(command: AddLessonCommand): Boolean {
    lessonRepository.insert(command.slideId, command.groupIds)
    return true
  }

  fun deleteLesson(lessonId: UUID): Boolean {
    return 1 == lessonRepository.delete(lessonId)
  }

  fun lessonStudies(lessonId: UUID): List<Study> {
    return studyRepository.selectByLessonId(lessonId)
  }

  fun notStartLessonStudies(lessonId: UUID): List<User> {
    return userRepository.selectByLessonIdAndNotExistStudy(lessonId)
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
        var onGoingCount: Int = 0
        var passCount: Int = 0
        var failCount: Int = 0
        studies
          .forEach {
            when(it.status) {
              StudyStatus.ON_GOING -> onGoingCount++
              StudyStatus.PASS -> passCount++
              StudyStatus.FAILED -> failCount++
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
