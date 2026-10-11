package com.tgp.mockexam.feature.exam

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tgp.mockexam.core.designsystem.theme.TgpSpacing
import com.tgp.mockexam.core.model.ContentLanguage
import com.tgp.mockexam.feature.exam.util.ExamFormat

@Composable
fun ExamRoute(
    paperId: String,
    modifier: Modifier = Modifier,
) {
    val viewModel: ExamViewModel = hiltViewModel(
        creationCallback = { factory: ExamViewModel.Factory -> factory.create(paperId) },
    )
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when {
        state.loading -> LoadingScreen(modifier)
        state.result != null && state.isSubmitted -> {
            val result = checkNotNull(state.result)
            ExamResultScreen(
                paper = state.paper,
                result = result,
                answers = state.session?.answers ?: emptyMap(),
                onRetake = viewModel::retake,
            )
        }
        state.session == null -> ExamIntroScreen(
            state = state,
            onStart = viewModel::start,
            onToggleLanguage = viewModel::toggleLanguage,
            modifier = modifier,
        )
        else -> ExamRunnerScreen(
            state = state,
            onOptionSelected = viewModel::selectOption,
            onToggleMarkForReview = viewModel::toggleMarkForReview,
            onPrevious = viewModel::previous,
            onNext = viewModel::next,
            onJumpTo = viewModel::jumpTo,
            onOpenPalette = viewModel::openPalette,
            onToggleLanguage = viewModel::toggleLanguage,
            onRequestSubmit = viewModel::requestSubmit,
            onDismissSubmit = viewModel::dismissSubmit,
            onConfirmSubmit = viewModel::confirmSubmit,
            onRequestExit = viewModel::requestExit,
            onDismissExit = viewModel::dismissExit,
            onConfirmExit = viewModel::confirmExit,
            modifier = modifier,
        )
    }
}

@Composable
private fun LoadingScreen(modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun ExamIntroScreen(
    state: ExamUiState,
    onStart: () -> Unit,
    onToggleLanguage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val paper = state.paper
    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(TgpSpacing.l),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = paper?.titleEn?.let {
                        if (state.language == ContentLanguage.ENGLISH) it else paper.titleTe
                    } ?: "",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
                if (paper != null) {
                    Text(
                        text = buildString {
                            append("${paper.track.name} · ${paper.stage.name} ${paper.year}")
                            append(" · ${paper.series} series")
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = TgpSpacing.xs),
                    )
                }
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.padding(top = TgpSpacing.m),
                ) {
                    Row(
                        modifier = Modifier.padding(TgpSpacing.m),
                        horizontalArrangement = Arrangement.spacedBy(TgpSpacing.xl),
                    ) {
                        paper?.let {
                            MetaItem("Questions", "${it.totalQuestions}")
                            MetaItem("Marks", "${it.marks}")
                            MetaItem("Duration", "${it.durationMins} min")
                        }
                    }
                }
                paper?.let {
                    Text(
                        text = markingScheme(it.positiveMarks, it.negativeMarks),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = if (it.negativeMarks > 0) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        modifier = Modifier.padding(top = TgpSpacing.s),
                    )
                }
                Text(
                    text = "Timer keeps running in background, answers auto-save every few seconds, and the paper runs fully offline.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = TgpSpacing.m),
                )
            }
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BilingualToggle(state.language, onToggleLanguage)
                }
                Button(
                    onClick = onStart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = TgpSpacing.m),
                ) {
                    Text(
                        text = if (state.session != null) "Resume exam" else "Start exam",
                        modifier = Modifier.padding(TgpSpacing.xs),
                    )
                }
            }
        }
    }
}

@Composable
fun BilingualToggle(language: ContentLanguage, onToggle: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "EN",
            color = if (language == ContentLanguage.ENGLISH) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            style = MaterialTheme.typography.labelLarge,
        )
        TextButton(onClick = onToggle) {
            Text("English / తెలుగు")
        }
        Text(
            text = "తెలుగు",
            color = if (language == ContentLanguage.TELUGU) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Composable
private fun MetaItem(label: String, value: String) {
    Column {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private fun formatMark(value: Double): String =
    if (value % 1.0 == 0.0) value.toInt().toString() else String.format("%.2f", value)

private fun markingScheme(positiveMarks: Double, negativeMarks: Double): String =
    if (negativeMarks > 0) {
        "Marking: +${formatMark(positiveMarks)} per correct · −${formatMark(negativeMarks)} per wrong"
    } else {
        "Marking: +${formatMark(positiveMarks)} per correct · no negative marking"
    }