package com.tgp.mockexam.core.network

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive

/**
 * A single TablesDB query, encoded as the JSON object the REST API expects:
 * `{"method":"equal","attribute":"paperId","values":["..."]}`,
 * `{"method":"offset","attribute":"","values":[25]}`.
 * Note: pagination runs through `queries[]`, server-side `limit` is capped at 25/page.
 */
@Serializable
data class TablesQuery(
    val method: String,
    val attribute: String = "",
    val values: List<JsonPrimitive> = emptyList(),
) {
    companion object {
        fun equal(attribute: String, value: String): TablesQuery =
            TablesQuery(method = "equal", attribute = attribute, values = listOf(JsonPrimitive(value)))

        fun offset(value: Int): TablesQuery =
            TablesQuery(method = "offset", attribute = "", values = listOf(JsonPrimitive(value)))

        fun limit(value: Int): TablesQuery =
            TablesQuery(method = "limit", attribute = "", values = listOf(JsonPrimitive(value)))

        fun orderDesc(attribute: String): TablesQuery =
            TablesQuery(method = "orderDesc", attribute = attribute, values = emptyList())

        fun orderAsc(attribute: String): TablesQuery =
            TablesQuery(method = "orderAsc", attribute = attribute, values = emptyList())
    }
}