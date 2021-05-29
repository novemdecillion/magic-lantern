package io.github.novemdecillion.domain

import org.jmolecules.architecture.onion.classical.DomainModelRing
import java.time.OffsetDateTime
import java.util.*
import kotlin.math.ceil

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
  FAILED
}

data class Study(
  val studyId: UUID,
  val userId: UUID,
  val slideId: String,
  val status: StudyStatus = StudyStatus.NOT_START,
  val progress: Map<Int, Set<Int>> = mapOf(),
  val progressRate: Int = 0,
  val answer: Map<Int, Map<Int, List<String>>> = mapOf(),
  val score: Map<Int, ExamChapterRecord> = mapOf(),
  val startAt: OffsetDateTime? = null,
  val endAt: OffsetDateTime? = null) {

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
       chapterRecord.questions.map { it.scoring }.sum() < chapterRecord.passScore
     }
     ?.let { false }
     ?: true
  }

  fun updateStatus(): Study {
    val updatedStatus = when {
      (startAt != null) -> StudyStatus.ON_GOING
      (endAt != null) || (progressRate == 100) -> if (isPass()) StudyStatus.PASS else StudyStatus.FAILED
      else -> status
    }
    return this.copy(status = updatedStatus)
  }

  fun answerForExam(chapterIndex: Int): Map<Int, List<Int>> {
    return convertForExamAnswer(answer[chapterIndex]?: return mapOf())
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
