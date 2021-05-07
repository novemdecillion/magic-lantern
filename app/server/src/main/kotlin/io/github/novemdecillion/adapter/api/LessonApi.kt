package io.github.novemdecillion.adapter.api

import graphql.kickstart.tools.GraphQLMutationResolver
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.kickstart.tools.GraphQLResolver
import graphql.schema.DataFetchingEnvironment
import io.github.novemdecillion.adapter.db.GroupRepository
import io.github.novemdecillion.adapter.db.LessonRepository
import io.github.novemdecillion.domain.*
import io.github.novemdecillion.slide.Slide
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.CompletableFuture

@Component
class LessonApi(private val lessonRepository: LessonRepository): GraphQLQueryResolver, GraphQLMutationResolver {
  data class AddLessonCommand(
    val slideId: String,
    val groupIds: List<UUID>)

  fun manageableLessons(environment: DataFetchingEnvironment): List<LessonWithGroup> {
    return lessonsByRole(Role.LESSON, environment)
  }

  fun studiableLessons(environment: DataFetchingEnvironment): List<LessonWithGroup> {
    return lessonsByRole(Role.STUDY, environment)
  }

  fun lessonsByRole(role: Role, environment: DataFetchingEnvironment): List<LessonWithGroup> {
    val manageableGroupIds = environment.currentUser().authorities
      ?.filter { it.roles.contains(role) }
      ?.map { it.groupId }
      ?: return listOf()

    return lessonRepository.selectByGroupTransitionIds(manageableGroupIds)
  }

  fun addLesson(command: AddLessonCommand): Boolean {
    lessonRepository.insert(command.slideId, command.groupIds)
    return true
  }

}

@Component
class LessonWithGroupResolver : GraphQLResolver<LessonWithGroup> {
  fun slide(lessonWithGroup: LessonWithGroup, environment: DataFetchingEnvironment): CompletableFuture<Slide> {
    val loader = environment.getDataLoader<String, Slide>(SlideApi.SlideLoader::class.java.simpleName)
    return loader.load(lessonWithGroup.lesson.slideId)
  }
}