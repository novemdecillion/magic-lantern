import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
//import com.github.gradle.node.npm.task.NpmTask

plugins {
  id("org.springframework.boot") version "2.4.3"
  id("io.spring.dependency-management") version "1.0.11.RELEASE"
//  id("com.github.node-gradle.node") version "3.0.1"
  id("idea")
  kotlin("jvm") version "1.4.30"
  kotlin("plugin.spring") version "1.4.30"

  id("io.github.novemdecillion.mybatis-generator")
}

group = "io.github.novemdecillion"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

sourceSets.main {
  java.srcDir("src/main/mbg")
}

configurations {
  idea
  mybatisGenerator
}

repositories {
  mavenCentral()
}

val graphqlSpringBootVersion = "11.0.0"

dependencies {
  // Kotlin
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

  // DB
  implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:2.1.4")
  runtimeOnly("org.postgresql:postgresql")
  runtimeOnly("org.flywaydb:flyway-core")

  // Web
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
  implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity5")
  implementation(platform("org.springframework.cloud:spring-cloud-dependencies:2020.0.1"))
  implementation("org.springframework.cloud:spring-cloud-starter-gateway")

  // Security
  implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

  // GraphQL
  implementation("com.graphql-java-kickstart:graphql-kickstart-spring-boot-starter-webflux:${graphqlSpringBootVersion}")
  implementation("com.graphql-java-kickstart:graphql-kickstart-spring-boot-autoconfigure-tools:${graphqlSpringBootVersion}")
  implementation("com.graphql-java-kickstart:voyager-spring-boot-starter:${graphqlSpringBootVersion}")

  // JavaScript
  runtimeOnly("org.webjars:webjars-locator:0.40")
  runtimeOnly("org.webjars:bootstrap:5.0.0-beta2")
  runtimeOnly("org.webjars.npm:bootstrap-icons:1.3.0")

  developmentOnly("org.springframework.boot:spring-boot-devtools")

  implementation("org.jmolecules:jmolecules-onion-architecture:1.0.0")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
  implementation("org.asciidoctor:asciidoctorj:2.4.3")
  implementation("org.apache.commons:commons-math3:3.6.1")
  implementation("org.keycloak:keycloak-core:12.0.4")


  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
  testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
  testImplementation("org.jsoup:jsoup:1.13.1")
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


//tasks.register<NpmTask>("sassCompile") {
//  group = "npm"
//  dependsOn(tasks.npmInstall)
//  args.set(listOf("run", "sass:build"))
//}
//
//tasks.register<NpmTask>("sassWatchStart") {
//  group = "npm"
//  dependsOn(tasks.npmInstall)
//  args.set(listOf("run", "sass:watch:start"))
//}
//
//tasks.register<NpmTask>("sassWatchStop") {
//  group = "npm"
//  dependsOn(tasks.npmInstall)
//  args.set(listOf("run", "sass:watch:stop"))
//}
//
//node {
//  download.set(true)
//}


mybatisGenerator {
  driver = "org.testcontainers.jdbc.ContainerDatabaseDriver"
  url = "jdbc:tc:postgresql:11:///build-test"
  user = "admin"
  password = "password123"
  targetPackage ="io.github.novemdecillion.adapter.mybatis"
}

idea {
  module {
    isDownloadSources = true
    inheritOutputDirs = true
  }
}