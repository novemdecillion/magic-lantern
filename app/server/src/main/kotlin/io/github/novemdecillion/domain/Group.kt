package io.github.novemdecillion.domain

import org.jmolecules.architecture.onion.classical.DomainModelRing
import java.time.LocalDate
import java.util.*

/**
 * 講習
 */
data class Course(
  val courseId: UUID,
  val slideId: String,
  /**
   * 講習開始日
   */
  val startDate: LocalDate?,
  /**
   * 講習終了日
   */
  val endDate: LocalDate?,
)

/**
 * グループ
 */
@DomainModelRing
data class Group(
  val groupId: UUID,
  val groupOriginId: UUID,
  val groupGenerationId: UUID,
  val groupName: String,
  val memberIds: Set<String> = setOf(),
  val courses: Set<Course> = setOf()
)

/**
 * 世代
 */
data class GroupGeneration(
  val groupGenerationId: UUID,
  /**
   * 開始日
   */
  val startDate: LocalDate,

  val groupIds: Set<UUID> = setOf()
)

/**
 * グループ起源
 */
data class GroupOrigin (
  val groupOriginId: UUID,
  val groupGenerations: List<Pair<UUID, UUID>> = listOf()
)