package io.github.novemdecillion.domain

import org.jmolecules.architecture.onion.classical.DomainModelRing
import java.time.LocalDate
import java.util.*

val ENTIRE_GROUP_ID: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")
val ENTIRE_GROUP_GENERATION_ID: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")
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

@DomainModelRing
data class Group(
  override val groupId: UUID,
  val groupOriginId: UUID,
  val groupGenerationId: UUID,
  override val groupName: String,
  val parentGroupId: UUID?
//  val members: Set<Member> = setOf(),
//  val courses: Set<Course> = setOf()
): IGroupCore

data class GroupWithPath(
  val group: Group,
  val path: List<GroupCore>
) {
  fun groupIdPath() = path.map { it.groupId.toString() }.plus(group.groupId.toString()).joinToString(GROUP_PATH_SEPARATOR)
  fun groupNamePath() = path.map { it.groupName }.plus(group.groupName).joinToString(GROUP_PATH_SEPARATOR)
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

/**
 * グループ起源
 */
data class GroupOrigin (
  val groupOriginId: UUID,
  val groupGenerations: List<Pair<UUID, UUID>> = listOf()
)