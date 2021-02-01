package io.github.novemdecillion.group

import io.github.novemdecillion.slide.SlideConfig
import org.jmolecules.architecture.onion.classical.DomainModelRing
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import java.util.*

data class Course(
  val courseId: UUID,
  val slide: SlideConfig
)
