package com.tgp.mockexam.core.network

import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

/**
 * [TablesClient] backed by [HttpURLConnection]. No external HTTP deps.
 * Auth is optional for content tables (read `any`); only the project header is sent.
 */
class HttpTablesClient(
    private val appwriteConfig: AppwriteConfig,
) : TablesClient {

    private val json = Json { ignoreUnknownKeys = true; isLenient = true; encodeDefaults = true }

    override suspend fun getRows(
        table: String,
        queries: List<TablesQuery>,
        limit: Int,
    ): RemoteRows = withContext(Dispatchers.IO) {
        val base = "${appwriteConfig.endpoint}/tablesdb/${appwriteConfig.databaseId}/tables/$table/rows"
        val qs = buildQueryString(queries, limit)
        val connection = URL(if (qs.isEmpty()) base else "$base?$qs").openConnection() as HttpURLConnection
        try {
            connection.requestMethod = "GET"
            connection.connectTimeout = 15_000
            connection.readTimeout = 120_000
            connection.setRequestProperty("X-Appwrite-Project", appwriteConfig.projectId)
            connection.setRequestProperty("Accept", "application/json")

            val code = connection.responseCode
            if (code !in 200..299) {
                val body = connection.errorStream?.bufferedReader()?.use { it.readText() }.orEmpty()
                throw TablesClientException("GET $table failed with HTTP $code: ${body.take(300)}")
            }
            val body = connection.inputStream.bufferedReader().use { it.readText() }
            json.decodeFromString<RemoteRows>(body)
        } finally {
            connection.disconnect()
        }
    }

    private fun buildQueryString(queries: List<TablesQuery>, limit: Int): String {
        val params = mutableListOf<String>()
        queries.forEachIndexed { index, query ->
            val name = URLEncoder.encode("queries[$index]", "UTF-8")
            val value = URLEncoder.encode(json.encodeToString(TablesQuery.serializer(), query), "UTF-8")
            params.add("$name=$value")
        }
        params.add("limit=$limit")
        return params.joinToString("&")
    }
}