package com.tgp.mockexam.core.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Appwrite `account` user response (only the fields the app needs). */
@Serializable
data class RemoteUser(
    @SerialName("\$id") val id: String,
    val name: String = "",
    val email: String = "",
)

@Serializable
data class RemoteSession(
    @SerialName("\$id") val id: String,
    val userId: String = "",
    val secret: String = "",
)

@Serializable
data class AppwriteError(
    val message: String = "Something went wrong",
    val code: Int = 0,
    val type: String = "",
)

/** Result of a login/signup: the user plus the `a_session_<project>` cookie to persist. */
data class AccountSession(
    val user: RemoteUser,
    val cookie: String,
)
