plugins {
  `kotlin-dsl`
  `java-gradle-plugin`
}

kotlinDslPluginOptions {
  experimentalWarning.set(false)
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(platform("org.testcontainers:testcontainers-bom:1.15.2"))
  implementation("org.testcontainers:postgresql")
  implementation("org.postgresql:postgresql:42.1.4")
  implementation("org.flywaydb:flyway-core:7.7.0")
  implementation("org.mybatis.generator:mybatis-generator-core:1.4.0")

  implementation("org.jooq:jooq-codegen:3.14.8")

  implementation("org.slf4j:slf4j-simple:1.7.30")
}

gradlePlugin {
  plugins {
    create("mybatisGenerator") {
      id = "io.github.novemdecillion.mybatis-generator"
      implementationClass = "io.github.novemdecillion.MyBatisGeneratorPlugin"
    }
    create("jooqGenerator") {
      id = "io.github.novemdecillion.jooq-generator"
      implementationClass = "io.github.novemdecillion.JooqGeneratorPlugin"
    }

  }
}
