package io.github.novemdecillion.adapter.api

import com.fasterxml.jackson.annotation.JsonIgnore
import graphql.kickstart.tools.GraphQLMutationResolver
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.kickstart.tools.GraphQLResolver
import graphql.schema.DataFetchingEnvironment
import io.github.novemdecillion.adapter.db.*
import io.github.novemdecillion.adapter.id.IdGeneratorService
import io.github.novemdecillion.adapter.jooq.tables.pojos.GroupGenerationEntity
import io.github.novemdecillion.domain.*
import io.github.novemdecillion.usecase.GroupUseCase
import io.github.novemdecillion.utils.lang.logger
import org.apache.commons.lang3.StringUtils
import org.apache.poi.EncryptedDocumentException
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.dataloader.MappedBatchLoader
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import javax.servlet.http.Part

@Component
class GroupApi(
  private val groupRepository: GroupRepository,
  private val realmRepository: RealmRepository,
  private val userRepository: AccountRepository,
  private val authorityRepository: GroupAuthorityRepository,
  private val idGeneratorService: IdGeneratorService,
  private val groupUseCase: GroupUseCase
) : GraphQLQueryResolver, GraphQLMutationResolver {
  companion object {
    val log = logger()
  }

  @Component
  class GroupMemberCountLoader(private val authorityRepository: GroupAuthorityRepository) :
    MappedBatchLoader<IGroup, Int>, LoaderFunctionMaker<IGroup, Int> {
    override fun load(keys: Set<IGroup>): CompletionStage<Map<IGroup, Int>> {
      val groupToCountMap = keys
        .groupBy { it.groupGenerationId }
        .flatMap { (groupGenerationId, groups) ->
          // TODO 一回ですませるようにする
          authorityRepository.selectCount(groups.map { it.groupId }, groupGenerationId)
            .map { (groupId, count) ->
              groups.first { it.groupId == groupId } to count
            }
        }
        .toMap()
        .toMutableMap()

      keys
        .forEach { key ->
          groupToCountMap.computeIfAbsent(key) { 0 }
        }
      return CompletableFuture.completedFuture(groupToCountMap)
    }
  }

  @Component
  class GroupStudentCountLoader(private val authorityRepository: GroupAuthorityRepository) :
    MappedBatchLoader<GroupKey, Int>, LoaderFunctionMaker<GroupKey, Int> {
    override fun load(keys: Set<GroupKey>): CompletionStage<Map<GroupKey, Int>> {
      val groupToCountMap = mutableMapOf<GroupKey, Int>()
      keys.forEach { groupToCountMap[it] = 0 }

      keys
        .groupBy { it.groupGenerationId }
        .forEach { (groupGenerationId, groups) ->
          authorityRepository.selectCount(groups.map { it.groupId }, groupGenerationId, Role.STUDY)
            .forEach { (groupId, count) ->
              groupToCountMap[GroupKey(groupGenerationId, groupId)] = count
            }
        }

      return CompletableFuture.completedFuture(groupToCountMap)
    }
  }

  data class GroupKey(
    val groupGenerationId: Int,
    val groupId: UUID,
  )

  data class AddGroupCommand(
    val currentGenerationId: Int,
    val targetGenerationId: Int?,
    val groupName: String,
    val parentGroupId: UUID
  )

  data class UpdateGroupCommand(
    val currentGenerationId: Int,
    val targetGenerationId: Int?,
    val groupId: UUID,
    val groupName: String
  )

  data class DeleteGroupCommand(
    val currentGenerationId: Int,
    val targetGenerationId: Int?,
    val groupId: UUID
  )

  data class GroupMemberCommand(
    val currentGenerationId: Int,
    val targetGenerationId: Int?,
    val groupId: UUID,
    val userIds: List<UUID>,
    val roles: List<Role>
  )

  data class DeleteGroupMemberCommand(
    val currentGenerationId: Int,
    val targetGenerationId: Int?,
    val groupId: UUID,
    val userIds: List<UUID>
  )

  data class SwitchGroupGenerationCommand(
    val currentGenerationId: Int,
    val nextGenerationId: Int
  )

  data class ImportGroupGenerationCommand(
    val groupGenerationId: Int?,
    @field:JsonIgnore
    val generationFile: Part? = null
  )

  data class ImportGroupGenerationResult(
    val warnings: List<String>?
  )

  @GraphQLApi
  fun currentGroupGenerationId(environment: DataFetchingEnvironment): Int {
    return environment.currentGroupGenerationId()
  }

  @GraphQLApi
  fun currentAndNextGroupGenerations(): List<GroupGenerationEntity> {
    return groupRepository.selectCurrentAndAvailableGeneration()
  }

  enum class GroupApiResult(val message: String = StringUtils.EMPTY) {
    UnrecognizedGenerationChanged("既に世代切替されていたため、処理を中止しました。"),
    ImportFileNotFount,
    ImportFileNotSupportFormat,
    ImportFileEncrypted,
    ImportFileCanNotRead,
  }

  @GraphQLApi
  fun switchGroupGeneration(
    command: SwitchGroupGenerationCommand,
    environment: DataFetchingEnvironment
  ): Boolean {
    if (command.nextGenerationId == environment.currentGroupGenerationId()) {
      // 既に世代が切替後なら
      return true
    } else if (command.currentGenerationId != environment.currentGroupGenerationId()) {
      // 切替後ではない上に、切替元も異なる
      val apiException = ApiException(GroupApiResult.UnrecognizedGenerationChanged.message, GroupApiResult.UnrecognizedGenerationChanged.name)
      log.error(apiException.message)
      throw apiException
    }
    try {
      groupRepository.updateCurrentGroupGeneration(command.currentGenerationId, command.nextGenerationId)
    } catch (ex: Exception) {
      val apiException = ApiException("世代の切替に失敗しました。")
      log.error(apiException.message, ex)
      throw apiException
    }
    return true
  }

  @GraphQLApi
  fun authoritativeGroupsByUser(role: Role, environment: DataFetchingEnvironment): List<GroupWithPath> {
    return environment.currentUser().authorities
      .filter { it.roles?.contains(role) == true }
      .map { it.groupId }
      .let {
        groupRepository.selectByIds(it, environment.currentGroupGenerationId())
      }
  }

  @GraphQLApi
  fun effectiveGroupsByUser(role: Role, environment: DataFetchingEnvironment): List<GroupWithPath> {
    return environment.currentUser().authorities
      .filter { it.roles?.contains(role) == true }
      .map { it.groupId }
      .let {
        groupRepository.selectChildrenByIds(it, environment.currentGroupGenerationId())
      }
  }

  @GraphQLApi
  fun nextGenerationGroups(): List<GroupWithPath> {
    val generations = groupRepository.selectCurrentAndAvailableGeneration()
    if (generations.size != 2) {
      return listOf()
    }
    return groupRepository.select(generations.last().groupGenerationId)
  }

  @GraphQLApi
  fun exportGroupGeneration(groupGenerationId: Int?, environment: DataFetchingEnvironment): String {
    // TODO 現行グループIDチェック
    val resolvedGenerationId = groupGenerationId ?: environment.currentGroupGenerationId()

    val groups = groupRepository.select(resolvedGenerationId)
    val workbook = groupUseCase.exportGroupGeneration(groups) { groupId, _ ->
      userRepository.selectMemberByGroupTransitionId(groupId, resolvedGenerationId)
    }

    val workbookBinary = ByteArrayOutputStream()
      .use { outStream ->
        workbook.write(outStream)
        outStream.toByteArray()
      }
    return Base64.getEncoder().encodeToString(workbookBinary)
  }

  @GraphQLApi
  fun importGroupGeneration(
    command: ImportGroupGenerationCommand,
    environment: DataFetchingEnvironment
  ): ImportGroupGenerationResult {
    val generationFile: Part = (environment.variables["command"] as? Map<String, Part>)
      ?.get("generationFile")
      ?: run {
        val apiException = ApiException("グループ構成ファイルを指定してください。", GroupApiResult.ImportFileNotFount.name)
        log.error(apiException.message)
        throw apiException
      }

    if (generationFile.contentType != "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") {
      val apiException = ApiException("グループ構成ファイルがExcel(xlsx形式)ではありません。", GroupApiResult.ImportFileNotSupportFormat.name)
      log.error(apiException.message)
      throw apiException
    }
    val workbook = try {
      WorkbookFactory.create(generationFile.inputStream)
    } catch (ex: Exception) {
      val apiException = ApiException("グループ構成ファイルの読込に失敗しました。", GroupApiResult.ImportFileCanNotRead.name)
      if (ex is EncryptedDocumentException) {
        apiException.message = "グループ構成ファイルのパスワードを解除してください。"
        apiException.setCode(GroupApiResult.ImportFileEncrypted.name)
      }
      log.error(apiException.message, ex)
      throw apiException
    }

    val generations = groupRepository.selectCurrentAndAvailableGeneration()
    val resolvedGenerationId = when {
      (generations.size == 2) ->
        if ((command.groupGenerationId == null)
          || (generations.last().groupGenerationId != command.groupGenerationId)
        ) {
          // 世代IDの不一致
          val apiException = ApiException(GroupApiResult.UnrecognizedGenerationChanged.message, GroupApiResult.UnrecognizedGenerationChanged.name)
          log.error(apiException.message)
          throw apiException
        } else {
          command.groupGenerationId
        }
      else ->
        // 2件じゃなければ1件の筈
        if (command.groupGenerationId == null) {
          groupRepository.createNewGeneration()
        } else {
          // 世代IDの不一致
          val apiException = ApiException(GroupApiResult.UnrecognizedGenerationChanged.message, GroupApiResult.UnrecognizedGenerationChanged.name)
          log.error(apiException.message)
          throw apiException
        }
    }

    val warnings = mutableListOf<String>()

    groupUseCase.importGroupGeneration(resolvedGenerationId, workbook,
      { groups, groupGenerationId ->
        groupRepository.deleteAndInsertGroups(groupGenerationId, groups)
      }, { userId, authorities ->
        authorityRepository.insertAuthorities(userId, authorities)
      }, { warningMessage ->
        warnings.add(warningMessage)
      })

    return ImportGroupGenerationResult(warnings.ifEmpty { null })
  }

  @GraphQLApi
  fun isTopManageableGroupByUser(groupId: UUID, environment: DataFetchingEnvironment): Boolean {
    val manageableGroupIds = environment.currentUser().authorities
      .filter { it.roles?.contains(Role.GROUP) == true }
      .map { it.groupId }
    if (manageableGroupIds.isNullOrEmpty()
      || !manageableGroupIds.contains(groupId)
    ) {
      return false
    }
    val groups = groupRepository.selectByIds(manageableGroupIds, environment.currentGroupGenerationId())
      .map { group -> group.groupIdPath() to group.groupId }
    return groups
      .filter { group ->
        groups
          .firstOrNull { path ->
            return@firstOrNull (path.second != group.second)
              && path.first.contains(group.first)
          }
          ?.let { false }
          ?: true
      }
      .map { it.second }
      .contains(groupId)
  }

  @GraphQLApi
  fun group(groupId: UUID, groupGenerationId: Int?, environment: DataFetchingEnvironment): GroupWithPath? {
    return groupRepository.selectById(groupId, groupGenerationId)
  }

  @GraphQLApi
  fun addGroup(command: AddGroupCommand, environment: DataFetchingEnvironment): Boolean {
    // TODO 現行グループIDチェック
    val resolvedGenerationId = command.targetGenerationId ?: environment.currentGroupGenerationId()
    val newGroup = Group(
      groupId = idGeneratorService.generate(),
      groupGenerationId = resolvedGenerationId,
      groupName = command.groupName,
      parentGroupId = command.parentGroupId
    )

    groupRepository.insertGroup(newGroup)
    return true
  }

  @GraphQLApi
  fun updateGroup(command: UpdateGroupCommand, environment: DataFetchingEnvironment): Boolean {
    // TODO 現行グループIDチェック
    val resolvedGenerationId = command.targetGenerationId ?: environment.currentGroupGenerationId()
    groupRepository.updateGroup(command.groupId, command.groupName, resolvedGenerationId)
    return true
  }

  @GraphQLApi
  fun deleteGroup(command: DeleteGroupCommand, environment: DataFetchingEnvironment): Boolean {
    // TODO 現行グループIDチェック
    val resolvedGenerationId = command.targetGenerationId ?: environment.currentGroupGenerationId()
    groupRepository.deleteGroup(command.groupId, resolvedGenerationId)
    return true
  }

  @GraphQLApi
  fun groupMembers(groupId: UUID, groupGenerationId: Int?, environment: DataFetchingEnvironment): List<User> {
    return userRepository.selectMemberByGroupTransitionId(groupId, groupGenerationId)
  }

  @GraphQLApi
  fun groupAppendableMembers(groupId: UUID, groupGenerationId: Int?, environment: DataFetchingEnvironment): List<User> {
    return userRepository.selectAppendableMemberByGroupTransitionId(groupId, groupGenerationId)
  }

  @GraphQLApi
  fun addGroupMember(command: GroupMemberCommand, environment: DataFetchingEnvironment): Boolean {
    // TODO 現行グループIDチェック

    val resolvedGenerationId = when (command.groupId) {
        ROOT_GROUP_ID -> return false
        else -> command.targetGenerationId ?: environment.currentGroupGenerationId()
    }

    command.userIds
      .windowed(100, 100, true)
      .forEach { userIds ->
        val authorities = userIds
          .map {
            it to listOf(Authority(command.groupId, resolvedGenerationId, command.roles))
          }
        authorityRepository.insertAuthorities(authorities)
      }
    return true
  }

  @GraphQLApi
  fun updateGroupMember(command: GroupMemberCommand, environment: DataFetchingEnvironment): Boolean {
    // TODO 現行グループIDチェック

    when (command.groupId) {
      ROOT_GROUP_ID -> {
        command.userIds
          .windowed(1000, 1000, true)
          .forEach { userIds ->
            authorityRepository.updateAuthorities(
              userIds, ROOT_GROUP_ID, ROOT_GROUP_GENERATION_ID,
              listOf(Role.LESSON, Role.STUDY), command.roles
            )
          }
      }
      else -> {
        command.targetGenerationId ?: environment.currentGroupGenerationId()
        command.userIds
          .windowed(1000, 1000, true)
          .forEach { userIds ->
            authorityRepository.updateAuthorities(
              userIds, command.groupId,
              environment.currentGroupGenerationId(),
              command.roles
            )
          }
      }
    }
    return true
  }

  @GraphQLApi
  fun deleteGroupMember(command: DeleteGroupMemberCommand, environment: DataFetchingEnvironment): Boolean {
    // TODO 現行グループIDチェック

    val resolvedGenerationId = when (command.groupId) {
        ROOT_GROUP_ID -> return false
        else -> command.targetGenerationId ?: environment.currentGroupGenerationId()
    }

    command.userIds
      .windowed(100, 100, true)
      .forEach { userIds ->
        authorityRepository.deleteAuthorities(command.groupId, userIds, resolvedGenerationId)
      }
    return true
  }
}

@Component
class GroupWithPathResolver : GraphQLResolver<GroupWithPath> {
  fun memberCount(group: GroupWithPath, environment: DataFetchingEnvironment): CompletableFuture<Int> {
    val loader = environment.dataLoader(GroupApi.GroupMemberCountLoader::class)
    return loader.load(group)
  }
}
