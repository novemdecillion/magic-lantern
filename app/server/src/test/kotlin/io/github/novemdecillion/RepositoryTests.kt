package io.github.novemdecillion

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.novemdecillion.adapter.db.*
import io.github.novemdecillion.domain.*
import org.assertj.core.api.Assertions
import org.flywaydb.core.Flyway
import org.jooq.DSLContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestConstructor
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.*

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@JooqTest
@AutoConfigureJson
@Transactional(rollbackFor = [Exception::class])
@Testcontainers
class RepositoryTests(
  dslContext: DSLContext,
  objectMapper: ObjectMapper,
  private val flyway: Flyway
) {
  companion object {
    val GROUP_MANAGER_USER_ID: UUID = UUID.fromString("00000000-0000-0000-0000-000000000010")
    val TEACHER_USER_ID: UUID = UUID.fromString("00000000-0000-0000-0000-000000000011")
    val STUDENT_A_USER_ID: UUID = UUID.fromString("00000000-0000-0000-0000-000000000012")
    val STUDENT_B_USER_ID: UUID = UUID.fromString("00000000-0000-0000-0000-000000000013")

    const val CURRENT_GENERATION_ID = 1

    @JvmStatic
    @Container
    val container = PostgreSQLContainer<Nothing>("postgres:13")

    @DynamicPropertySource
    @JvmStatic
    fun changeProperty(registry: DynamicPropertyRegistry): Unit {
      println("container.jdbcUrl = ${container.jdbcUrl}")
      registry.add("spring.datasource.url", container::getJdbcUrl)
      registry.add("spring.datasource.username", container::getUsername)
      registry.add("spring.datasource.password", container::getPassword)
    }
  }

  val accountRepository = AccountRepository(dslContext, objectMapper)
  val groupRepository = GroupRepository(dslContext, MaterializedViewRepository(dslContext))
  val lessonRepository = LessonRepository(dslContext)
  val studyRepository = StudyRepository(dslContext, objectMapper)

  @BeforeEach
  internal fun setUp() {
    flyway.clean()
    flyway.migrate()
  }

  @Test
  fun `AccountRepository正常系`() {

    Assertions.assertThat(accountRepository.selectCount()).isEqualTo(5)

    val users = accountRepository.selectAllWithAuthority()
    Assertions.assertThat(users.size).isEqualTo(5)
    val admin = users.first { it.userId == BUILT_IN_ADMIN_USER_ID }
    Assertions.assertThat(admin.userId).isEqualTo(BUILT_IN_ADMIN_USER_ID)
    Assertions.assertThat(admin.authorities.map { it.groupId }).isEqualTo(listOf(ROOT_GROUP_ID))
    Assertions.assertThat(admin.authorities.first().roles).containsExactlyElementsOf(Role.values().asIterable())

    accountRepository.selectByAccountNameAndRealmId(admin.accountName, SYSTEM_REALM_ID)
      ?.also { user ->
        Assertions.assertThat(user.accountId).isEqualTo(BUILT_IN_ADMIN_USER_ID)
      }

    // 各グループの所属人数を調査
    val groupCount = mutableMapOf<Pair<UUID, Int>, Int>()
    users
      .map { user ->
        user.authorities
          .forEach { auth ->
            groupCount.compute(auth.groupId to auth.groupGenerationInt) { _, v ->
              if (v != null) v + 1 else 1
            }
          }
      }

    groupCount
      .forEach { (groupId, generationId), memberCount ->
          val members = accountRepository.selectMemberByGroupTransitionId(groupId, generationId)
          Assertions.assertThat(members.size).isEqualTo(memberCount)
      }
  }

  @Test
  fun `GroupRepository正常系`() {
    managedGroupByUserId(BUILT_IN_ADMIN_USER_ID)
      .also { Assertions.assertThat(it).hasSize(6) }

    managedGroupByUserId(GROUP_MANAGER_USER_ID)
      .also { Assertions.assertThat(it).hasSize(5) }

    managedGroupByUserId(TEACHER_USER_ID)
      .also { Assertions.assertThat(it).hasSize(0) }

    val currentGenerationId = groupRepository.selectCurrentGroupGenerationId()
    Assertions.assertThat(currentGenerationId).isEqualTo(CURRENT_GENERATION_ID)

    changeGroupGeneration()

    managedGroupByUserId(BUILT_IN_ADMIN_USER_ID)
      .also {
        Assertions.assertThat(it).hasSize(6)
      }

    managedGroupByUserId(GROUP_MANAGER_USER_ID)
      .also { Assertions.assertThat(it).hasSize(3) }

    managedGroupByUserId(TEACHER_USER_ID)
      .also { Assertions.assertThat(it).hasSize(0) }
  }

    @Test
  fun `LessonRepository正常系`() {
    lessonRepository.selectByGroupTransitionIds(listOf(ROOT_GROUP_ID))
      .also {
        Assertions.assertThat(it).hasSize(1)
      }
  }


  @Test
  fun `StudyRepository正常系`() {

    studyRepository.selectByUserIdAndExcludeStatus(STUDENT_A_USER_ID)
      .also {
        Assertions.assertThat(it).hasSize(1)
      }
    studyRepository.selectNotStartStudyByUserId(STUDENT_A_USER_ID)
      .also {
        Assertions.assertThat(it).hasSize(0)
      }

    studyRepository.selectByUserIdAndExcludeStatus(STUDENT_B_USER_ID)
      .also {
        Assertions.assertThat(it).hasSize(0)
      }
    studyRepository.selectNotStartStudyByUserId(STUDENT_B_USER_ID)
      .also {
        Assertions.assertThat(it).hasSize(1)
      }
  }

  private fun changeGroupGeneration() {
    val generations = groupRepository.selectCurrentAndAvailableGeneration()
    Assertions.assertThat(generations).hasSize(2)
    groupRepository.updateCurrentGroupGeneration(
      generations[0].groupGenerationId!!,
      generations[1].groupGenerationId!!)
    val currentGenerationId = groupRepository.selectCurrentGroupGenerationId()
    Assertions.assertThat(currentGenerationId).isEqualTo(generations[1].groupGenerationId!!)
  }

  private fun managedGroupByUserId(userId: UUID): List<GroupWithPath> {
    return accountRepository.selectByIds(listOf(userId))
      .first()
      .authorities
      .filter { it.roles?.contains(Role.GROUP) == true }
      .map { it.groupId }
      .let { groupRepository.selectChildrenByIds(it) }
  }
}