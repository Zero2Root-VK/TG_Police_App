package com.tgp.mockexam.core.network

/**
 * Minimal Appwrite Account REST client (no SDK). Authentication is delivered as the
 * `a_session_<projectId>` cookie, which callers persist and resend on later requests.
 */
interface AccountClient {
    /** Creates an account. Appwrite does not return a session here, so callers then log in. */
    suspend fun createAccount(userId: String, email: String, password: String, name: String): RemoteUser

    /** Creates an email/password session and returns the user plus the session cookie. */
    suspend fun createEmailSession(email: String, password: String): AccountSession

    /** Fetches the current account using a persisted session cookie. */
    suspend fun getAccount(cookie: String): RemoteUser

    /** Deletes the current session. Safe to ignore failures on logout. */
    suspend fun deleteCurrentSession(cookie: String)

    /**
     * Builds the Appwrite OAuth2 authorization URL to open in a browser for [provider]
     * (`google`, `github`, …). On success the browser is redirected to `AppwriteConfig.OAUTH_SUCCESS_URL`.
     */
    fun oauth2SessionUrl(provider: String): String

    /** Creates a session from a verification token (`userId` + `secret`), e.g. magic-link sign-in. */
    suspend fun createSessionFromToken(userId: String, secret: String): AccountSession

    /**
     * Completes an OAuth2 / magic-link redirect by reading the session parameters Appwrite appends
     * to the callback URL (`userId`/`secret`, or `key`/`secret` = cookie name/value).
     */
    suspend fun completeRedirect(callbackUrl: String): AccountSession

    /** Emails a one-time sign-in link for [email]; the link redirects back to [url]. */
    suspend fun createEmailToken(userId: String, email: String, url: String)
}

class AccountException(
    override val message: String,
    val code: Int = 0,
    val type: String = "",
    cause: Throwable? = null,
) : Exception(message, cause)
