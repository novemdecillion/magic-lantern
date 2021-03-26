dependencies {
  api(project(":utils:lang"))

  api("org.keycloak:keycloak-core:12.0.4")
  api("org.springframework:spring-webflux")
  testImplementation("org.apache.commons:commons-lang3")
  testImplementation("org.junit.jupiter:junit-jupiter")
}
