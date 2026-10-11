package com.tgp.mockexam.feature.auth

import androidx.compose.runtime.Immutable
import com.tgp.mockexam.core.model.ContentLanguage
import com.tgp.mockexam.core.model.Track

enum class ExamChoice(val track: Track?) {
    CONSTABLE(Track.CONSTABLE),
    SI(Track.SI),
    BOTH(null),
}

@Immutable
data class OnboardingUiState(
    val language: ContentLanguage = ContentLanguage.ENGLISH,
    val examChoice: ExamChoice = ExamChoice.BOTH,
    val saving: Boolean = false,
)
