package com.tgp.mockexam.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.tgp.mockexam.core.domain.repository.UserPreferencesRepository
import com.tgp.mockexam.core.model.ContentLanguage
import com.tgp.mockexam.core.model.Track
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class DefaultUserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : UserPreferencesRepository {

    private object Keys {
        val onboardingCompleted = booleanPreferencesKey("onboarding_completed")
        val uiLanguage = stringPreferencesKey("ui_language")
        val selectedTrack = stringPreferencesKey("selected_track")
    }

    override val onboardingCompleted: Flow<Boolean> =
        dataStore.data.map { it[Keys.onboardingCompleted] ?: false }

    override val uiLanguage: Flow<ContentLanguage> = dataStore.data.map { prefs ->
        val stored = prefs[Keys.uiLanguage]
        ContentLanguage.entries.firstOrNull { it.name == stored } ?: ContentLanguage.ENGLISH
    }

    override val selectedTrack: Flow<Track?> = dataStore.data.map { prefs ->
        prefs[Keys.selectedTrack]?.let { Track.fromId(it) }
    }

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { it[Keys.onboardingCompleted] = completed }
    }

    override suspend fun setUiLanguage(language: ContentLanguage) {
        dataStore.edit { it[Keys.uiLanguage] = language.name }
    }

    override suspend fun setSelectedTrack(track: Track?) {
        dataStore.edit { prefs ->
            if (track == null) prefs.remove(Keys.selectedTrack) else prefs[Keys.selectedTrack] = track.id
        }
    }
}
