package io.github.novemdecillion.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.data.forAll
import io.kotest.data.headers
import io.kotest.data.row
import io.kotest.data.table
import org.assertj.core.api.Assertions

class SlideTest: StringSpec({
    val examChapter = ExamChapter(
      title = "試験",
      path = null,
      passScore = null,
      textType = null,
      numberOfQuestions = QUESTION_SIZE,
      questions = (1..QUESTION_SIZE).map { questionIndex ->
        ExamQuestion(
          type = ExamQuestionType.Checkbox,
          score = 10,
          text = "問題$questionIndex",
          comment = null,
          choices = (1..3).map { ExamQuestionOption("選択肢$it") }
        )
      },
      option = SlideConfigOption())

  "出題ランダム化" {
    forAll(
      table(
        headers("numberOfQuestions"),
        row(QUESTION_SIZE),
        row(3)
      )
    ) { numberOfQuestions ->
      val (randomizedChapter, questionIndexes) = examChapter.copy(numberOfQuestions = numberOfQuestions).shuffle()

      Assertions.assertThat(randomizedChapter.questions.size).isEqualTo(numberOfQuestions)
      Assertions.assertThat(randomizedChapter.questions.size).isEqualTo(randomizedChapter.numberOfQuestions)

      val replayedChapter = examChapter.replayShuffle(questionIndexes)

      Assertions.assertThat(replayedChapter.questions.size).isEqualTo(randomizedChapter.questions.size)

      Assertions.assertThat(replayedChapter.questions.map { it.text }.toSet())
        .containsAll(randomizedChapter.questions.map { it.text }.toSet())
    }
  }


}) {
  companion object {
    const val QUESTION_SIZE = 10
  }
}