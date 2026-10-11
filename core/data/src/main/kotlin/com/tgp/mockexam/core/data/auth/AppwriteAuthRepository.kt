package com.tgp.mockexam.core.data.auth

import com.tgp.mockexam.core.common.TgpResult
import com.tgp.mockexam.core.domain.repository.AuthRepository
import com.tgp.mockexam.core.domain.repository.SessionRepository
import com.tgp.mockexam.core.model.AuthUser
import com.tgp.mockexam.core.network.AccountClient
import com.tgp.mockexam.core.network.AccountException
import com.tgp.mockexam.core.network.AppwriteConfig
import com.tgp.mockexam.core.network.RemoteUser
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@Singleton
class AppwriteAuthRepository @Inject constructor(
    private val accountClient: AccountClient,
    private val sessionRepository: SessionRepository,
) : AuthRepository {

    override fun observeUser(): Flow<AuthUser?> = sessionRepository.user

    override suspend fun refreshUser(): TgpResult<AuthUser?> {
        val cookie = sessionRepository.cookie.first() ?: return TgpResult.Success(null)
        return try {
            val user = accountClient.getAccount(cookie).toModel()
            sessionRepository.saveSession(cookie, user)
            TgpResult.Success(user)
        } catch (networkError: AccountException) {
            if (networkError.code == 401) {
                sessionRepository.clearSession()
                TgpResult.Success(null)
            } else {
                TgpResult.Error(networkError)
            }
        } catch (throwable: Throwable) {
            TgpResult.Error(throwable)
        }
    }

    override suspend fun signUp(
        name: String,
        email: String,
        password: String,
    ): TgpResult<AuthUser> = runCatching {
        val userId = "u" + UUID.randomUUID().toString().replace("-", "").take(30)
        accountClient.createAccount(
            userId = userId,
            email = email.trim(),
            password = password,
            name = name.trim(),
        )
        val session = accountClient.createEmailSession(email = email.trim(), password = password)
        val user = session.user.toModel()
        sessionRepository.saveSession(cookie = session.cookie, user = user)
        user
    }.toTgpResult()

    override suspend fun signIn(email: String, password: String): TgpResult<AuthUser> =
        runCatching {
            val session = accountClient.createEmailSession(email = email.trim(), password = password)
            val user = session.user.toModel()
            sessionRepository.saveSession(cookie = session.cookie, user = user)
            user
        }.toTgpResult()

    override suspend fun signOut(): TgpResult<Unit> = runCatching {
        sessionRepository.cookie.first()?.let { accountClient.deleteCurrentSession(it) }
        sessionRepository.clearSession()
    }.toTgpResult()

    override fun googleSignInUrl(): String = accountClient.oauth2SessionUrl("google")

    override suspend fun completeOAuthCallback(callbackUrl: String): TgpResult<AuthUser?> = runCatching {
        val session = accountClient.completeRedirect(callbackUrl)
        val user = session.user.toModel()
        sessionRepository.saveSession(cookie = session.cookie, user = user)
        user
    }.toTgpResult()

    override suspend fun sendMagicLink(email: String): TgpResult<Unit> = runCatching {
        val userId = "u" + UUID.randomUUID().toString().replace("-", "").take(30)
        accountClient.createEmailToken(
            userId = userId,
            email = email.trim(),
            url = AppwriteConfig.MAGIC_LINK_URL,
        )
    }.toTgpResult()

    private fun RemoteUser.toModel(): AuthUser = AuthUser(id = id, name = name, email = email)

    private inline fun <T> Result<T>.toTgpResult(): TgpResult<T> = fold(
        onSuccess = { TgpResult.Success(it) },
        onFailure = { TgpResult.Error(it) },
    )
}
