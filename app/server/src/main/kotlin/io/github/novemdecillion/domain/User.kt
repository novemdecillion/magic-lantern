package io.github.novemdecillion.domain

import org.jmolecules.architecture.onion.classical.DomainModelRing
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
        ?: null to false
    }
  }
}

enum class StudyStatus {
  NOT_START,
  ON_GOING,
  PASS,
  FAILED,
  EXCLUDED
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
  val progress: Map<Int, Set<Int>> = emptyMap(),
  val progressRate: Int = 0,
  val answer: Map<Int, Map<Int, List<String>>> = emptyMap(),
  val score: Map<Int, ExamChapterRecord> = emptyMap(),
  val shuffledQuestion: Map<Int, List<Pair<Int, List<Int>>>> = emptyMap(),
  val startAt: OffsetDateTime? = null,
  val endAt: OffsetDateTime? = null): INotStartStudy {

  companion object {
    fun convertForExamAnswer(answer: Map<Int, List<String>>): Map<Int, List<Int>> {
      return answer
        .map { (key, value) ->
          key to value.map { it.toInt() }
        }
        .toMap()
    }
  }

  fun recordProgress(chapterIndex: Int, pageIndexInChapter: Int, totalPage: Int): Study {
    val progressInChapter = progress.getOrDefault(chapterIndex, setOf())
    val updatedProgress = progress.plus(chapterIndex to progressInChapter.plus(pageIndexInChapter))
    val updatedProgressRate = ceil (updatedProgress.map { it.value.size }.sum().toDouble() / totalPage * 100).toInt()
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
      } else status

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
    return chapterAnswer
      .map { (answerQuestionIndex, answerChoiceIndexes) ->
        val (questionIndex, choiceIndexes) = shuffled[answerQuestionIndex]
        questionIndex to answerChoiceIndexes
          .map { answerChoiceIndex ->
            choiceIndexes.withIndex().first { it.value == answerChoiceIndex.toInt() }.index.toString()
          }
      }
      .toMap()
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

@DomainModelRing
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
