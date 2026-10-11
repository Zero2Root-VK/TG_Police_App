package com.tgp.mockexam.feature.exam

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tgp.mockexam.core.designsystem.theme.TgpSpacing
import com.tgp.mockexam.core.model.QuestionState
import com.tgp.mockexam.feature.exam.util.ExamFormat
import com.tgp.mockexam.feature.exam.util.ExamLabel
import com.tgp.mockexam.feature.exam.util.PaletteCalculator

private const val WARNING_MS = 5 * 60_000L

@Composable
fun ExamRunnerScreen(
    state: ExamUiState,
    onOptionSelected: (Int) -> Unit,
    onToggleMarkForReview: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onJumpTo: (Int) -> Unit,
    onOpenPalette: () -> Unit,
    onToggleLanguage: () -> Unit,
    onRequestSubmit: () -> Unit,
    onDismissSubmit: () -> Unit,
    onConfirmSubmit: () -> Unit,
    onRequestExit: () -> Unit,
    onDismissExit: () -> Unit,
    onConfirmExit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val paper = state.paper ?: return
    val session = state.session ?: return
    val question = state.currentQuestion ?: return
    val answer = session.answers[question.number]
    val language = state.language

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            ExamTopBar(
                timeRemaining = state.remainingMs,
                questionNumber = question.number,
                totalQuestions = paper.totalQuestions,
                onExit = onRequestExit,
                onSubmit = onRequestSubmit,
                onToggleLanguage = onToggleLanguage,
            )
        },
        bottomBar = {
            ExamBottomBar(
                onPalette = onOpenPalette,
                onPrevious = onPrevious,
                onNext = onNext,
                isFirst = question.number == paper.questions.firstOrNull()?.number,
                isLast = question.number == paper.totalQuestions,
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(TgpSpacing.m),
            verticalArrangement = Arrangement.spacedBy(TgpSpacing.m),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val isMarked = answer?.markedForReview == true
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = if (isMarked) {
                        MaterialTheme.colorScheme.tertiaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .clickable { onToggleMarkForReview() },
                ) {
                    Text(
                        text = if (isMarked) "Unmark for review" else "Mark for review",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = TgpSpacing.s, vertical = TgpSpacing.xxs),
                    )
                }
                BilingualToggle(language, onToggleLanguage)
            }

            Text(
                text = "Question ${question.number} of ${paper.totalQuestions}",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = question.prompt.forLanguage(language),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(TgpSpacing.m),
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(TgpSpacing.xs)) {
                question.options.forEachIndexed { index, option ->
                    val selected = answer?.selectedOptionIndex == index
                    val shape = MaterialTheme.shapes.medium
                    Surface(
                        shape = shape,
                        color = if (selected) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surface
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape)
                            .border(
                                width = if (selected) 2.dp else 1.dp,
                                color = if (selected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.outlineVariant
                                },
                                shape = shape,
                            )
                            .clickable { onOptionSelected(index) },
                    ) {
                        Row(
                            modifier = Modifier.padding(TgpSpacing.s),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = selected,
                                onClick = { onOptionSelected(index) },
                            )
                            Text(
                                text = "${ExamLabel.optionLetter(index)}. ${option.text.forLanguage(language)}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = TgpSpacing.xs),
                            )
                        }
                    }
                }
            }
        }
    }

    if (state.showPalette) {
        ExamPaletteSheet(
            totalQuestions = paper.totalQuestions,
            answers = session.answers,
            currentQuestionNumber = question.number,
            onJumpTo = onJumpTo,
            onCurrent = { onJumpTo(question.number) },
        )
    }

    if (state.showSubmitDialog) {
        AlertDialog(
            onDismissRequest = onDismissSubmit,
            title = { Text("Finish exam?") },
            text = { Text("Submit all answers now? You cannot change them after submission.") },
            confirmButton = {
                TextButton(onClick = onConfirmSubmit) {
                    Text("Submit", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissSubmit) { Text("Keep going") }
            },
        )
    }

    if (state.showExitDialog) {
        AlertDialog(
            onDismissRequest = onDismissExit,
            title = { Text("Exit exam?") },
            text = { Text("Leaving will submit your exam as it is.") },
            confirmButton = {
                TextButton(onClick = onConfirmExit) {
                    Text("Exit & submit", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissExit) { Text("Stay") }
            },
        )
    }
}

@Composable
private fun ExamTopBar(
    timeRemaining: Long,
    questionNumber: Int,
    totalQuestions: Int,
    onExit: () -> Unit,
    onSubmit: () -> Unit,
    onToggleLanguage: () -> Unit,
) {
    val isLastMinutes = timeRemaining <= WARNING_MS
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .statusBarsPadding()
            .padding(horizontal = TgpSpacing.xs, vertical = TgpSpacing.xxs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onExit) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Exit",
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Question $questionNumber / $totalQuestions",
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "Answers auto-save",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Surface(
            shape = CircleShape,
            color = if (isLastMinutes) {
                MaterialTheme.colorScheme.errorContainer
            } else {
                MaterialTheme.colorScheme.surface
            },
            border = androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                color = if (isLastMinutes) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.outline
                },
            ),
        ) {
            Text(
                text = ExamFormat.countdown(timeRemaining),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = if (isLastMinutes) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                modifier = Modifier.padding(horizontal = TgpSpacing.s, vertical = TgpSpacing.xxs),
            )
        }
        TextButton(onClick = onSubmit) {
            Text("Submit", fontWeight = FontWeight.Bold)
        }
        TextButton(onClick = onToggleLanguage) {
            Text("EN/తెలుగు", style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun ExamBottomBar(
    onPalette: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    isFirst: Boolean,
    isLast: Boolean,
) {
    Surface(shadowElevation = 8.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(TgpSpacing.s),
            horizontalArrangement = Arrangement.spacedBy(TgpSpacing.s),
        ) {
            TextButton(
                onClick = onPalette,
                modifier = Modifier.weight(1f),
            ) {
                Text("Palette")
            }
            TextButton(
                onClick = onPrevious,
                enabled = !isFirst,
                modifier = Modifier.weight(1f),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous",
                    modifier = Modifier.size(18.dp),
                )
                Text("Previous", modifier = Modifier.padding(start = TgpSpacing.xxs))
            }
            TextButton(
                onClick = onNext,
                enabled = !isLast,
                modifier = Modifier.weight(1f),
            ) {
                Text("Next")
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next",
                    modifier = Modifier
                        .padding(start = TgpSpacing.xxs)
                        .size(18.dp),
                )
            }
        }
    }
}