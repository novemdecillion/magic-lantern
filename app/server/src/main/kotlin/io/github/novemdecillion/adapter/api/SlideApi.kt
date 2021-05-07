package io.github.novemdecillion.adapter.api

import com.fasterxml.jackson.annotation.JsonIgnore
import graphql.kickstart.tools.GraphQLMutationResolver
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.schema.DataFetchingEnvironment
import io.github.novemdecillion.adapter.db.SlideRepository
import io.github.novemdecillion.adapter.web.AppSlideProperties
import io.github.novemdecillion.slide.SLIDE_CONFIG_FILE_NAME
import io.github.novemdecillion.slide.Slide
import net.lingala.zip4j.ZipFile
import org.dataloader.BatchLoader
import org.dataloader.DataLoader
import org.springframework.stereotype.Component
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import javax.servlet.http.Part
import org.dataloader.DataLoaderRegistry
import org.dataloader.MappedBatchLoader
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

@Component
class SlideApi(private val slideRepository: SlideRepository, private val appSlideProperties: AppSlideProperties) : GraphQLQueryResolver, GraphQLMutationResolver {
  @Component
  class SlideLoader(private val slideRepository: SlideRepository): MappedBatchLoader<String, Slide>, LoaderFunctionMaker {
    override fun load(keys: Set<String>): CompletionStage<Map<String, Slide>> {
      return CompletableFuture.completedFuture(slideRepository.selectByIds(keys).associateBy { it.slideId })
    }
  }

  data class AddSlideCommand(
    val slideId: String,
    @field:JsonIgnore
    val slideFile: Part? = null)


  fun slides(): List<Slide> {
    return slideRepository.selectAll()
  }

  fun addSlide(command: AddSlideCommand, environment: DataFetchingEnvironment): Boolean {

    slideRepository.insert(command.slideId)

    val slideFile = (environment.variables["command"] as Map<String, Part>)["slideFile"] ?: return false

    if (slideFile.contentType != "application/zip") {
      return false
    }


    val tempFolderPath = Files.createTempDirectory(Paths.get(appSlideProperties.rootPath.file.absolutePath), "temp-")
    val tempZipFile = Files.createTempFile(tempFolderPath, "slide-", ".zip")

    FileOutputStream(tempZipFile.toFile())
      .use { fileOutputStream ->
        fileOutputStream.write(slideFile.inputStream.readAllBytes())
      }

    val zipFile = ZipFile(tempZipFile.toFile())
    zipFile.extractAll(tempFolderPath.toString())
    Files.delete(tempZipFile)

    val slideConfigPath = Files.list(tempFolderPath).filter { it.fileName.toString() == SLIDE_CONFIG_FILE_NAME }
      .findFirst()
    if (slideConfigPath.isEmpty) {
      // TODO 設定ファイルなし
      return false
    }
    val slideConfigExistFolderPath = slideConfigPath.get().parent

    val slideFolderPath = appSlideProperties.rootPath.createRelative(command.slideId).file.absolutePath

    Files.move(slideConfigExistFolderPath, Paths.get(slideFolderPath))
    Files.delete(tempFolderPath)

    // TODO
    return true
  }
}

