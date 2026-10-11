package com.tgp.mockexam.core.network

import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * [AccountClient] over [HttpURLConnection]. Content tables need only the project header, but
 * account endpoints rely on the `a_session_<projectId>` cookie returned by the server.
 */
class HttpAccountClient(
    private val appwriteConfig: AppwriteConfig,
) : AccountClient {

    private val json = Json { ignoreUnknownKeys = true; isLenient = true; encodeDefaults = true }
    private val sessionCookieName = "a_session_${appwriteConfig.projectId}"

    override suspend fun createAccount(
        userId: String,
        email: String,
        password: String,
        name: String,
    ): RemoteUser {
        val body = json.encodeToString(
            kotlinx.serialization.json.JsonObject.serializer(),
            buildJsonObject {
                put("userId", userId)
                put("email", email)
                put("password", password)
                put("name", name)
            },
        )
        val response = request("POST", "/account", body = body)
        return json.decodeFromString<RemoteUser>(response.body)
    }

    override suspend fun createEmailSession(email: String, password: String): AccountSession {
        val body = json.encodeToString(
            kotlinx.serialization.json.JsonObject.serializer(),
            buildJsonObject {
                put("email", email)
                put("password", password)
            },
        )
        val response = request("POST", "/account/sessions/email", body = body)
        val session = json.decodeFromString<RemoteSession>(response.body)
        val cookie = response.sessionCookie
            ?: throw AccountException("Login succeeded but no session cookie was returned", 500)
        val user = getAccount(cookie)
        return AccountSession(user = user, cookie = cookie)
    }

    override suspend fun getAccount(cookie: String): RemoteUser = withContext(Dispatchers.IO) {
        val response = request("GET", "/account", cookie = cookie)
        json.decodeFromString<RemoteUser>(response.body)
    }

    override suspend fun deleteCurrentSession(cookie: String) {
        runCatching { request("DELETE", "/account/sessions/current", cookie = cookie) }
    }

    override fun oauth2SessionUrl(provider: String): String {
        val success = urlEncode(AppwriteConfig.OAUTH_SUCCESS_URL)
        val failure = urlEncode(AppwriteConfig.OAUTH_FAILURE_URL)
        return "${appwriteConfig.endpoint}/account/sessions/oauth2/$provider" +
            "?project=${appwriteConfig.projectId}&success=$success&failure=$failure"
    }

    override suspend fun createSessionFromToken(userId: String, secret: String): AccountSession {
        val body = json.encodeToString(
            kotlinx.serialization.json.JsonObject.serializer(),
            buildJsonObject {
                put("userId", userId)
                put("secret", secret)
            },
        )
        val response = request("POST", "/account/sessions/token", body = body)
        val cookie = response.sessionCookie
            ?: throw AccountException("Sign-in succeeded but no session cookie was returned", 500)
        val user = getAccount(cookie)
        return AccountSession(user = user, cookie = cookie)
    }

    override suspend fun completeRedirect(callbackUrl: String): AccountSession {
        val params = parseQuery(callbackUrl)
        val secret = params["secret"]
        val userId = params["userId"]
        // Token flows (OAuth2 token / magic link) return userId + secret.
        if (!userId.isNullOrBlank() && !secret.isNullOrBlank()) {
            return createSessionFromToken(userId, secret)
        }
        // Session flows return the session cookie directly as `key` = cookie name, `secret` = value.
        val key = params["key"]
        if (!key.isNullOrBlank() && !secret.isNullOrBlank()) {
            val cookie = "$key=$secret"
            val user = getAccount(cookie)
            return AccountSession(user = user, cookie = cookie)
        }
        throw AccountException("Sign-in callback is missing its session parameters", 400)
    }

    override suspend fun createEmailToken(userId: String, email: String, url: String) {
        val body = json.encodeToString(
            kotlinx.serialization.json.JsonObject.serializer(),
            buildJsonObject {
                put("userId", userId)
                put("email", email)
                put("url", url)
            },
        )
        request("POST", "/account/tokens/email", body = body)
    }

    private fun parseQuery(url: String): Map<String, String> {
        val rawQuery = url.substringAfter('?', missingDelimiterValue = "")
        if (rawQuery.isEmpty()) return emptyMap()
        return rawQuery.split("&").mapNotNull { pair ->
            val idx = pair.indexOf('=')
            if (idx <= 0) return@mapNotNull null
            val name = percentDecode(pair.substring(0, idx))
            val value = percentDecode(pair.substring(idx + 1))
            name to value
        }.toMap()
    }

    /** Minimal percent-decoding that leaves `+` untouched (Appwrite percent-encodes its parameters). */
    private fun percentDecode(value: String): String {
        if ('%' !in value) return value
        val out = StringBuilder(value.length)
        var i = 0
        while (i < value.length) {
            val c = value[i]
            if (c == '%' && i + 2 < value.length) {
                val hex = value.substring(i + 1, i + 3).toIntOrNull(16)
                if (hex != null) {
                    out.append(hex.toChar())
                    i += 3
                    continue
                }
            }
            out.append(c)
            i++
        }
        return out.toString()
    }

    private fun urlEncode(value: String): String =
        java.net.URLEncoder.encode(value, "UTF-8").replace("+", "%20")


    private suspend fun request(
        method: String,
        path: String,
        body: String? = null,
        cookie: String? = null,
    ): HttpResult = withContext(Dispatchers.IO) {
        val connection = URL("${appwriteConfig.endpoint}$path").openConnection() as HttpURLConnection
        try {
            connection.requestMethod = method
            connection.connectTimeout = 20_000
            connection.readTimeout = 30_000
            connection.setRequestProperty("X-Appwrite-Project", appwriteConfig.projectId)
            connection.setRequestProperty("Accept", "application/json")
            if (cookie != null) connection.setRequestProperty("Cookie", cookie)
            if (body != null) {
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json")
                connection.outputStream.use { it.write(body.toByteArray(Charsets.UTF_8)) }
            }

            val code = connection.responseCode
            val stream = if (code in 200..299) connection.inputStream else connection.errorStream
            val responseBody = stream?.bufferedReader()?.use { it.readText() }.orEmpty()
            if (code !in 200..299) {
                val parsed = runCatching { json.decodeFromString<AppwriteError>(responseBody) }.getOrNull()
                throw AccountException(
                    message = parsed?.message ?: "Request failed (HTTP $code)",
                    code = parsed?.code ?: code,
                    type = parsed?.type.orEmpty(),
                )
            }
            HttpResult(body = responseBody, sessionCookie = extractSessionCookie(connection.headerFields))
        } finally {
            connection.disconnect()
        }
    }

    private fun extractSessionCookie(headers: Map<String, List<String>>): String? {
        val raw = headers.entries
            .filter { it.key.equals("Set-Cookie", ignoreCase = true) }
            .flatMap { it.value }
            .joinToString("; ")
        val match = Regex("$sessionCookieName=([^;]+)").find(raw) ?: return null
        return "$sessionCookieName=${match.groupValues[1]}"
    }

    private data class HttpResult(
        val body: String,
        val sessionCookie: String? = null,
    )
}
