package com.tgp.mockexam.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.tgp.mockexam.core.domain.repository.SessionRepository
import com.tgp.mockexam.core.model.AuthUser
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class DefaultSessionRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : SessionRepository {

    private object Keys {
        val cookie = stringPreferencesKey("auth_cookie")
        val userId = stringPreferencesKey("auth_user_id")
        val name = stringPreferencesKey("auth_user_name")
        val email = stringPreferencesKey("auth_user_email")
    }

    override val cookie: Flow<String?> = dataStore.data.map { it[Keys.cookie] }

    override val user: Flow<AuthUser?> = dataStore.data.map { prefs ->
        val id = prefs[Keys.userId]
        val email = prefs[Keys.email]
        if (id.isNullOrBlank() || email.isNullOrBlank()) {
            null
        } else {
            AuthUser(id = id, name = prefs[Keys.name].orEmpty(), email = email)
        }
    }

    override suspend fun saveSession(cookie: String, user: AuthUser) {
        dataStore.edit { prefs ->
            prefs[Keys.cookie] = cookie
            prefs[Keys.userId] = user.id
            prefs[Keys.name] = user.name
            prefs[Keys.email] = user.email
        }
    }

    override suspend fun clearSession() {
        dataStore.edit { prefs ->
            prefs.remove(Keys.cookie)
            prefs.remove(Keys.userId)
            prefs.remove(Keys.name)
            prefs.remove(Keys.email)
        }
    }
}
