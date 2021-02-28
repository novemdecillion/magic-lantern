package io.github.novemdecillion.domain

import org.jmolecules.architecture.onion.classical.DomainModelRing
import org.springframework.data.annotation.Version
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship

@Node
@DomainModelRing
data class User(
  @field:Id
  val loginId: String,
  val firstName: String,
  val lastName: String,
  val fullName: String,
  @field:Relationship(type = "ACHIEVEMENT")
  val courseHistories: Set<CourseHistory>? = null,
  @field:Version
  var version: Long? = null
) {
  constructor(loginId: String, firstName: String, lastName: String)
    : this(loginId, firstName, lastName, "$firstName $lastName")
  constructor(loginId: String, firstName: String, lastName: String, courseHistories: Set<CourseHistory>)
    : this(loginId, firstName, lastName, "$firstName $lastName", courseHistories)
}
