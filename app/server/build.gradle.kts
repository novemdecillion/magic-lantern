import org.jooq.meta.jaxb.ForcedType
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
  id("io.github.novemdecillion.jooq-generator")
}

sourceSets.main {
//  java.srcDir("src/main/mbg")
  java.srcDir("src/main/jooq")
}

val graphqlSpringBootVersion = "11.0.0"

dependencies {
  implementation(project(":utils:keycloak-client"))

  // DB
  implementation("org.springframework.boot:spring-boot-starter-jooq")
  runtimeOnly("org.postgresql:postgresql")
  runtimeOnly("org.flywaydb:flyway-core")

  // Web
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
  implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity5")

  // Security
  implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

  // GraphQL
  implementation("com.graphql-java-kickstart:graphql-spring-boot-starter:${graphqlSpringBootVersion}")
  implementation("com.graphql-java-kickstart:voyager-spring-boot-starter:${graphqlSpringBootVersion}")
  implementation("com.graphql-java:graphql-java-extended-scalars:16.0.1")

  // JavaScript
  runtimeOnly("org.webjars:webjars-locator:0.40")
  runtimeOnly("org.webjars:bootstrap:5.0.1")
  runtimeOnly("org.webjars.npm:bootstrap-icons:1.5.0")

  implementation("org.jmolecules:jmolecules-onion-architecture:1.0.0")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
  implementation("org.asciidoctor:asciidoctorj:2.5.1")
  implementation("org.apache.commons:commons-math3:3.6.1")
  implementation("commons-io:commons-io:2.9.0")
  implementation("com.fasterxml.uuid:java-uuid-generator:4.0.1")
  implementation("net.lingala.zip4j:zip4j:2.7.0")

  implementation("org.apache.poi:poi:5.0.0")
  implementation("org.apache.poi:poi-ooxml:5.0.0")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
  testImplementation("org.jsoup:jsoup:1.13.1")
  testImplementation("org.testcontainers:postgresql")
  testImplementation("org.testcontainers:junit-jupiter")
}

tasks.getByName<BootJar>("bootJar") {
  requiresUnpack("**/asciidoctorj-*.jar")
}

jooqGenerator {
  driver = "org.testcontainers.jdbc.ContainerDatabaseDriver"
  url = "jdbc:tc:postgresql:11:///build-test"
  user = "admin"
  password = "password123"
  packageName ="io.github.novemdecillion.adapter.jooq"
  appendForcedTypes = listOf(
//    ForcedType()
//      .also {
//        it.isEnumConverter = true
//        it.includeExpression = """.*\.ROLE"""
//        it.userType = "io.github.novemdecillion.domain.Role[]"
//      },
    ForcedType()
      .also {
        it.isEnumConverter = true
        it.includeExpression = """STUDY\.STATUS"""
        it.userType = "io.github.novemdecillion.domain.StudyStatus"
      }  )
}
