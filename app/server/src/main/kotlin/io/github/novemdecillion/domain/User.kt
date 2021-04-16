package io.github.novemdecillion.domain

import org.jmolecules.architecture.onion.classical.DomainModelRing
import java.util.*

enum class Role {
  ADMIN,
  MANAGER,
  STUDENT
}

data class CourseHistory(
  val loginId: String,
  val slideId: String,
)

data class Authority(
  val groupId: UUID,
  val role: Role,
)

@DomainModelRing
data class User(
  val userId: UUID,
  val accountName: String,
  val userName: String,
  val email: String?,
  val realmId: String?,
  val enabled: Boolean,
  val authorities: Set<Authority>? = null
)
