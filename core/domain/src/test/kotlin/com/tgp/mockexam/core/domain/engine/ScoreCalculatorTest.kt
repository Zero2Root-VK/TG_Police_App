package com.tgp.mockexam.core.domain.engine

import com.tgp.mockexam.core.model.AttemptAnswer
import com.tgp.mockexam.core.model.Bilingual
import com.tgp.mockexam.core.model.Paper
import com.tgp.mockexam.core.model.Question
import com.tgp.mockexam.core.model.QuestionOption
import com.tgp.mockexam.core.model.Solution
import com.tgp.mockexam.core.model.Stage
import com.tgp.mockexam.core.model.Track
import org.junit.Assert.assertEquals
import org.junit.Test

class ScoreCalculatorTest {

    private val paper = Paper(
        id = "t",
        track = Track.CONSTABLE,
        stage = Stage.PRELIMS,
        year = 2022,
        paperNo = 1,
        series = "A",
        titleEn = "",
        titleTe = "",
        marks = 2,
        durationMins = 1,
        totalQuestions = 2,
        reviewPending = false,
        positiveMarks = 1.0,
        negativeMarks = 0.25,
        questions = listOf(
            question(1, answer = 1),
            question(2, answer = 2),
        ),
    )

    @Test
    fun `all correct scores full marks`() {
        val answers = mapOf(
            1 to answer(1, selected = 1),
            2 to answer(2, selected = 2),
        )
        val result = ScoreCalculator.compute(paper, answers, timeTakenMs = 10_000L)
        assertEquals(2.0, result.score, 0.0)
        assertEquals(2, result.correct)
        assertEquals(0, result.incorrect)
        assertEquals(0, result.skipped)
        assertEquals(2.0, result.maxMarks, 0.0)
        assertEquals(100.0, result.accuracyPercent, 0.0)
        assertEquals(10_000L, result.timeTakenMs)
    }

    @Test
    fun `wrong answer deducts negative marks`() {
        val answers = mapOf(1 to answer(1, selected = 2))
        val result = ScoreCalculator.compute(paper, answers, timeTakenMs = 0L)
        assertEquals(-0.25, result.score, 0.0)
        assertEquals(1, result.incorrect)
    }

    @Test
    fun `no negative marking is applied when negativeMarks is zero`() {
        val noNegative = paper.copy(track = Track.CONSTABLE, negativeMarks = 0.0)
        val answers = mapOf(1 to answer(1, selected = 2))
        val result = ScoreCalculator.compute(noNegative, answers, timeTakenMs = 0L)
        assertEquals(0.0, result.score, 0.0)
        assertEquals(1, result.incorrect)
    }

    @Test
    fun `si prelims negative marking of 0_20 is applied`() {
        val siPrelims = paper.copy(track = Track.SI, stage = Stage.PRELIMS, negativeMarks = 0.20)
        val answers = mapOf(1 to answer(1, selected = 2))
        val result = ScoreCalculator.compute(siPrelims, answers, timeTakenMs = 0L)
        assertEquals(-0.20, result.score, 0.0)
    }

    @Test
    fun `unattempted questions count as skipped`() {
        val result = ScoreCalculator.compute(paper, emptyMap(), timeTakenMs = 0L)
        assertEquals(2, result.skipped)
        assertEquals(0.0, result.score, 0.0)
        assertEquals(0.0, result.accuracyPercent, 0.0)
    }
}

private fun question(number: Int, answer: Int): Question = Question(
    number = number,
    prompt = Bilingual("q$number", ""),
    options = listOf(
        QuestionOption(Bilingual("a", "")),
        QuestionOption(Bilingual("b", "")),
    ),
    answerIndex = answer,
    solution = Solution(rule = null, en = "", te = ""),
)

private fun answer(questionNumber: Int, selected: Int): AttemptAnswer = AttemptAnswer(
    questionNumber = questionNumber,
    selectedOptionIndex = selected,
    markedForReview = false,
    visited = true,
)