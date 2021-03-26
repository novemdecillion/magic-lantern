package io.github.novemdecillion

//@DataNeo4jTest
//@Transactional(propagation = Propagation.NOT_SUPPORTED)
//class RepositoryTests {
//  companion object {
//    private val embeddedNeo4jServer: Neo4j = Neo4jBuilders.newInProcessBuilder()
//      .withDisabledServer()
//      .build()
//
//    @AfterAll
//    fun stopNeo4j() {
//      embeddedNeo4jServer.close()
//    }
//
//    @JvmStatic
//    @DynamicPropertySource
//    fun neo4jProperties(registry: DynamicPropertyRegistry) {
////      registry.add("spring.neo4j.uri") { embeddedNeo4jServer.boltURI() }
//      registry.add("spring.neo4j.uri") { "bolt://localhost:7687" }
//      registry.add("spring.neo4j.authentication.username") { "neo4j" }
//      registry.add("spring.neo4j.authentication.password") { null }
//    }
//  }
//
//  @MockBean
//  lateinit var appProp: AppSlideProperties
//
//  @BeforeEach
//  fun before(@Autowired client: Neo4jClient) {
//    client.query(
//      """
//      MATCH (n)
//      OPTIONAL MATCH (n)-[r]-()
//      DELETE n,r"""
//    ).run()
//  }
//
//  @Test
//  fun saveUser(@Autowired client: ReactiveNeo4jOperations) {
//    val saveSource = client.saveAll(
//      listOf(
//        User("user0001", "user", "0001"),
//        User("user0002", "user", "0002"),
//        User("user0003", "user", "0003")
//      )
//    )
//    val findSource = client.findAll(User::class.java).doOnEach { println(it) }
//
//    val users = saveSource
//      .thenMany(findSource)
//      .collectList()
//      .block()
//      ?: listOf()
//
//    Assertions.assertThat(users.size).isEqualTo(3)
//  }
//
//  @Test
//  fun saveUserAndRecord(@Autowired client: Neo4jOperations) {
//
//    client.save(
//      User("user0001", "user", "0001",
//        setOf(
//          CourseHistory("slide0001"),
//          CourseHistory("slide0002"),
//          CourseHistory("slide0003")
//        )))
//
//    val users = client.findAll(User::class.java).map { println(it); it }
//    Assertions.assertThat(users.size).isEqualTo(1)
//    Assertions.assertThat(users[0].courseHistories?.size).isEqualTo(3)
//
//    val records = client.findAll(CourseHistory::class.java).map { println(it); it }
//    Assertions.assertThat(records.size).isEqualTo(3)
//  }
//
//  @Test
//  fun saveUserAndRecord2(@Autowired client: Neo4jOperations) {
//
//    val user = client.save(User("user0001", "user", "0001"))
//
//    client.saveAll(
//      listOf(
//        CourseHistory("slide0001", user),
//        CourseHistory("slide0002", user),
//        CourseHistory("slide0003", user)
//      )
//    )
//
//    val users = client.findAll(User::class.java).map { println(it); it }
//    Assertions.assertThat(users.size).isEqualTo(1)
//    Assertions.assertThat(users[0].courseHistories?.size).isEqualTo(3)
//
//    val records = client.findAll(CourseHistory::class.java).map { println(it); it }
//    Assertions.assertThat(records.size).isEqualTo(3)
//  }
//
//}
