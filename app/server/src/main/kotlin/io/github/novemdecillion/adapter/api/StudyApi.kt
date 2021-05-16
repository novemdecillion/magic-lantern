package io.github.novemdecillion.adapter.api

import graphql.kickstart.tools.GraphQLMutationResolver
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.kickstart.tools.GraphQLResolver
import graphql.schema.DataFetchingEnvironment
import io.github.novemdecillion.adapter.db.LessonRepository
import io.github.novemdecillion.adapter.db.StudyRepository
import io.github.novemdecillion.domain.*
import io.github.novemdecillion.domain.Slide
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class StudyApi(private val studyRepository: StudyRepository, private val lessonRepository: LessonRepository): GraphQLQueryResolver, GraphQLMutationResolver {

  fun myStudies(environment: DataFetchingEnvironment): List<Study> {
    val userId = environment.currentUser().userId
    val startedStudies = studyRepository.selectByUserId(userId)
    val startedSlides = startedStudies.map { it.slideId }.toSet()

    val notStartedStudies = lessonRepository.selectByUserId(userId)
      .filter { !startedSlides.contains(it.slideId) }
      .map {
        Study(userId = userId, slideId = it.slideId)
      }
    return startedStudies.plus(notStartedStudies)
  }
}

@Component
class StudyResolver : GraphQLResolver<Study> {
  fun slide(study: Study, environment: DataFetchingEnvironment): CompletableFuture<Slide> {
    val loader = environment.getDataLoader<String, Slide>(SlideApi.SlideLoader::class.java.simpleName)
    return loader.load(study.slideId)
  }

  fun lessons(study: Study, environment: DataFetchingEnvironment): CompletableFuture<List<LessonWithGroup>> {
    val keys = environment.currentUser().authorities
      ?.filter { it.roles.contains(Role.STUDY) }
      ?.map { it.groupId to study.slideId }
      ?: return CompletableFuture.completedFuture(listOf())
    val loader = environment.dataLoader(LessonApi.LessonWithGroupLoader::class)
    return loader.loadMany(keys)
  }

}
