package com.tgp.mockexam.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tgp.mockexam.core.designsystem.component.TgpBrand
import com.tgp.mockexam.core.designsystem.component.TgpShield
import com.tgp.mockexam.core.designsystem.theme.TgpSpacing
import com.tgp.mockexam.core.model.ContentLanguage
import com.tgp.mockexam.core.model.Track
import kotlinx.coroutines.launch

private const val PAGE_COUNT = 3

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val strings = authStrings(state.language)
    val pagerState = rememberPagerState(pageCount = { PAGE_COUNT })
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = TgpSpacing.l, vertical = TgpSpacing.m),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(TgpSpacing.xs),
        ) {
            TgpShield(modifier = Modifier.size(28.dp))
            Text(
                text = "TGP Mock Exam",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) { page ->
            when (page) {
                0 -> WelcomePage(strings)
                1 -> LanguagePage(
                    strings = strings,
                    selected = state.language,
                    onSelect = viewModel::selectLanguage,
                )
                else -> ExamPage(
                    strings = strings,
                    selected = state.examChoice,
                    onSelect = viewModel::selectExam,
                )
            }
        }

        PagerIndicator(
            pageCount = PAGE_COUNT,
            current = pagerState.currentPage,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = TgpSpacing.m),
        )

        Column(Modifier.padding(horizontal = TgpSpacing.l)) {
            Button(
                onClick = {
                    if (pagerState.currentPage == PAGE_COUNT - 1) {
                        viewModel.finish(onFinish)
                    } else {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
            ) {
                Text(
                    text = if (pagerState.currentPage == PAGE_COUNT - 1) strings.getStarted else strings.next,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
            androidx.compose.material3.TextButton(
                onClick = { viewModel.finish(onFinish) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = TgpSpacing.s),
            ) {
                Text(strings.skip)
            }
        }
    }
}

@Composable
private fun WelcomePage(strings: AuthStrings) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = TgpSpacing.l),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.extraLarge)
                .background(TgpBrand.HeroGradient)
                .padding(vertical = TgpSpacing.xxl, horizontal = TgpSpacing.l),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                TgpShield(modifier = Modifier.size(88.dp), onDark = true)
                Spacer(Modifier.height(TgpSpacing.m))
                Text(
                    text = strings.onboardingTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(TgpSpacing.xs))
                Text(
                    text = strings.onboardingSubtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center,
                )
            }
        }

        Spacer(Modifier.height(TgpSpacing.l))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(TgpSpacing.s),
        ) {
            StatChip("15", strings.statPapers, Modifier.weight(1f))
            StatChip("3,000", strings.statQuestions, Modifier.weight(1f))
            StatChip("EN/తె", strings.statBilingual, Modifier.weight(1f))
        }
    }
}

@Composable
private fun StatChip(value: String, label: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerHighest,
    ) {
        Column(
            modifier = Modifier.padding(vertical = TgpSpacing.s, horizontal = TgpSpacing.xs),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun LanguagePage(
    strings: AuthStrings,
    selected: ContentLanguage,
    onSelect: (ContentLanguage) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = TgpSpacing.l),
        verticalArrangement = Arrangement.Center,
    ) {
        PageHeader(strings.languageTitle, strings.languageSubtitle)
        Spacer(Modifier.height(TgpSpacing.l))
        SelectableOption(
            title = "English",
            subtitle = "Continue in English",
            selected = selected == ContentLanguage.ENGLISH,
            onClick = { onSelect(ContentLanguage.ENGLISH) },
        )
        Spacer(Modifier.height(TgpSpacing.s))
        SelectableOption(
            title = "తెలుగు",
            subtitle = "తెలుగులో కొనసాగించండి",
            selected = selected == ContentLanguage.TELUGU,
            onClick = { onSelect(ContentLanguage.TELUGU) },
        )
    }
}

@Composable
private fun ExamPage(
    strings: AuthStrings,
    selected: ExamChoice,
    onSelect: (ExamChoice) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = TgpSpacing.l),
        verticalArrangement = Arrangement.Center,
    ) {
        PageHeader(strings.examTitle, strings.examSubtitle)
        Spacer(Modifier.height(TgpSpacing.l))
        SelectableOption(
            title = strings.examConstable,
            subtitle = strings.examConstableSub,
            selected = selected == ExamChoice.CONSTABLE,
            onClick = { onSelect(ExamChoice.CONSTABLE) },
        )
        Spacer(Modifier.height(TgpSpacing.s))
        SelectableOption(
            title = strings.examSi,
            subtitle = strings.examSiSub,
            selected = selected == ExamChoice.SI,
            onClick = { onSelect(ExamChoice.SI) },
        )
        Spacer(Modifier.height(TgpSpacing.s))
        SelectableOption(
            title = strings.examBoth,
            subtitle = strings.examBothSub,
            selected = selected == ExamChoice.BOTH,
            onClick = { onSelect(ExamChoice.BOTH) },
        )
    }
}

@Composable
private fun PageHeader(title: String, subtitle: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = TgpSpacing.xxs),
        )
    }
}

@Composable
private fun SelectableOption(
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val borderColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = borderColor,
                shape = MaterialTheme.shapes.large,
            )
            .selectable(selected = selected, onClick = onClick),
        shape = MaterialTheme.shapes.large,
        color = if (selected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerLow
        },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(TgpSpacing.m),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        if (selected) MaterialTheme.colorScheme.primary
                        else Color.Transparent,
                    )
                    .border(
                        width = 2.dp,
                        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                if (selected) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun PagerIndicator(
    pageCount: Int,
    current: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
    ) {
        repeat(pageCount) { index ->
            val active = index == current
            Box(
                modifier = Modifier
                    .padding(horizontal = TgpSpacing.xxs)
                    .height(8.dp)
                    .width(if (active) 24.dp else 8.dp)
                    .clip(CircleShape)
                    .background(
                        if (active) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outlineVariant,
                    ),
            )
        }
    }
}
