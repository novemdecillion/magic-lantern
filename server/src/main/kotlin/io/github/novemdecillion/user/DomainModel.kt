package io.github.novemdecillion.user

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
  val records: Set<Record>? = null,
  @field:Version
  var version: Long? = null
) {
  constructor(loginId: String, firstName: String, lastName: String)
    : this(loginId, firstName, lastName, "$firstName $lastName")
  constructor(loginId: String, firstName: String, lastName: String, records: Set<Record>)
    : this(loginId, firstName, lastName, "$firstName $lastName", records)
}

@Node
@DomainModelRing
data class Record(
  @field:Id
  val slideId: String,
  @field:Relationship(type = "ACHIEVEMENT", direction = Relationship.Direction.INCOMING)
  val user: User? = null,
  @field:Version
  var version: Long? = null
)
