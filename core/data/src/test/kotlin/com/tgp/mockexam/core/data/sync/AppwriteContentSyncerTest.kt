package com.tgp.mockexam.core.data.sync

import com.tgp.mockexam.core.common.DispatcherProvider
import com.tgp.mockexam.core.data.mapper.toDomain
import com.tgp.mockexam.core.database.dao.ContentDao
import com.tgp.mockexam.core.database.model.CachedPaperEntity
import com.tgp.mockexam.core.database.model.CachedQuestionEntity
import com.tgp.mockexam.core.model.Question
import com.tgp.mockexam.core.network.RemoteRows
import com.tgp.mockexam.core.network.TablesClient
import com.tgp.mockexam.core.network.TablesQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

private val json = Json { ignoreUnknownKeys = true }

private val dispatchers = object : DispatcherProvider {
    override val main = Dispatchers.Unconfined
    override val io = Dispatchers.Unconfined
    override val default = Dispatchers.Unconfined
}

private class FakeTablesClient(
    private val byTable: Map<String, List<JsonObject>>,
) : TablesClient {
    val questionOffsets = mutableListOf<Int>()
    override suspend fun getRows(table: String, queries: List<TablesQuery>, limit: Int): RemoteRows {
        val rows = byTable[table].orEmpty()
        val offset = queries.filter { it.method == "offset" }
            .sumOf { it.values.firstOrNull()?.content?.toIntOrNull() ?: 0 }
        if (table == "questions") questionOffsets += offset
        val page = rows.drop(offset).take(limit)
        return RemoteRows(total = rows.size.toLong(), rows = page)
    }
}

private class FakeDao : ContentDao {
    val papers = mutableListOf<CachedPaperEntity>()
    val questions = mutableListOf<CachedQuestionEntity>()
    var contentHashes = mutableMapOf<String, String>()

    override suspend fun upsertPapers(papers: List<CachedPaperEntity>) {
        this.papers += papers
    }

    override suspend fun upsertQuestions(questions: List<CachedQuestionEntity>) {
        this.questions += questions
    }

    override suspend fun listPapers(): List<CachedPaperEntity> = papers

    override fun observePaper(id: String): Flow<CachedPaperEntity?> = flowOf(papers.firstOrNull { it.id == id })

    override suspend fun getPaper(id: String): CachedPaperEntity? = papers.firstOrNull { it.id == id }

    override fun observeQuestions(paperId: String): Flow<List<CachedQuestionEntity>> =
        flowOf(questions.filter { it.paperId == paperId })

    override suspend fun listQuestions(paperId: String): List<CachedQuestionEntity> =
        questions.filter { it.paperId == paperId }

    override suspend fun countQuestions(paperId: String): Int = questions.count { it.paperId == paperId }

    override suspend fun contentHash(id: String): String? = contentHashes[id]

    override suspend fun paperCount(): Int = papers.size
}

private fun questionRow(paperId: String, n: Int, answer: Int = 1) = buildJsonObject {
    put("paperId", paperId)
    put("n", n)
    put("promptEn", "Prompt $n")
    put("optionsEn", """["A","B","C","D"]""")
    put("optionsTe", """["అ","బ","స","డ"]""")
    put("answer", answer)
    put("updatedAt", "2026-01-01T00:00:00+00:00")
}

private fun paperRow(id: String, hash: String, total: Int = 200) = buildJsonObject {
    put("\u0024id", id)
    put("track", "si")
    put("stage", "mains")
    put("year", 2018)
    put("paperNo", 1)
    put("series", "A")
    put("titleEn", "SI Mains $id")
    put("titleTe", "")
    put("marks", 200)
    put("durationMins", 180)
    put("totalQuestions", total)
    put("reviewPending", true)
    put("isFree", false)
    put("contentHash", hash)
    put("updatedAt", "2026-01-01T00:00:00+00:00")
}

internal class AppwriteContentSyncerTest {

    private fun syncer(client: TablesClient, dao: FakeDao) = AppwriteContentSyncer(client, dao, dispatchers)

    @Test
    fun `sync paginates questions across pages`() = runBlocking {
        val paper = paperRow("p1", "hash-new", total = 60)
        val rows = (1..60).map { questionRow("p1", it) }
        val client = FakeTablesClient(mapOf("papers" to listOf(paper), "questions" to rows))
        val dao = FakeDao()

        val result = syncer(client, dao).sync()

        assertTrue(result is SyncResult.Success)
        assertEquals(1, dao.papers.size)
        assertEquals(60, dao.questions.size)
        assertEquals(listOf(0, 25, 50), client.questionOffsets)
    }

    @Test
    fun `sync skips unchanged papers (matching hash and count)`() = runBlocking {
        val paper = paperRow("p1", "hash-same", total = 200)
        val client = FakeTablesClient(mapOf("papers" to listOf(paper), "questions" to listOf()))
        val dao = FakeDao().apply {
            contentHashes["p1"] = "hash-same"
            questions += (1..200).map { CachedQuestionEntity(
                paperId = "p1", n = it, promptEn = "x", promptTe = "", optionsEn = "[]", optionsTe = "[]",
                answer = 1, solutionEn = "", solutionTe = "", ruleEn = "", ruleTe = "",
                topic = "", conceptKey = "", version = 1, status = "reviewPending", updatedAt = "",
            ) }
        }

        val result = syncer(client, dao).sync()

        assertTrue(result is SyncResult.Success)
        assertTrue(client.questionOffsets.isEmpty()) // no question pages fetched
    }

    @Test
    fun `sync fetches questions when hash changed`() = runBlocking {
        val paper = paperRow("p1", "hash-new", total = 50)
        val rows = (1..50).map { questionRow("p1", it) }
        val client = FakeTablesClient(mapOf("papers" to listOf(paper), "questions" to rows))
        val dao = FakeDao().apply { contentHashes["p1"] = "hash-old" }

        syncer(client, dao).sync()

        assertEquals(listOf(0, 25), client.questionOffsets)
        assertEquals(50, dao.questions.size)
    }

    @Test
    fun `unknown answers map to domain UNKNOWN and skip scoring`() = runBlocking {
        val paper = paperRow("p1", "hash-new", total = 2)
        val rows = listOf(
            questionRow("p1", 1, answer = 4),
            questionRow("p1", 2, answer = -1),
        )
        val client = FakeTablesClient(mapOf("papers" to listOf(paper), "questions" to rows))
        val dao = FakeDao()

        syncer(client, dao).sync()

        val domain = dao.papers.first().toDomain(dao.questions)
        assertEquals(2, domain.questions.size)
        assertEquals(3, domain.questions[0].answerIndex)
        assertEquals(Question.UNKNOWN_ANSWER_INDEX, domain.questions[1].answerIndex)
        assertEquals(4, domain.questions[0].options.size)
        assertEquals("అ", domain.questions[0].options[0].text.te)
        assertFalse(domain.questions[1].hasAnswer)
    }
}