package io.github.novemdecillion

import io.github.novemdecillion.slide.web.AppSlideProperties
import io.github.novemdecillion.user.Record
import io.github.novemdecillion.user.User
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.neo4j.harness.Neo4j
import org.neo4j.harness.Neo4jBuilders
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.neo4j.core.*
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@DataNeo4jTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class RepositoryTests {
  companion object {
    private val embeddedNeo4jServer: Neo4j = Neo4jBuilders.newInProcessBuilder()
      .withDisabledServer()
      .build()

    @AfterAll
    fun stopNeo4j() {
      embeddedNeo4jServer.close()
    }

    @JvmStatic
    @DynamicPropertySource
    fun neo4jProperties(registry: DynamicPropertyRegistry) {
//      registry.add("spring.neo4j.uri") { embeddedNeo4jServer.boltURI() }
      registry.add("spring.neo4j.uri") { "bolt://localhost:7687" }
      registry.add("spring.neo4j.authentication.username") { "neo4j" }
      registry.add("spring.neo4j.authentication.password") { null }
    }
  }

  @MockBean
  lateinit var appProp: AppSlideProperties

  @BeforeEach
  fun before(@Autowired client: Neo4jClient) {
    client.query(
      """
      MATCH (n)
      OPTIONAL MATCH (n)-[r]-()
      DELETE n,r"""
    ).run()
  }

  @Test
  fun saveUser(@Autowired client: ReactiveNeo4jOperations) {
    val saveSource = client.saveAll(
      listOf(
        User("user0001", "user", "0001"),
        User("user0002", "user", "0002"),
        User("user0003", "user", "0003")
      )
    )
    val findSource = client.findAll(User::class.java).doOnEach { println(it) }

    val users = saveSource
      .thenMany(findSource)
      .collectList()
      .block()
      ?: listOf()

    Assertions.assertThat(users.size).isEqualTo(3)
  }

  @Test
  fun saveUserAndRecord(@Autowired client: Neo4jOperations) {

    client.save(
      User("user0001", "user", "0001",
        setOf(
          Record("slide0001"),
          Record("slide0002"),
          Record("slide0003")
        )))

    val users = client.findAll(User::class.java).map { println(it); it }
    Assertions.assertThat(users.size).isEqualTo(1)
    Assertions.assertThat(users[0].records?.size).isEqualTo(3)

    val records = client.findAll(Record::class.java).map { println(it); it }
    Assertions.assertThat(records.size).isEqualTo(3)
  }

  @Test
  fun saveUserAndRecord2(@Autowired client: Neo4jOperations) {

    val user = client.save(User("user0001", "user", "0001"))

    client.saveAll(
      listOf(
        Record("slide0001", user),
        Record("slide0002", user),
        Record("slide0003", user)
      )
    )

    val users = client.findAll(User::class.java).map { println(it); it }
    Assertions.assertThat(users.size).isEqualTo(1)
    Assertions.assertThat(users[0].records?.size).isEqualTo(3)

    val records = client.findAll(Record::class.java).map { println(it); it }
    Assertions.assertThat(records.size).isEqualTo(3)
  }

}
