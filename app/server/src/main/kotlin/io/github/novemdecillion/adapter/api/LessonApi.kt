package io.github.novemdecillion.adapter.api

import graphql.kickstart.tools.GraphQLMutationResolver
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.kickstart.tools.GraphQLResolver
import graphql.schema.DataFetchingEnvironment
import io.github.novemdecillion.adapter.db.LessonRepository
import io.github.novemdecillion.domain.*
import io.github.novemdecillion.domain.Slide
import org.dataloader.BatchLoaderEnvironment
import org.dataloader.MappedBatchLoaderWithContext
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

@Component
class LessonApi(private val lessonRepository: LessonRepository): GraphQLQueryResolver, GraphQLMutationResolver {
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

  fun manageableLessons(environment: DataFetchingEnvironment): List<LessonWithGroup> {
    return lessonsByRole(Role.LESSON, environment)
  }

  fun lessonsByRole(role: Role, environment: DataFetchingEnvironment): List<LessonWithGroup> {
    return environment.currentUser().authorities
      ?.filter { it.roles.contains(Role.LESSON) }
      ?.map { it.groupId }
      ?.let {
        lessonRepository.selectByGroupTransitionIds(it)
      }
      ?: listOf()
  }

  fun addLesson(command: AddLessonCommand): Boolean {
    lessonRepository.insert(command.slideId, command.groupIds)
    return true
  }

  fun deleteLesson(lessonId: UUID): Boolean {
    return 1 == lessonRepository.delete(lessonId)
  }
}

@Component
class LessonWithGroupResolver : GraphQLResolver<LessonWithGroup> {
  fun slide(lesson: LessonWithGroup, environment: DataFetchingEnvironment): CompletableFuture<Slide> {
    val loader = environment.dataLoader(SlideApi.SlideLoader::class)
    return loader.load(lesson.slideId)
  }
}
