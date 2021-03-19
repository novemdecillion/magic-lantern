plugins {
  `kotlin-dsl`
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
}