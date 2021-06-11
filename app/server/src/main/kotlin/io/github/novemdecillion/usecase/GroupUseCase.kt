package io.github.novemdecillion.usecase

import io.github.novemdecillion.adapter.id.IdGeneratorService
import io.github.novemdecillion.domain.*
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellReference
import org.springframework.stereotype.Component
import java.lang.Integer.max
import java.util.*

@Component
class GroupUseCase(val idGeneratorService: IdGeneratorService) {
  companion object {
    const val SHEET_NAME = "グループ構成"
    const val GROUP_ID_ROW = 0
    const val ROOT_GROUP_NAME_ROW = 0
    const val GROUP_NAME_START_ROW = ROOT_GROUP_NAME_ROW + 1

    const val USER_ID_COL = 0
    const val USER_NAME_COL = 1
    const val USER_EMAIL_COL = 2
    const val ROOT_GROUP_COL = 3
    const val GROUP_START_COL = ROOT_GROUP_COL + 1

    const val GROUP_HIERARCHY_HEADER = "グループ階層"
    const val GROUP_ID_HEADER = "グループID"
    const val USER_ID_HEADER = "ユーザID"
    const val USER_NAME_HEADER = "ユーザ名"
    const val USER_EMAIL_HEADER = "メールアドレス"
  }

  class GroupNode(group: IGroup, var layer: Int = 0, val children: MutableList<GroupNode> = mutableListOf()): IGroup by group

  fun exportGroupGeneration(groups: Collection<IGroup>, memberSupplier: (groupId: UUID, groupGenerationId: Int)->Collection<User>): Workbook {
    require(groups.isNotEmpty()) { "グループは1件以上存在する筈" }

    val workbook = WorkbookFactory.create(true)

    val sheet = workbook.createSheet(SHEET_NAME)

    val groupIdToGroupMap = groups.associate { it.groupId to GroupNode(it) }
    groupIdToGroupMap
      .filter { it.value.parentGroupId != null }
      .forEach {
        groupIdToGroupMap[it.value.parentGroupId]?.children?.add(it.value)
      }

    val groupsForHeader: MutableList<GroupNode> = mutableListOf()
    enumerateGroup(groupIdToGroupMap[ROOT_GROUP_ID]!!, 0, groupsForHeader)

    check(groupsForHeader.first().groupId == ROOT_GROUP_ID) { "ルートグループは先頭になる筈。" }

    val maxLayer = groupsForHeader.maxOf { it.layer }
    val groupHeaderStyle = workbook.createCellStyle()
      .also {
        it.fillPattern = FillPatternType.SOLID_FOREGROUND
        it.fillForegroundColor = IndexedColors.LIME.index
      }
    val groupNameRows = (0..maxLayer)
      .map { rowIndex ->
        sheet.createRow(rowIndex)
          .also {
            if (rowIndex == 0) {
              it.createCell(ROOT_GROUP_COL - 1)?.styleAndValue(GROUP_HIERARCHY_HEADER, groupHeaderStyle)
            } else {
              it.createCell(ROOT_GROUP_COL - 1)?.cellStyle = groupHeaderStyle
            }
          }
      }
    val groupIdRow = sheet.createRow(maxLayer + 1)
    groupIdRow.createCell(ROOT_GROUP_COL - 1).styleAndValue(GROUP_ID_HEADER, groupHeaderStyle)
    val userStartRowNum = maxLayer + 3

    // グループヘッダ行の出力
    groupsForHeader
      .forEachIndexed { index, groupNode ->
        groupNameRows[groupNode.layer].createCell(index + ROOT_GROUP_COL).setCellValue(groupNode.groupName)
        groupIdRow.createCell(index + ROOT_GROUP_COL).setCellValue(groupNode.groupId.toString())
      }

    // ユーザ列の出力
    val userHeaderRow = sheet.createRow(userStartRowNum - 1)
    val userHeaderStyle = workbook.createCellStyle()
      .also {
        it.fillPattern = FillPatternType.SOLID_FOREGROUND
        it.fillForegroundColor = IndexedColors.SKY_BLUE.index
      }
    userHeaderRow.createCell(USER_ID_COL).styleAndValue(USER_ID_HEADER, userHeaderStyle)
    userHeaderRow.createCell(USER_NAME_COL).styleAndValue(USER_NAME_HEADER, userHeaderStyle)
    userHeaderRow.createCell(USER_EMAIL_COL).styleAndValue(USER_EMAIL_HEADER, userHeaderStyle)

    val userIdToRowMap = memberSupplier(ROOT_GROUP_ID, ROOT_GROUP_GENERATION_ID)
      .mapIndexed { index, user ->
        val userRow = sheet.createRow(userStartRowNum + index)
        userRow.createCell(USER_ID_COL).setCellValue(user.userId.toString())
        userRow.createCell(USER_NAME_COL).setCellValue(user.userName)
        userRow.createCell(USER_EMAIL_COL).setCellValue(user.email)

        // ルートグループの権限出力
        val roleNames = user.authorities.firstOrNull { it.groupId == ROOT_GROUP_ID }?.roleNames()
        userRow.createCell(ROOT_GROUP_COL).setCellValue(roleNames)

        user.userId to userRow
      }
      .toMap()

    groupsForHeader
      .forEachIndexed { colIndex, groupNode ->
        if (colIndex != 0) {
          memberSupplier(groupNode.groupId, groupNode.groupGenerationId)
            .mapIndexed { _, user ->
              val roleNames = user.authorities.firstOrNull { it.groupId == groupNode.groupId }?.roleNames()
              userIdToRowMap[user.userId]?.createCell(ROOT_GROUP_COL + colIndex)?.setCellValue(roleNames)
            }
        }
      }

    (0 until (ROOT_GROUP_COL + groupsForHeader.size)).forEach {
      sheet.autoSizeColumn(it)
    }
    return workbook
  }

  private fun Cell.styleAndValue(value: String, style: CellStyle? = null) {
    this.setCellValue(value)
    if (style != null) {
      this.cellStyle = style
    }
  }

  private fun enumerateGroup(groupNode: GroupNode, layer: Int, groups: MutableList<GroupNode>) {
    groupNode.layer = layer
    groups.add(groupNode)
    groupNode.children
      .forEach {
        enumerateGroup(it, layer + 1, groups)
      }
  }

  class ImportGroup(group: Group, var layer: Int = 0): IGroup by group

  fun importGroupGeneration(groupGenerationId: Int, workbook: Workbook,
                            groupConsumer: (groups: Collection<IGroup>, groupGenerationId: Int)->Unit,
                            userAuthoritiesConsumer: (userId: UUID, authorities: Collection<Authority>)->Unit,
                            warningConsumer: (message: String)->Unit,
  ) {
    val sheet = workbook.getSheet(SHEET_NAME)
    var rowIndex = 0
    var maxGroupColNum = GROUP_START_COL
    var groupRow = sheet.getRow(rowIndex)
    while (groupRow.getCell(ROOT_GROUP_COL - 1).stringCellValue != GROUP_ID_HEADER) {
      maxGroupColNum = max(maxGroupColNum, groupRow.lastCellNum.toInt())
      groupRow = sheet.getRow(++rowIndex)
      if (sheet.lastRowNum <= rowIndex) {
        // フォーマット不正
        return
      }
    }
    val groupIdRow = groupRow

    val importGroups = mutableListOf<ImportGroup>()

    (GROUP_START_COL..maxGroupColNum)
      .forEach { colNum ->
        (GROUP_NAME_START_ROW until groupIdRow.rowNum)
          .forEach rowLoop@ { rowNum ->
            val groupName = sheet.getRow(rowNum).getCell(colNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).stringCellValue
            if (groupName.isBlank()) {
              return@rowLoop
            }
            // グループIDが空なら新規のグループ
            val groupId = groupIdRow.getCell(colNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).stringCellValue
              .let {
                if (it.isNotBlank()) {
                  UUID.fromString(it)
                } else idGeneratorService.generate()
              }

            // 所属グループに子として登録
            val parentGroupId = importGroups.lastOrNull { it.layer < rowNum }?.groupId ?: ROOT_GROUP_ID
            val group = ImportGroup(Group(groupId, groupGenerationId, groupName, parentGroupId), rowNum)
            importGroups.add(group)
          }
      }
    groupConsumer(importGroups, groupGenerationId)

    (groupIdRow.rowNum + 2..sheet.lastRowNum)
      .forEach  rowLoop@ { rowNum ->
        val userRow = sheet.getRow(rowNum)
        val userId: UUID = userRow.getCell(USER_ID_COL, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).stringCellValue
          .let {
            try {
              UUID.fromString(it)
            } catch (ex: Exception) {
              warningConsumer("${CellReference(rowNum, USER_ID_COL).formatAsString()}セルに記載されたユーザID${it}は無効な値であるため、このユーザのグループへの登録は行いませんでした。")
              return@rowLoop
            }
          }

        val authorities: Collection<Authority> = (GROUP_START_COL .. userRow.lastCellNum)
          .mapNotNull colLoop@ { colNum ->
            val groupIndex = colNum - GROUP_START_COL
            if (importGroups.lastIndex < groupIndex) {
              return@colLoop null
            }
            val groupId = importGroups[groupIndex].groupId

            val rolesText = userRow.getCell(colNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).stringCellValue
            if (rolesText.isBlank()) {
              return@colLoop null
            }
            val roleOrInvalidTexts = rolesText.split(",")
              .map { it.trim() }
              .map {
                val roleAndValidFlag = Role.fromRoleName(it)
                if (roleAndValidFlag.second) {
                  roleAndValidFlag.first to null
                } else {
                  null to it
                }
              }

            val invalidTexts = roleOrInvalidTexts.mapNotNull { it.second }
            if (invalidTexts.isNotEmpty()) {
              warningConsumer("${CellReference(rowNum, colNum).formatAsString()}セルに記載された権限${invalidTexts}は無効な値であるため、このユーザのグループへの登録は行いませんでした。")
              return@colLoop null
            }
            val roles = roleOrInvalidTexts.mapNotNull { it.first }.ifEmpty { null }
            return@colLoop Authority(groupId, groupGenerationId, roles)
          }
        userAuthoritiesConsumer(userId, authorities)
      }
  }
}