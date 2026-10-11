package com.tgp.mockexam.core.network

import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

interface AdminWriteClient {
    suspend fun upsertPaper(paperId: String, data: JsonObject, apiKey: String)
    suspend fun upsertQuestion(rowId: String, data: JsonObject, apiKey: String)
    suspend fun createRow(table: String, rowId: String, data: JsonObject, apiKey: String): JsonObject
    suspend fun updateRow(table: String, rowId: String, data: JsonObject, apiKey: String): JsonObject
}

class HttpAdminWriteClient(
    private val appwriteConfig: AppwriteConfig,
) : AdminWriteClient {
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    override suspend fun upsertPaper(paperId: String, data: JsonObject, apiKey: String) {
        upsert("papers", paperId, data, apiKey)
    }

    override suspend fun upsertQuestion(rowId: String, data: JsonObject, apiKey: String) {
        upsert("questions", rowId, data, apiKey)
    }

    private suspend fun upsert(table: String, rowId: String, data: JsonObject, apiKey: String): JsonObject = withContext(Dispatchers.IO) {
        val base = "${appwriteConfig.endpoint}/tablesdb/${appwriteConfig.databaseId}/tables/$table/rows/$rowId"
        val conn = URL(base).openConnection() as HttpURLConnection
        try {
            conn.requestMethod = "PUT"
            conn.connectTimeout = 15_000
            conn.readTimeout = 120_000
            conn.setRequestProperty("X-Appwrite-Project", appwriteConfig.projectId)
            conn.setRequestProperty("X-Appwrite-Key", apiKey)
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Accept", "application/json")
            conn.doOutput = true
            val body = buildJsonObject { put("data", data) }
            conn.outputStream.use { it.write(json.encodeToString(JsonObject.serializer(), body).toByteArray()) }
            val code = conn.responseCode
            val resp = if (code in 200..299) conn.inputStream.bufferedReader().use { it.readText() } else conn.errorStream?.bufferedReader()?.use { it.readText() }.orEmpty()
            if (code !in 200..299) throw AdminWriteException(resp)
            json.decodeFromString(JsonObject.serializer(), resp)
        } finally {
            conn.disconnect()
        }
    }

    override suspend fun createRow(table: String, rowId: String, data: JsonObject, apiKey: String): JsonObject = upsert(table, rowId, data, apiKey)
    override suspend fun updateRow(table: String, rowId: String, data: JsonObject, apiKey: String): JsonObject = upsert(table, rowId, data, apiKey)
}

class AdminWriteException(message: String) : Exception(message)
