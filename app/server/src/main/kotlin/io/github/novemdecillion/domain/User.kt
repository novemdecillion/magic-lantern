package io.github.novemdecillion.domain

import org.jmolecules.architecture.onion.classical.DomainModelRing
import java.util.*

data class CourseHistory(
  val loginId: String,
  val slideId: String,
)

data class Authority(
  val groupId: UUID,
  val role: String,
)

@DomainModelRing
data class User(
  val userId: UUID,
  val userName: String,
  val realm: String?,
  val enabled: Boolean,
  val authorities: Set<Authority> = setOf()
)
