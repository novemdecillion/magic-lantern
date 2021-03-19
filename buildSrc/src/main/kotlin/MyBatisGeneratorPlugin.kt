package io.github.novemdecillion

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.Location
import org.flywaydb.core.internal.configuration.ConfigUtils
import org.gradle.api.Plugin
import org.gradle.api.Project

import org.gradle.kotlin.dsl.*
import org.flywaydb.core.internal.jdbc.DriverDataSource
import org.mybatis.generator.api.MyBatisGenerator
import org.mybatis.generator.internal.DefaultShellCallback
import org.mybatis.generator.config.Configuration
import org.mybatis.generator.config.xml.ConfigurationParser
import java.io.File
import java.sql.DriverManager
import java.util.*

open class MyBatisGeneratorPlugin : Plugin<Project> {
  companion object {
    const val PLUGIN_NAME = "mybatisGenerator"
  }

  override fun apply(project: Project): Unit {
    project.extensions.create(PLUGIN_NAME, MyBatisGeneratorExtension::class)

    project.tasks {
      register(PLUGIN_NAME) {
        group = "novemdecillion"
        doLast {
          val extension = project.extensions.getByName(PLUGIN_NAME) as MyBatisGeneratorExtension

          Class.forName(extension.driver)
          DriverManager.getConnection(extension.url, extension.user, extension.password)
            .use {
              val flywayConfig: Map<String, String> = mapOf(
                ConfigUtils.DRIVER to extension.driver,
                ConfigUtils.URL to extension.url,
                ConfigUtils.USER to extension.user,
                ConfigUtils.PASSWORD to extension.password,
                ConfigUtils.LOCATIONS to "${Location.FILESYSTEM_PREFIX}${project.projectDir.absolutePath}/src/main/resources/db/migration"
              )

              val flyway = Flyway.configure().configuration(flywayConfig).load()
              flyway.migrate()

              val warnings: List<String> = ArrayList()
              val overwrite = true
              val extraProperties = Properties()
                .apply {
                  setProperty("connectionURL", extension.url)
                  setProperty("driverClass", extension.driver)
                  setProperty("userId", extension.user)
                  setProperty("password", extension.password)
                  setProperty("targetProject", if (extension.targetProject.isEmpty()) {
                    "${project.projectDir.absolutePath}/src/main/mbg"
                  } else {
                    "${project.projectDir.absolutePath}${extension.targetProject}"
                  })
                  setProperty("targetPackage", if (extension.targetPackage.isEmpty()) {
                    "${project.group}"
                  } else {
                    extension.targetPackage
                  })
                }

              val configFile = File("${project.projectDir.absolutePath}/mybatis-generator-config.xml")
              val cp = ConfigurationParser(extraProperties, warnings)
              val mbgConfig: Configuration = cp.parseConfiguration(configFile)


              val callback = DefaultShellCallback(overwrite)
              val myBatisGenerator = MyBatisGenerator(mbgConfig, callback, warnings)
              myBatisGenerator.generate(null)

              (flyway.configuration.dataSource as DriverDataSource).shutdownDatabase()
            }
        }
      }
    }
  }
}