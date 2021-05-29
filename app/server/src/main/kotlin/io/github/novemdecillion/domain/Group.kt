package io.github.novemdecillion.domain

import org.jmolecules.architecture.onion.classical.DomainModelRing
import java.time.LocalDate
import java.util.*

const val SYSTEM_REALM_ID: String = "!system"
val ROOT_GROUP_ID: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")
const val ROOT_GROUP_GENERATION_ID: Int = 0
const val GROUP_PATH_SEPARATOR = "/"

data class Member (
  val userId: UUID,
  val roles: Collection<Role>
)

/**
 * グループ
 */
interface IGroupCore {
  val groupId: UUID
  val groupName: String
}

data class GroupCore(
  override val groupId: UUID,
  override val groupName: String
): IGroupCore

interface IGroup : IGroupCore {
  val groupGenerationId: Int
  val parentGroupId: UUID?
}

@DomainModelRing
data class Group(
  override val groupId: UUID,
  override val groupGenerationId: Int,
  override val groupName: String,
  override val parentGroupId: UUID?
): IGroup

typealias GroupPath = List<GroupCore>

class GroupWithPath(
  group: Group,
  val path: GroupPath
): IGroup by group {
  fun groupIdPath() = path.map { it.groupId.toString() }.plus(groupId.toString()).joinToString(GROUP_PATH_SEPARATOR)
  fun groupNamePath() = path.map { it.groupName }.plus(groupName).joinToString(GROUP_PATH_SEPARATOR)
}

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

interface ILesson {
  val lessonId: UUID
  val slideId: String
}

/**
 * 講習
 */
data class Lesson(
  override val lessonId: UUID,
  override val slideId: String,

//  /**
//   * 講習開始日
//   */
//  val startDate: LocalDate?,
//  /**
//   * 講習終了日
//   */
//  val endDate: LocalDate?,
): ILesson

class LessonWithGroup(lesson: Lesson, val groupId: UUID, val group: GroupWithPath) : ILesson by lesson

//data class Study(val slideId: String, val lessons: List<LessonWithGroup>)