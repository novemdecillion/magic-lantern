package io.github.novemdecillion.domain

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.apache.commons.math3.fraction.Fraction
import java.lang.IllegalArgumentException

interface IChapterConfig {
  val title: String
}

interface IChapter: IChapterConfig {
  fun numberOfPages(): Int
}

interface IPage {
  val path: String?
  val textType: TextType?
}

enum class TextType {
  HTML,
  AsciiDoc
}

/**
 * 説明
 */
data class ExplainPage(
  override val path: String?,
  val title: String,
  val text: String?,
  override val textType: TextType?
): IPage

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

interface IExamQuestion {
  val type: ExamQuestionType
  val text: String
  val comment: String?
  val choices: List<ExamQuestionOption>
}

data class ExamQuestion(
  override val type: ExamQuestionType,
  val score: Int,
  override val text: String,
  override val comment: String?,
  override val choices: List<ExamQuestionOption>
): IExamQuestion {
  fun score(answer: List<Int>, scoringMethod: ScoringMethod, score: Int): Int = when (scoringMethod) {
    ScoringMethod.Neutralize -> scoringNeutralize(answer, score)
    ScoringMethod.AllOrNothing -> scoringAllOrNothing(answer, score)
  }

  /**
   * 得点
   */
  fun scoringNeutralize(answer: List<Int>, score: Int): Int {
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
  fun scoringAllOrNothing(answer: List<Int>, score: Int): Int {
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

data class ExamQuestionRecord(
  // 得点
  val scoring: Int,
  // 配点
  val score: Int
)

interface IExamChapterRecord {
  val passScore: Int
  val questions: List<ExamQuestionRecord>
}

data class ExamChapterRecord(
  override val passScore: Int,
  override val questions: List<ExamQuestionRecord>
) : IExamChapterRecord {
  fun isPass(): Boolean {
    return passScore <= questions.sumOf { it.scoring }
  }
}

data class ExamChapter(
  override val title: String,
  override val path: String?,
  val passScore: Int?,
  override val textType: TextType?,
  val questions: List<ExamQuestion>,
  val numberOfQuestions: Int,
  val option: SlideConfigOption
) : IChapter, IPage {
  override fun numberOfPages(): Int = 2

  /**
   * 得点
   */
  fun questionRecords(answer: Map<Int, List<Int>>, scoringMethod: ScoringMethod): List<ExamQuestionRecord>
    = questions
    .mapIndexed { index, question ->
      ExamQuestionRecord(answer[index]?.let { question.score(it, scoringMethod, question.score) } ?: 0, question.score)
    }

  fun chapterRecord(answer: Map<Int, List<Int>>, scoringMethod: ScoringMethod): ExamChapterRecord
    = ExamChapterRecord(passScore(), questionRecords(answer, scoringMethod))

  fun scoringPerQuestion(answer: Map<Int, List<Int>>, scoringMethod: ScoringMethod): List<Int>
    = questionRecords(answer, scoringMethod)
      .map { it.scoring }

  fun scoring(answer: Map<Int, List<Int>>): Int
    = scoringPerQuestion(answer, option.scoringMethod).sum()

  fun passScore(): Int {
    return passScore ?: totalScore()
  }

  fun totalScore(): Int {
    return questions.sumOf { it.score }
  }

  fun shuffle(): Pair<ExamChapter, List<Pair<Int, List<Int>>>> {
    val shuffledIndexes: MutableList<Pair<Int, List<Int>>> = mutableListOf()

    val shuffledQuestions = questions.withIndex()
      .let {
        if (option.shuffleQuestions) {
          // 問題の並べ替え
          it.shuffled()
        } else it.toList()
      }
      .subList(0, numberOfQuestions)
      .map { (index, question) ->
        val shuffledChoices = question.choices.withIndex()
          .let {
            if (option.shuffleChoices) {
              // 選択肢の並べ替え
              it.shuffled()
            } else it.toList()
          }
        shuffledIndexes.add(index to shuffledChoices.map { it.index })
        question.copy(choices = shuffledChoices.map { it.value })
      }
    return this.copy(questions = shuffledQuestions) to shuffledIndexes
  }

  fun replayShuffle(shuffledIndexes: List<Pair<Int, List<Int>>>): ExamChapter {
    val shuffledQuestions = shuffledIndexes
      .map { (chapterIndex, choiceIndexes) ->
        val question = this.questions[chapterIndex]
        val shuffledChoices = choiceIndexes.map { question.choices[it] }
        question.copy(choices = shuffledChoices)
      }
    return this.copy(questions = shuffledQuestions)
  }
}


/**
 * アンケート
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes(
  JsonSubTypes.Type(name = "Radio", value = SurveyQuestion::class),
  JsonSubTypes.Type(name = "Checkbox", value = SurveyQuestion::class),
  JsonSubTypes.Type(name = "Textarea", value = TextareaSurveyQuestion::class)
)
interface ISurveyQuestion {
  val type: SurveyQuestionType
  val text: String
  val required: Boolean
}

interface ISelectSurveyQuestion : ISurveyQuestion {
  val choices: List<String>
}

enum class SurveyQuestionType {
  Radio, Checkbox, Textarea
}

data class SurveyQuestion(
  override val type: SurveyQuestionType,
  override val text: String,
  override val required: Boolean = false,
  override val choices: List<String>
) : ISelectSurveyQuestion

data class TextareaSurveyQuestion(
  override val type: SurveyQuestionType = SurveyQuestionType.Textarea,
  override val text: String,
  override val required: Boolean = false
) : ISurveyQuestion

data class SurveyChapter(
  override val title: String,
  override val path: String?,
  override val textType: TextType?,
  val questions: List<ISurveyQuestion>
) : IChapter, IPage {
  override fun numberOfPages(): Int = 2
  fun numberOfQuestions(): Int = questions.size
}

enum class ScoringMethod {
  Neutralize, AllOrNothing
}

data class SlideConfigOption(
  val scoringMethod: ScoringMethod = ScoringMethod.Neutralize,
  val shuffleQuestions: Boolean = true,
  val shuffleChoices: Boolean = true,
  val showCorrectAnswer: Boolean = true
)

data class Slide(
  val slideId: String,
  val enable: Boolean,
  val title: String,
  val textType: TextType?,
  val chapters: List<IChapter>,
  val option: SlideConfigOption = SlideConfigOption()
) {
  private val chapterPageRange = chapters
    .fold(mutableListOf<Pair<IntRange, IChapter>>()){ acc, chapter ->
      val prevLast: Int = acc.lastOrNull()?.first?.last ?: -1
      acc.add(IntRange(prevLast + 1, prevLast + chapter.numberOfPages()) to chapter)
      acc
    }
  
  /**
   * 総ページ数
   */
  fun numberOfPages(): Int {
    return chapters
      .sumOf{ it.numberOfPages() }
  }

  fun chapterAndPageIndex(pageIndex: Int): Pair<IndexedValue<IChapter>, Int>? {
    return chapterPageRange.withIndex()
      .firstOrNull { it.value.first.contains(pageIndex) }
      ?.let {
        val pageIndexInChapter = pageIndex - it.value.first.first
        IndexedValue(it.index, it.value.second) to pageIndexInChapter
      }
  }

  fun chapterStartPageIndex(chapterIndex: Int): Int {
    return chapterPageRange[chapterIndex].first.first
  }

  /**
   * 満点
   */
  fun perfectScore(): Int {
    return chapters
      .filterIsInstance(ExamChapter::class.java)
      .sumOf { examChapter ->
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
          ?.scoring(it.value)
          ?: throw IllegalArgumentException()
      }
      .sum()
  }
}
