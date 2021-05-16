package io.github.novemdecillion.domain

import org.jmolecules.architecture.onion.classical.DomainModelRing
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import java.time.OffsetDateTime
import java.util.*

enum class Role {
  ADMIN,
  GROUP,
  SLIDE,
  LESSON,
  STUDY,
  NONE
}

data class Study(
  val studyId: UUID? = null,
  val userId: UUID,
  val slideId: String,

  val progressRate: Int = 0,
  val answer: Map<Int, LinkedMultiValueMap<Int, Int>> = mapOf(),
  val score: Int = 0,
  val startAt: OffsetDateTime? = null,
  val endAt: OffsetDateTime? = null)

data class Authority(
  val groupId: UUID,
  val roles: Collection<Role>)

@DomainModelRing
data class User(
  val userId: UUID,
  val accountName: String,
  val userName: String,
  val email: String? = null,
  val realmId: String? = null,
  val enabled: Boolean,
  val authorities: Collection<Authority>? = null
)
