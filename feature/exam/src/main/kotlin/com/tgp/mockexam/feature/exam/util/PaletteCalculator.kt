package com.tgp.mockexam.feature.exam.util

import androidx.compose.runtime.Immutable
import com.tgp.mockexam.core.model.AttemptAnswer
import com.tgp.mockexam.core.model.QuestionState

object PaletteCalculator {

    fun stateOf(questionNumber: Int, answers: Map<Int, AttemptAnswer>): QuestionState {
        val answer = answers[questionNumber]
        return when {
            answer == null || !answer.visited -> QuestionState.NOT_VISITED
            answer.markedForReview && answer.selectedOptionIndex != null -> QuestionState.ANSWERED_AND_MARKED
            answer.markedForReview -> QuestionState.MARKED_FOR_REVIEW
            answer.selectedOptionIndex != null -> QuestionState.ANSWERED
            else -> QuestionState.NOT_ANSWERED
        }
    }

    fun counts(totalQuestions: Int, answers: Map<Int, AttemptAnswer>): PaletteCounts {
        var answered = 0
        var notAnswered = 0
        var marked = 0
        var notVisited = 0
        for (number in 1..totalQuestions) {
            when (stateOf(number, answers)) {
                QuestionState.ANSWERED, QuestionState.ANSWERED_AND_MARKED -> answered++
                QuestionState.MARKED_FOR_REVIEW -> marked++
                QuestionState.NOT_ANSWERED -> notAnswered++
                QuestionState.NOT_VISITED -> notVisited++
            }
        }
        return PaletteCounts(answered, notAnswered, marked, notVisited)
    }
}

@Immutable
data class PaletteCounts(
    val answered: Int,
    val notAnswered: Int,
    val marked: Int,
    val notVisited: Int,
) {
    val sum: Int get() = answered + notAnswered + marked + notVisited
}