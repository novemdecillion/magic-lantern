subprojects {
  apply {
    plugin("org.springframework.boot")
  }

  dependencies {
    "developmentOnly"("org.springframework.boot:spring-boot-devtools")
  }
}
