package com.tgp.mockexam.core.network

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class TablesQueryTest {

    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    @Test
    fun equalQuery_encodes_likeBackend() {
        assertEquals(
            """{"method":"equal","attribute":"paperId","values":["constable-prelims-2015"]}""",
            json.encodeToString(TablesQuery.serializer(), TablesQuery.equal("paperId", "constable-prelims-2015")),
        )
    }

    @Test
    fun offsetQuery_encodes_likeBackend() {
        assertEquals(
            """{"method":"offset","attribute":"","values":[25]}""",
            json.encodeToString(TablesQuery.serializer(), TablesQuery.offset(25)),
        )
    }

    @Test
    fun limitQuery_encodes_likeBackend() {
        assertEquals(
            """{"method":"limit","attribute":"","values":[25]}""",
            json.encodeToString(TablesQuery.serializer(), TablesQuery.limit(25)),
        )
    }

    @Test
    fun rowsResponse_decodes_ignoringUnknownFields() {
        val encoded = """
            {"total":200,"rows":[{"paperId":"constable-prelims-2015","n":1,
            "promptEn":"Hi","optionsEn":"[\"a\",\"b\"]","answer":4,
            "updatedAt":"2026-01-01T00:00:00+00:00","${'$'}id":"abc","${'$'}permissions":[]}]}
        """.trimIndent()
        val parsed = json.decodeFromString<RemoteRows>(encoded)
        assertEquals(200L, parsed.total)
        assertEquals(1, parsed.rows.size)
        val row = RemoteQuestionRow(parsed.rows.first())
        assertEquals("constable-prelims-2015", row.paperId)
        assertEquals(1, row.n)
        assertEquals(4, row.answer)
        assertEquals(1, row.version)
    }
}