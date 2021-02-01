package io.github.novemdecillion.slide

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.assertj.core.api.Assertions
//import org.junit.jupiter.api.Test
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource
import org.springframework.core.io.FileSystemResource
import java.net.URL

class SlideConfigTest {
  fun loadSlideConfig(): SlideConfig {
    val objMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
    return objMapper.readValue(URL("file:../slides/sample/config.yml"), SlideConfig::class.java)
  }

  fun `読み込み`() {
    val slideConfig = loadSlideConfig()
    Assertions.assertThat(slideConfig.numberOfPages()).isEqualTo(13)
    Assertions.assertThat(slideConfig.perfectScore()).isEqualTo(100)

    var answer = mapOf(2 to mapOf(0 to listOf(0)), 4 to mapOf(0 to listOf(1, 2)))
    Assertions.assertThat(slideConfig.score(answer)).isEqualTo(100)

    answer = mapOf(2 to mapOf(0 to listOf(0)), 4 to mapOf(0 to listOf(1)))
    Assertions.assertThat(slideConfig.score(answer)).isEqualTo(70)

    answer = mapOf(2 to mapOf(0 to listOf(0)), 4 to mapOf(0 to listOf(0, 2)))
    Assertions.assertThat(slideConfig.score(answer)).isEqualTo(40)
  }
}