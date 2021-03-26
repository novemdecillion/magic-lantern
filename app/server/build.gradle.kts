plugins {
  id("io.github.novemdecillion.mybatis-generator")
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
//  implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:2.1.4")
//  implementation("org.mybatis.dynamic-sql:mybatis-dynamic-sql:1.2.1")
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

  // JavaScript
  runtimeOnly("org.webjars:webjars-locator:0.40")
  runtimeOnly("org.webjars:bootstrap:5.0.0-beta2")
  runtimeOnly("org.webjars.npm:bootstrap-icons:1.3.0")

  implementation("org.jmolecules:jmolecules-onion-architecture:1.0.0")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
  implementation("org.asciidoctor:asciidoctorj:2.4.3")
  implementation("org.apache.commons:commons-math3:3.6.1")


  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
  testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
  testImplementation("org.jsoup:jsoup:1.13.1")
}

mybatisGenerator {
  driver = "org.testcontainers.jdbc.ContainerDatabaseDriver"
  url = "jdbc:tc:postgresql:11:///build-test"
  user = "admin"
  password = "password123"
  targetPackage ="io.github.novemdecillion.adapter.mybatis"
}

jooqGenerator {
  driver = "org.testcontainers.jdbc.ContainerDatabaseDriver"
  url = "jdbc:tc:postgresql:11:///build-test"
  user = "admin"
  password = "password123"
  packageName ="io.github.novemdecillion.adapter.jooq"
}
