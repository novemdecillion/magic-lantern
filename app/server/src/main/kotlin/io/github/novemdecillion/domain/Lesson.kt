package io.github.novemdecillion.domain

import org.jmolecules.architecture.onion.classical.DomainModelRing
import java.time.LocalDate
import java.util.*

interface ILesson {
  val lessonId: UUID
  val slideId: String
  val groupId: UUID
}

/**
 * 講習
 */
data class Lesson(
  override val lessonId: UUID,
  override val slideId: String,
  override val groupId: UUID

//  /**
//   * 講習開始日
//   */
//  val startDate: LocalDate?,
//  /**
//   * 講習終了日
//   */
//  val endDate: LocalDate?,
): ILesson

class LessonWithGroup(lesson: Lesson, val group: GroupWithPath) : ILesson by lesson

//data class Study(val slideId: String, val lessons: List<LessonWithGroup>)