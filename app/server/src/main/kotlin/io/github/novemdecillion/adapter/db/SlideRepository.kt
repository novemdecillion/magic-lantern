package io.github.novemdecillion.adapter.db

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import io.github.novemdecillion.adapter.jooq.tables.records.SlideRecord
import io.github.novemdecillion.adapter.jooq.tables.references.SLIDE
import io.github.novemdecillion.adapter.web.AppSlideProperties
import io.github.novemdecillion.slide.SLIDE_CONFIG_FILE_NAME
import io.github.novemdecillion.slide.Slide
import io.github.novemdecillion.slide.SlideConfig
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import org.yaml.snakeyaml.Yaml

@Repository
class SlideRepository(private val dslContext: DSLContext, private val appSlideProperties: AppSlideProperties) {
  companion object {
//    val yaml = Yaml()
    val yamlMapper = ObjectMapper(YAMLFactory()).findAndRegisterModules()
  }

  fun insert(slideId: String) {
    dslContext.insertInto(SLIDE).set(SlideRecord(slideId)).execute()
  }

  fun selectAll(): List<Slide> {
    return dslContext.selectFrom(SLIDE)
      .fetch { record -> record.slideId }
      .map { loadSlide(it) }
  }

  fun selectByIds(slideIds: Collection<String>): List<Slide> {
    return dslContext.selectFrom(SLIDE)
      .where(SLIDE.SLIDE_ID.`in`(slideIds))
      .fetch { record -> record.slideId }
      .map { loadSlide(it) }
  }

  fun loadSlide(slideId: String): Slide {
    val configResource = appSlideProperties.rootPath.createRelative("${slideId}/$SLIDE_CONFIG_FILE_NAME")
    val slideConfig = configResource.inputStream
      .use { inputStream ->
        yamlMapper.readValue(inputStream, SlideConfig::class.java)
      }
    return Slide(slideId, slideConfig)
  }

}