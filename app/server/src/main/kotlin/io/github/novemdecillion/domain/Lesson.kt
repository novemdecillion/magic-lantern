package io.github.novemdecillion.domain

import org.jmolecules.architecture.onion.classical.DomainModelRing
import java.time.LocalDate
import java.util.*

/**
 * 講習
 */
data class Lesson(
  val lessonId: UUID,
  val slideId: String,
  val groupId: UUID

//  /**
//   * 講習開始日
//   */
//  val startDate: LocalDate?,
//  /**
//   * 講習終了日
//   */
//  val endDate: LocalDate?,
)

data class LessonWithGroup(val lesson: Lesson, val group: GroupWithPath)