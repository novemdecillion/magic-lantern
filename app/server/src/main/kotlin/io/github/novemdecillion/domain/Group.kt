package io.github.novemdecillion.domain

import java.util.*

const val SYSTEM_REALM_ID: String = "!system"
val ROOT_GROUP_ID: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")
const val ROOT_GROUP_GENERATION_ID: Int = 0
const val GROUP_PATH_SEPARATOR = "\t"

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
): ILesson

class LessonWithGroup(lesson: Lesson, val group: GroupWithPath) : ILesson by lesson
