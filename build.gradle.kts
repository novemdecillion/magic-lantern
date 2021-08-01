import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  idea
  java
  kotlin("jvm") version "1.5.21" apply false
  kotlin("plugin.spring") version "1.5.21" apply false

  id("org.springframework.boot") version "2.5.3" apply false
  id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
}

subprojects {
  apply {
    plugin("io.spring.dependency-management")
    plugin("org.jetbrains.kotlin.jvm")
    plugin("org.jetbrains.kotlin.plugin.spring")
  }

  group = "io.github.novemdecillion"
  version = "1.0.0"
  java.sourceCompatibility = JavaVersion.VERSION_11
  extra["testcontainersVersion"] = "1.16.0"

  repositories {
    mavenCentral()
  }

  dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  }

  tasks.withType<KotlinCompile> {
    kotlinOptions {
      freeCompilerArgs = listOf("-Xjsr305=strict")
      jvmTarget = "11"
    }
  }

  tasks.withType<Test> {
    useJUnitPlatform()
  }

  the<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension>().apply {
    imports {
      mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
      mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
    }
  }
}

idea {
  module {
    isDownloadSources = true
  }
}
