package com.tgp.mockexam.core.data.sync

import com.tgp.mockexam.core.common.DispatcherProvider
import com.tgp.mockexam.core.database.dao.ContentDao
import com.tgp.mockexam.core.data.mapper.toEntity
import com.tgp.mockexam.core.network.RemotePaperRow
import com.tgp.mockexam.core.network.RemoteQuestionRow
import com.tgp.mockexam.core.network.TablesClient
import com.tgp.mockexam.core.network.TablesQuery
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonObject

/**
 * Pulls Appwrite content into the Room cache.
 *
 * - Papers are fetched wholesale (server page max 25, few enough for one page).
 * - Questions are fetched per paper, paginated with [TablesQuery.offset] (server caps pages at 25).
 * - A paper is skipped when its cached [contentHash] matches AND the cached question count already
 *   matches [totalQuestions], so resume/cold re-syncs are near-free.
 */
@Singleton
class AppwriteContentSyncer @Inject constructor(
    private val client: TablesClient,
    private val dao: ContentDao,
    private val dispatchers: DispatcherProvider,
) {
    private val syncMutex = Mutex()
    private val _state = MutableStateFlow(SyncState.IDLE)
    val state: StateFlow<SyncState> = _state.asStateFlow()

    private val fireAndForget = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /** Non-blocking kick; serialized by [syncMutex]. Safe from any thread. */
    fun requestSync() {
        if (_state.value == SyncState.IN_PROGRESS) return
        fireAndForget.launch { sync() }
    }

    suspend fun sync(): SyncResult = withContext(dispatchers.io) {
        if (_state.value == SyncState.IN_PROGRESS) return@withContext SyncResult.Success
        _state.value = SyncState.IN_PROGRESS
        try {
            syncMutex.withLock { doSync() }
            SyncResult.Success
        } catch (throwable: Throwable) {
            if (throwable is CancellationException) throw throwable
            android.util.Log.w("AppwriteContentSyncer", "sync failed", throwable)
            SyncResult.Failure(throwable)
        } finally {
            _state.value = SyncState.IDLE
        }
    }

    private suspend fun doSync() {
        val papers = fetchAll("papers").map { RemotePaperRow(it).toEntity() }
        android.util.Log.i("AppwriteContentSyncer", "papers fetched: ${papers.size}")
        dao.upsertPapers(papers)
        var questionTotal = 0
        papers.forEach { paper ->
            val cachedHash = dao.contentHash(paper.id)
            val cachedCount = dao.countQuestions(paper.id)
            if (cachedHash == paper.contentHash && cachedCount >= paper.totalQuestions) {
                return@forEach
            }
            val questions = fetchAll(
                table = "questions",
                queries = listOf(TablesQuery.equal("paperId", paper.id)),
            ).map { RemoteQuestionRow(it).toEntity() }
            questionTotal += questions.size
            dao.upsertQuestions(questions)
        }
        android.util.Log.i("AppwriteContentSyncer", "questions fetched: $questionTotal")
    }

    private suspend fun fetchAll(
        table: String,
        queries: List<TablesQuery> = emptyList(),
        pageSize: Int = 25,
        maxPages: Int = 200,
    ): List<JsonObject> {
        val out = mutableListOf<JsonObject>()
        var offset = 0
        for (pageIndex in 0 until maxPages) {
            val page = client.getRows(
                table = table,
                queries = queries + TablesQuery.offset(offset),
                limit = pageSize,
            )
            out += page.rows
            offset += page.rows.size
            if (page.rows.isEmpty() || out.size >= page.total) break
        }
        return out
    }
}