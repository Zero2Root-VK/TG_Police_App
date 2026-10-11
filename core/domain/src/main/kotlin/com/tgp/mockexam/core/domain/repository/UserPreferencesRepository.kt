package com.tgp.mockexam.core.domain.repository

import com.tgp.mockexam.core.model.ContentLanguage
import com.tgp.mockexam.core.model.Track
import kotlinx.coroutines.flow.Flow

/** Lightweight user preferences that must survive restarts (onboarding, language, exam choice). */
interface UserPreferencesRepository {
    val onboardingCompleted: Flow<Boolean>
    val uiLanguage: Flow<ContentLanguage>
    val selectedTrack: Flow<Track?>

    suspend fun setOnboardingCompleted(completed: Boolean)
    suspend fun setUiLanguage(language: ContentLanguage)
    suspend fun setSelectedTrack(track: Track?)
}
