package com.tgp.mockexam.core.domain.repository

import com.tgp.mockexam.core.common.TgpResult
import com.tgp.mockexam.core.model.AuthUser
import kotlinx.coroutines.flow.Flow

/**
 * Account operations backed by Appwrite. Implementations persist the session and expose the
 * cached user so the UI is offline-first (the last known account shows even without network).
 */
interface AuthRepository {
    fun observeUser(): Flow<AuthUser?>
    suspend fun refreshUser(): TgpResult<AuthUser?>
    suspend fun signUp(name: String, email: String, password: String): TgpResult<AuthUser>
    suspend fun signIn(email: String, password: String): TgpResult<AuthUser>
    suspend fun signOut(): TgpResult<Unit>

    /** URL to open in a browser to start Google sign-in (Appwrite OAuth2). */
    fun googleSignInUrl(): String

    /** Completes a social / magic-link redirect captured from the app's deep link. */
    suspend fun completeOAuthCallback(callbackUrl: String): TgpResult<AuthUser?>

    /** Emails a one-time sign-in link (magic link). */
    suspend fun sendMagicLink(email: String): TgpResult<Unit>
}

/** Read/write access to the persisted Appwrite session (cookie + cached account). */
interface SessionRepository {
    val cookie: Flow<String?>
    val user: Flow<AuthUser?>
    suspend fun saveSession(cookie: String, user: AuthUser)
    suspend fun clearSession()
}
