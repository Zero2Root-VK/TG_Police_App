package com.tgp.mockexam.core.data.repository

import android.content.Context
import com.tgp.mockexam.core.common.DispatcherProvider
import com.tgp.mockexam.core.data.json.PaperDto
import com.tgp.mockexam.core.data.json.PaperIndexItemDto
import com.tgp.mockexam.core.data.mapper.toDomain
import com.tgp.mockexam.core.domain.repository.PaperRepository
import com.tgp.mockexam.core.model.Paper
import com.tgp.mockexam.core.model.PaperSummary
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

private const val ASSET_DIR = "papers"
private const val INDEX_FILE = "index.json"

@Singleton
class AssetPaperRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dispatchers: DispatcherProvider,
) : PaperRepository {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val mutex = Mutex()
    private var indexCache: List<PaperSummary>? = null
    private val paperCache = mutableMapOf<String, Paper>()

    override fun observePaper(paperId: String): Flow<Paper?> = flow { emit(getPaper(paperId)) }

    override suspend fun getPaper(paperId: String): Paper? = withContext(dispatchers.io) {
        mutex.withLock {
            paperCache[paperId] ?: runCatching {
                val text = readAsset("$ASSET_DIR/$paperId.json")
                json.decodeFromString<PaperDto>(text).toDomain().also { paperCache[paperId] = it }
            }.getOrNull()
        }
    }

    override suspend fun listPapers(): List<PaperSummary> = withContext(dispatchers.io) {
        mutex.withLock {
            indexCache ?: runCatching {
                val text = readAsset("$ASSET_DIR/$INDEX_FILE")
                json.decodeFromString<List<PaperIndexItemDto>>(text).map { it.toDomain() }
            }.getOrDefault(emptyList()).also { indexCache = it }
        }
    }

    private fun readAsset(path: String): String =
        context.assets.open(path).bufferedReader().use { it.readText() }
}
