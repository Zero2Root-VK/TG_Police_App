package com.tgp.mockexam.feature.auth

import androidx.compose.runtime.Immutable
import com.tgp.mockexam.core.model.AuthUser
import com.tgp.mockexam.core.model.ContentLanguage

enum class AuthMode { SIGN_IN, SIGN_UP }

@Immutable
data class AuthUiState(
    val mode: AuthMode = AuthMode.SIGN_IN,
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null,
    val user: AuthUser? = null,
    val role: String = "student",
    val language: ContentLanguage = ContentLanguage.ENGLISH,
    /** When non-null the screen opens this URL in a browser (Google sign-in). */
    val browserUrl: String? = null,
    /** True once a magic sign-in link has been emailed. */
    val magicLinkSent: Boolean = false,
) {
    val canSubmit: Boolean
        get() = !loading &&
            email.trim().contains("@") &&
            password.length >= 8 &&
            (mode == AuthMode.SIGN_IN || name.isNotBlank())

    val canSendMagicLink: Boolean
        get() = !loading && email.trim().contains("@")
}
