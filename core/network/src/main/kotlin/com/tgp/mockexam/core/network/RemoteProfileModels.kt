package com.tgp.mockexam.core.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull

@Serializable
data class RemoteProfile(
    @SerialName("\$id") val id: String,
    val userId: String = "",
    val role: String = "student",
)

class RemoteProfileRow(private val row: JsonObject) {
    val id: String get() = row["id"]?.let { if (it is JsonPrimitive && it.isString) it.content else null } ?: row["\$id"]?.let { if (it is JsonPrimitive && it.isString) it.content else null }.orEmpty()
    val userId: String get() = row["userId"]?.let { if (it is JsonPrimitive && it.isString) it.content else null }.orEmpty()
    val role: String get() = row["role"]?.let { if (it is JsonPrimitive && it.isString) it.content else null }.orEmpty()
}
