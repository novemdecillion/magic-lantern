package io.github.novemdecillion.slide

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.apache.commons.math3.fraction.Fraction
import org.jmolecules.architecture.onion.classical.DomainModelRing
import java.lang.IllegalArgumentException

const val SLIDE_CONFIG_FILE_NAME = "config.yml"

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
  JsonSubTypes.Type(name = "Explain", value = ExplainChapter::class),
  JsonSubTypes.Type(name = "Exam", value = ExamChapter::class),
  JsonSubTypes.Type(name = "Survey", value = SurveyChapter::class)
)
interface IChapter {
  val title: String
  fun numberOfPages(): Int
}

/**
 * 説明
 */
data class ExplainPage(
  val path: String,
  val title: String,
  val content: String?
)

data class ExplainChapter(
  override val title: String,
  val pages: List<ExplainPage>
) : IChapter {
  override fun numberOfPages(): Int = pages.size
}

/**
 * テスト
 */
enum class ExamQuestionType {
  Radio, Checkbox
}

data class ExamQuestionOption(
  val text: String,
  val correct: Boolean = false
)

data class ExamQuestion(
  val type: ExamQuestionType,
  val score: Int,
  val passScore: Int?,
  val text: String,
  val choices: List<ExamQuestionOption>
) {
  fun score(answer: List<Int>, scoringMethod: ScoringMethod): Int = when (scoringMethod) {
    ScoringMethod.Neutralize -> scoringNeutralize(answer)
    ScoringMethod.AllOrNothing -> scoringAllOrNothing(answer)
  }

  /**
   * 得点
   */
  fun scoringNeutralize(answer: List<Int>): Int {
    // 正解選択肢数
    val numberOfCorrectChoice = choices.count { it.correct }
    // 正解選択肢あたりの点数
    val scorePerCorrect = Fraction(score, numberOfCorrectChoice)
    // 回答正解or誤答数
    val correctAnswer: Int = answer
      .map { choices[it].correct }
      .fold(0 to 0) { acc, isCorrect ->
        if (isCorrect) {
          (acc.first + 1) to acc.second
        } else {
          acc.first to (acc.second + 1)
        }
      }
      .let {
        val correct = it.first - it.second
        if (correct < 0) {
          return 0
        } else {
          correct
        }
      }
    return scorePerCorrect.multiply(correctAnswer).toInt()
  }

  /**
   * 得点
   */
  fun scoringAllOrNothing(answer: List<Int>): Int {
    // 正解選択肢数
    val numberOfCorrectChoice = choices.count { it.correct }
    answer
      .map { choices[it].correct }
      .fold(0 to 0) { acc, isCorrect ->
        if (isCorrect) {
          (acc.first + 1) to acc.second
        } else {
          acc.first to (acc.second + 1)
        }
      }
      .let {
        return if ((numberOfCorrectChoice == it.first)
          && (0 == it.second)
        ) {
          score
        } else {
          0
        }
      }
  }
}

data class ExamChapter(
  override val title: String,
  val questions: List<ExamQuestion>
) : IChapter {
  override fun numberOfPages(): Int = 2

  /**
   * 得点
   */
  fun score(answer: Map<Int, List<Int>>, scoringMethod: ScoringMethod): Int = answer
    .map { questions[it.key].score(it.value, scoringMethod) }
    .sum()
}

/**
 * アンケート
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
  JsonSubTypes.Type(name = "Radio", value = RadioSurveyQuestion::class),
  JsonSubTypes.Type(name = "Checkbox", value = CheckboxSurveyQuestion::class),
  JsonSubTypes.Type(name = "Textarea", value = TextareaSurveyQuestion::class)
)
interface ISurveyQuestion {
  val text: String
  val required: Boolean
}

interface ISelectSurveyQuestion : ISurveyQuestion {
  val choices: List<String>
}

data class RadioSurveyQuestion(
  override val text: String,
  override val required: Boolean = false,
  override val choices: List<String>
) : ISelectSurveyQuestion

data class CheckboxSurveyQuestion(
  override val text: String,
  override val required: Boolean = false,
  override val choices: List<String>
) : ISelectSurveyQuestion

data class TextareaSurveyQuestion(
  override val text: String,
  override val required: Boolean = false
) : ISurveyQuestion

data class SurveyChapter(
  override val title: String,
  val questions: List<ISurveyQuestion>
) : IChapter {
  override fun numberOfPages(): Int = 2
}

enum class ScoringMethod {
  Neutralize, AllOrNothing
}

data class SlideConfigOption(
  val scoringMethod: ScoringMethod = ScoringMethod.Neutralize
)

data class SlideConfig(
  val title: String,
  val chapters: List<IChapter>,
  val option: SlideConfigOption = SlideConfigOption()
) {
  /**
   * 総ページ数
   */
  fun numberOfPages(): Int {
    return chapters
      .sumBy { it.numberOfPages() }
  }

  /**
   * 満点
   */
  fun perfectScore(): Int {
    return chapters
      .filterIsInstance(ExamChapter::class.java)
      .sumBy { examChapter ->
        examChapter.questions.sumBy { it.score }
      }
  }

  /**
   * 得点
   */
  fun score(answer: Map<Int, Map<Int, List<Int>>>): Int {
    return answer
      .map {
        (chapters[it.key] as? ExamChapter)
          ?.score(it.value, option.scoringMethod)
          ?: throw IllegalArgumentException()
      }
      .sum()
  }
}

@DomainModelRing
class Slide(
  val slideId: String,
  val config: SlideConfig,
) {

}
