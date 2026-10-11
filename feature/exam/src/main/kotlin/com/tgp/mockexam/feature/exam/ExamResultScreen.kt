package com.tgp.mockexam.feature.exam

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tgp.mockexam.core.designsystem.theme.TgpSpacing
import com.tgp.mockexam.core.domain.engine.ExamResult
import com.tgp.mockexam.core.model.AttemptAnswer
import com.tgp.mockexam.core.model.Paper
import com.tgp.mockexam.feature.exam.util.ExamFormat
import com.tgp.mockexam.feature.exam.util.ExamLabel

@Composable
fun ExamResultScreen(
    paper: Paper?,
    result: ExamResult,
    answers: Map<Int, AttemptAnswer>,
    onRetake: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier.fillMaxSize()) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            ScoreCard(result = result, paperTitle = paper?.titleEn)

            if (paper != null) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(
                        start = TgpSpacing.m,
                        end = TgpSpacing.m,
                        bottom = TgpSpacing.m,
                    ),
                    verticalArrangement = Arrangement.spacedBy(TgpSpacing.xs),
                ) {
                    items(paper.questions, key = { it.number }) { question ->
                        val answer = answers[question.number]?.selectedOptionIndex
                        val status = when {
                            answer == null -> ResultRowStatus.SKIPPED
                            answer == question.answerIndex -> ResultRowStatus.CORRECT
                            else -> ResultRowStatus.WRONG
                        }
                        ResultRow(
                            number = question.number,
                            answeredLetter = answer?.let { ExamLabel.optionLetter(it) },
                            correctLetter = ExamLabel.optionLetter(question.answerIndex),
                            status = status,
                        )
                    }
                }
            }

            Surface(shadowElevation = 8.dp) {
                Button(
                    onClick = onRetake,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(TgpSpacing.m),
                ) {
                    Text("Retake exam")
                }
            }
        }
    }
}

@Composable
private fun ScoreCard(result: ExamResult, paperTitle: String?) {
    Surface(color = MaterialTheme.colorScheme.primaryContainer) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(TgpSpacing.l),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (paperTitle != null) {
                Text(
                    text = paperTitle,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )
            }
            Spacer(Modifier.height(TgpSpacing.s))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = ExamFormat.score(result.score),
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    text = " / ${ExamFormat.score(result.maxMarks)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }
            Spacer(Modifier.height(TgpSpacing.s))
            Row(horizontalArrangement = Arrangement.spacedBy(TgpSpacing.l)) {
                StatChip(value = "${result.correct}", label = "Correct", color = MaterialTheme.colorScheme.primary)
                StatChip(value = "${result.incorrect}", label = "Wrong", color = MaterialTheme.colorScheme.error)
                StatChip(value = "${result.skipped}", label = "Skipped", color = MaterialTheme.colorScheme.outline)
            }
            Spacer(Modifier.height(TgpSpacing.s))
            Text(
                text = "Accuracy ${String.format("%.0f", result.accuracyPercent)}% · Time ${ExamFormat.elapsed(result.timeTakenMs)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

@Composable
private fun StatChip(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = color)
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
    }
}

private enum class ResultRowStatus { CORRECT, WRONG, SKIPPED }

@Composable
private fun ResultRow(
    number: Int,
    answeredLetter: String?,
    correctLetter: String,
    status: ResultRowStatus,
) {
    Column {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = TgpSpacing.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "$number",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.width(40.dp),
            )
            Icon(
                imageVector = when (status) {
                    ResultRowStatus.CORRECT -> Icons.Filled.Check
                    ResultRowStatus.WRONG -> Icons.Filled.Close
                    ResultRowStatus.SKIPPED -> Icons.AutoMirrored.Filled.ArrowBack
                },
                contentDescription = status.name,
                tint = when (status) {
                    ResultRowStatus.CORRECT -> MaterialTheme.colorScheme.primary
                    ResultRowStatus.WRONG -> MaterialTheme.colorScheme.error
                    ResultRowStatus.SKIPPED -> MaterialTheme.colorScheme.outline
                },
                modifier = Modifier.width(24.dp),
            )
            Text(
                text = if (answeredLetter != null) {
                    "Your answer $answeredLetter · Correct $correctLetter"
                } else {
                    "Skipped · Correct $correctLetter"
                },
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}