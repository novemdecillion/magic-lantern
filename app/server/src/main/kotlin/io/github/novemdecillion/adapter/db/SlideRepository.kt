package io.github.novemdecillion.adapter.db

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import io.github.novemdecillion.adapter.web.AppSlideProperties
import io.github.novemdecillion.domain.*
import net.lingala.zip4j.ZipFile
import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.NameFileFilter
import org.apache.commons.io.filefilter.TrueFileFilter
import org.asciidoctor.Asciidoctor
import org.asciidoctor.Options
import org.springframework.dao.DuplicateKeyException
import org.springframework.dao.NonTransientDataAccessResourceException
import org.springframework.stereotype.Repository
import org.springframework.util.FileSystemUtils
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.file.*
import java.util.stream.Collectors
import kotlin.io.FileAlreadyExistsException

@Repository
class SlideRepository(private val appSlideProperties: AppSlideProperties) {
  companion object {
    val asciiDoctor: Asciidoctor = Asciidoctor.Factory.create()
    val yamlMapper: ObjectMapper = ObjectMapper(YAMLFactory()).findAndRegisterModules()
    const val TEMP_FOLDER_PREFIX = "temp-"
    const val TEMP_SLIDE_ZIP_FILENAME = "slide.zip"
    const val ENABLE_FLG_FILENAME = "enable"
  }

  val rootPath: Path = Paths.get(appSlideProperties.rootPath.file.absolutePath)

  @Synchronized
  fun insert(slideId: String, slideZipStream: InputStream) {
    val slideFolderPath = try {
      Files.createDirectory(Paths.get(appSlideProperties.rootPath.file.absolutePath, slideId))
    } catch (e: FileAlreadyExistsException) {
      throw DuplicateKeyException("$slideId フォルダは既に存在します。", e)
    } catch (e: SecurityException) {
      throw NonTransientDataAccessResourceException("$slideId フォルダの作成に失敗しました。", e)
    }

    val tempFolderPath = Files.createTempDirectory(rootPath, TEMP_FOLDER_PREFIX)
    val tempZipFilePath = tempFolderPath.resolve(TEMP_SLIDE_ZIP_FILENAME)
    FileOutputStream(tempZipFilePath.toFile())
      .use { slideZipStream.transferTo(it) }

    val zipFile = ZipFile(tempZipFilePath.toFile())
    zipFile.extractAll(tempFolderPath.toString())
    Files.delete(tempZipFilePath)

    val slideConfigPath = FileUtils.listFiles(tempFolderPath.toFile(), NameFileFilter(SLIDE_CONFIG_FILE_NAME), TrueFileFilter.INSTANCE)
      .firstOrNull()?.toPath()

    if (slideConfigPath == null) {
      FileSystemUtils.deleteRecursively(slideFolderPath)
      FileSystemUtils.deleteRecursively(tempFolderPath)
      throw FileNotFoundException("設定ファイル($SLIDE_CONFIG_FILE_NAME)が見つかりません。")
    }
    val slideConfigExistFolderPath = slideConfigPath.parent
    Files.move(slideConfigExistFolderPath, slideFolderPath, StandardCopyOption.REPLACE_EXISTING)
    FileUtils.deleteDirectory(tempFolderPath.toFile())
  }

  @Synchronized
  fun updateEnable(slideId: String, enable: Boolean) {
    val enablePath = rootPath.resolve("${slideId}/$ENABLE_FLG_FILENAME")
    if (enable && !enablePath.toFile().exists()) {
      Files.createFile(enablePath)
    } else if (!enable) {
      Files.deleteIfExists(enablePath)
    }
  }

  @Synchronized
  fun delete(slideId: String): Boolean {
    FileUtils.deleteDirectory(rootPath.resolve(slideId).toFile())
    return true
  }

  @Synchronized
  fun selectAll(): List<Slide> {
    return Files.list(rootPath)
      .filter { Files.isDirectory(it) }
      .filter { !it.fileName.toString().startsWith(TEMP_FOLDER_PREFIX) }
      .map {
        loadSlide(it.fileName.toString())
      }
      .collect(Collectors.toList())
      .filterNotNull()
  }

  @Synchronized
  fun selectByIds(slideIds: Collection<String>): List<Slide> {
    return slideIds
      .mapNotNull { loadSlide(it) }
  }

  @Synchronized
  fun selectById(slideId: String): Slide? {
    return loadSlide(slideId)
  }

  @Synchronized
  fun loadSlide(slideId: String): Slide? {
    val configResource = appSlideProperties.rootPath.createRelative("${slideId}/$SLIDE_CONFIG_FILE_NAME")
    if (!configResource.exists()) {
      return null
    }

    val enable = rootPath.resolve("${slideId}/$ENABLE_FLG_FILENAME").toFile().exists()

    val slideConfig = configResource.inputStream
      .use { inputStream ->
        yamlMapper.readValue(inputStream, SlideConfig::class.java)
      }

    val convertedChapters = slideConfig.chapters
      .map { chapter ->
        when (chapter) {
          is ExplainChapter -> {
            chapter.copy(
              pages = chapter.pages
                .map { page ->
                  val textType = page.textType ?: slideConfig.textType
                  page.copy(
                    text = convertTextOrNull(page.text, textType))
                })
          }
          is ExamChapter -> {
            val textType = chapter.textType ?: slideConfig.textType
            chapter.copy(
              questions = chapter.questions
                .map { question ->
                  question.copy(
                    text = convertText(question.text, textType),
                    comment = convertTextOrNull(question.comment, textType),
                    choices = question.choices.map { choice ->
                      choice.copy(text = convertText(choice.text, textType))
                    })
                })
          }
          is SurveyChapter -> {
            val textType = chapter.textType ?: slideConfig.textType
            chapter.copy(
              questions = chapter.questions
                .map { question ->
                  when (question) {
                    is SurveyQuestion -> question.copy(
                      text = convertText(question.text, textType),
                      choices = question.choices.map { convertText(it, textType) })
                    is TextareaSurveyQuestion -> question.copy(
                      text = convertText(question.text, textType))
                    else -> throw UnsupportedOperationException()
                  }
                })
          }
          else -> throw UnsupportedOperationException()
        }
      }
    return Slide(slideId, enable, slideConfig.copy(chapters = convertedChapters))
  }

  fun convertText(text: String, textType: TextType?): String {
    return when (textType) {
      TextType.AsciiDoc -> asciiDoctor.convert(text, Options.builder().build())
      else -> text
    }
  }

  fun convertTextOrNull(text: String?, textType: TextType?): String? {
    if (text.isNullOrBlank()) {
      return text
    }
    return when (textType) {
      TextType.AsciiDoc -> asciiDoctor.convert(text, Options.builder().build())
      else -> text
    }
  }

}