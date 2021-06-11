package io.github.novemdecillion.adapter.api

import com.fasterxml.jackson.annotation.JsonIgnore
import graphql.kickstart.tools.GraphQLMutationResolver
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.schema.DataFetchingEnvironment
import io.github.novemdecillion.adapter.db.LessonRepository
import io.github.novemdecillion.adapter.db.SlideRepository
import io.github.novemdecillion.adapter.web.AppSlideProperties
import io.github.novemdecillion.domain.Slide
import org.springframework.stereotype.Component
import javax.servlet.http.Part
import org.dataloader.MappedBatchLoader
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

@Component
class SlideApi(private val slideRepository: SlideRepository, private val lessonRepository: LessonRepository) : GraphQLQueryResolver, GraphQLMutationResolver {
  @Component
  class SlideLoader(private val slideRepository: SlideRepository): MappedBatchLoader<String, Slide>, LoaderFunctionMaker<String, Slide> {
    override fun load(keys: Set<String>): CompletionStage<Map<String, Slide>> {
      return CompletableFuture.completedFuture(slideRepository.selectByIds(keys).associateBy { it.slideId })
    }
  }

  data class AddSlideCommand(
    val slideId: String,
    @field:JsonIgnore
    val slideFile: Part? = null)


  @GraphQLApi
  fun slides(): List<Slide> {
    return slideRepository.selectAll()
  }

  @GraphQLApi
  fun slide(slideId: String): Slide? {
    return slideRepository.selectById(slideId)
  }

  @GraphQLApi
  fun addSlide(command: AddSlideCommand, environment: DataFetchingEnvironment): Boolean {
    val slideFile = (environment.variables["command"] as Map<String, Part>)["slideFile"] ?: return false

//    if (slideFile.contentType != "application/zip") {
//      return false
//    }

    slideRepository.insert(command.slideId, slideFile.inputStream)
    return true
  }

  @GraphQLApi
  fun deleteSlide(slideId: String): Boolean {
    return slideRepository.delete(slideId)
  }

  @GraphQLApi
  fun enableSlide(slideId: String, enable: Boolean): Boolean {
    slideRepository.updateEnable(slideId, enable)
    lessonRepository.deleteBySlideId(slideId)
    return true
  }
}

