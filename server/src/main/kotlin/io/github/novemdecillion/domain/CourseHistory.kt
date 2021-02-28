package io.github.novemdecillion.domain

import org.jmolecules.architecture.onion.classical.DomainModelRing
import org.springframework.data.annotation.Version
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship

@Node
@DomainModelRing
data class CourseHistory(
  @field:Id
  val slideId: String,
  @field:Relationship(type = "ACHIEVEMENT", direction = Relationship.Direction.INCOMING)
  val user: User? = null,
  @field:Version
  var version: Long? = null
)
