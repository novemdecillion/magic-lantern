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
  val loginId: String,
  val firstName: String,
  val lastName: String,
  val fullName: String,
  val groups: Set<UUID> = setOf(),
  val courseHistories: Set<CourseHistory> = setOf()
) {
  constructor(loginId: String, firstName: String, lastName: String)
    : this(loginId, firstName, lastName, "$firstName $lastName")
}
