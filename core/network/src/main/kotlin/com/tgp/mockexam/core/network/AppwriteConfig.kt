package com.tgp.mockexam.core.network

/**
 * Appwrite project connection details for the TGP backend (project `tgp`, region fra).
 * Content tables are read `any`/guest, so the app only sends the project header.
 */
data class AppwriteConfig(
    val endpoint: String = DEFAULT_ENDPOINT,
    val projectId: String = DEFAULT_PROJECT_ID,
    val databaseId: String = DEFAULT_DATABASE_ID,
    val pageSize: Int = 25,
) {
    companion object {
        const val DEFAULT_ENDPOINT = "https://fra.cloud.appwrite.io/v1"
        const val DEFAULT_PROJECT_ID = "tgp"
        const val DEFAULT_DATABASE_ID = "tgp"

        /** Custom-scheme deep links the browser is sent back to after a social / magic-link sign-in. */
        const val CALLBACK_SCHEME = "appwrite-callback-tgp"
        const val OAUTH_SUCCESS_URL = "$CALLBACK_SCHEME://oauth"
        const val OAUTH_FAILURE_URL = "$CALLBACK_SCHEME://oauth-error"
        const val MAGIC_LINK_URL = "$CALLBACK_SCHEME://magic-link"
    }
}