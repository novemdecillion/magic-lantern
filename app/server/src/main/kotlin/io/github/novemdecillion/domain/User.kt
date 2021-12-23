package io.github.novemdecillion.domain

import java.time.OffsetDateTime
import java.util.*
import kotlin.math.ceil

val BUILT_IN_ADMIN_USER_ID: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")

enum class Role(val roleName: String) {
  ADMIN("システム"),
  GROUP("グループ"),
  SLIDE("教材"),
  LESSON("講座"),
  STUDY("受講");
  companion object {
    const val NO_ROLE_NAME = "なし"

    fun fromRoleName(roleName: String): Pair<Role?, Boolean> {
      if (roleName == NO_ROLE_NAME) {
        return null to true
      }
      return values().firstOrNull { it.roleName == roleName }
        ?.let { it to true }
        ?: (null to false)
    }
  }
}

enum class StudyStatus(val statusName: String) {
  NOT_START("未着手"),
  ON_GOING("実施中"),
  PASS("合格"),
  FAILED("不合格"),
  EXCLUDED("対象外")
}

interface INotStartStudy {
  val userId: UUID
  val slideId: String
  val status: StudyStatus
}

data class NotStartStudy(
  override val userId: UUID,
  override val slideId: String,
) : INotStartStudy {
  override val status: StudyStatus
    get() = StudyStatus.NOT_START
}

data class Study(
  val studyId: UUID,
  override val userId: UUID,
  override val slideId: String,
  override val status: StudyStatus = StudyStatus.NOT_START,
  val index: Int = START_INDEX,
  val progress: Map<Int, Set<Int>> = emptyMap(),
  val progressRate: Int = 0,
  val answer: Map<Int, Map<Int, List<String>>> = emptyMap(),
  val score: Map<Int, ExamChapterRecord> = emptyMap(),
  val shuffledQuestion: Map<Int, List<Pair<Int, List<Int>>>> = emptyMap(),
  val startAt: OffsetDateTime? = null,
  val endAt: OffsetDateTime? = null): INotStartStudy {

  companion object {
    const val START_INDEX = 0
    const val CURRENT_PAGE_INDEX = -1
    fun convertForExamAnswer(answer: Map<Int, List<String>>): Map<Int, List<Int>> {
      return answer
        .map { (key, value) ->
          key to value.map { it.toInt() }
        }
        .toMap()
    }
    fun progressWithoutCurrentPageIndex(progress: Map<Int, Set<Int>>): Map<Int, Set<Int>> {
      return progress.filter { it.key != CURRENT_PAGE_INDEX }
    }
  }

  fun isComplete(): Boolean {
    return when(status) {
      StudyStatus.PASS, StudyStatus.FAILED -> true
      else -> false
    }
  }

  fun progressWithoutCurrentPageIndex(): Map<Int, Set<Int>> {
    return progressWithoutCurrentPageIndex(this.progress)
  }

  fun currentPageIndex(): Int {
    return progress.getOrDefault(CURRENT_PAGE_INDEX, setOf(0)).first()
  }

  fun recordCurrentPage(pageIndex: Int): Study {
    val updatedProgress = progress.plus(CURRENT_PAGE_INDEX to setOf(pageIndex))
    return this.copy(progress = updatedProgress)
  }

  fun recordProgress(chapterIndex: Int, pageIndexInChapter: Int, totalPage: Int): Study {
    val progressInChapter = progress.getOrDefault(chapterIndex, setOf())
    val updatedProgress = progress.plus(chapterIndex to progressInChapter.plus(pageIndexInChapter))
    val updatedProgressRate = ceil (Companion.progressWithoutCurrentPageIndex(updatedProgress).map { it.value.size }.sum().toDouble() / totalPage * 100).toInt()
    return this.copy(progress = updatedProgress, progressRate = updatedProgressRate)
  }

  fun recordAnswer(chapterIndex: Int, answer: Map<Int, List<String>>): Study {
    val undatedAnswer = this.answer.plus(chapterIndex to answer)
    return this.copy(answer = undatedAnswer)
  }

  fun recordAnswer(chapterIndex: Int, answer: Map<Int, List<String>>, record: ExamChapterRecord): Study {
    val undatedAnswer = this.answer.plus(chapterIndex to answer)
    val updatedScore = this.score.plus(chapterIndex to record)
    return this.copy(answer = undatedAnswer, score = updatedScore)
  }

  fun isPass(): Boolean {
   return score.values
     .firstOrNull { chapterRecord ->
       chapterRecord.questions.sumOf { it.scoring } < chapterRecord.passScore
     }
     ?.let { false }
     ?: true
  }

  fun updateStatus(): Study {
    var updatedStartAt: OffsetDateTime? = startAt
    var updatedEndAt: OffsetDateTime? = endAt

    val updatedStatus = when(status) {
      StudyStatus.NOT_START -> if (0 < progressRate) {
        if ( updatedStartAt == null ) {
          updatedStartAt = OffsetDateTime.now()
        }
        StudyStatus.ON_GOING
      } else status

      StudyStatus.ON_GOING ->if (progressRate == 100) {
        updatedEndAt = OffsetDateTime.now()
        if (isPass()) StudyStatus.PASS else StudyStatus.FAILED
      } else {
        if ( updatedStartAt == null ) {
          updatedStartAt = OffsetDateTime.now()
        }
        status
      }

      else -> {
        val recheckedStatus = if (isPass()) StudyStatus.PASS else StudyStatus.FAILED
        if (recheckedStatus != status) {
          updatedEndAt = OffsetDateTime.now()
        }
        recheckedStatus
      }
    }
    return this.copy(status = updatedStatus, startAt = updatedStartAt, endAt = updatedEndAt)
  }

  fun answerForExam(chapterIndex: Int): Map<Int, List<Int>> {
    val chapterAnswer = answer[chapterIndex] ?: return emptyMap()
    return chapterAnswer
      .map { (questionIndex, questionAnswers) ->
        questionIndex to questionAnswers.map { it.toInt() }
      }
      .toMap()
  }

  fun shuffledAnswer(chapterIndex: Int): Map<Int, List<String>>? {
    val chapterAnswer = answer[chapterIndex] ?: return null
    val shuffled = shuffledQuestion[chapterIndex] ?: return chapterAnswer
    var questionIndex = 0


    return shuffled
      .associate { (shuffledQuestionIndex, shuffledChoiceIndexes) ->
        val questionAnswers = chapterAnswer[shuffledQuestionIndex]

        questionIndex++ to (questionAnswers
          ?.map { answerChoiceIndex ->
            shuffledChoiceIndexes.withIndex().first { it.value == answerChoiceIndex.toInt() }.index.toString()
          }
          // emptyList()だとThymeleafで実行されるSpELがcontainsメソッドを見つけられない
          // おそらくList<String>を期待しているのに、emptyList()はList<Nothing>だからだと思う。
          ?: mutableListOf())
      }
  }
}

data class Authority(
  val groupId: UUID,
  val groupGenerationInt: Int,
  val roles: Collection<Role>?
) {
  companion object {
    fun forRootGroup(roles: Collection<Role>? = null): Authority {
      return Authority(ROOT_GROUP_ID, ROOT_GROUP_GENERATION_ID, roles)
    }
  }

  fun roleNames(): String {
    return roles?.joinToString { it.roleName } ?: Role.NO_ROLE_NAME
  }
}

data class User(
  val userId: UUID,
  val accountName: String,
  val userName: String,
  val email: String? = null,
  val realmId: String,
  val enabled: Boolean,
  val authorities: Collection<Authority> = listOf()
) {
  val isSystemRealm: Boolean
    get() = realmId == SYSTEM_REALM_ID

  fun isAdmin(): Boolean {
    return null != authorities.firstOrNull { it.groupId == ROOT_GROUP_ID }?.roles?.contains(Role.ADMIN)
  }
}
