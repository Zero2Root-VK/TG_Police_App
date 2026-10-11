package com.tgp.mockexam.core.data.repository

import com.tgp.mockexam.core.common.DispatcherProvider
import com.tgp.mockexam.core.data.mapper.toDomain
import com.tgp.mockexam.core.data.mapper.toSummary
import com.tgp.mockexam.core.data.sync.AppwriteContentSyncer
import com.tgp.mockexam.core.database.dao.ContentDao
import com.tgp.mockexam.core.domain.repository.PaperRepository
import com.tgp.mockexam.core.model.Paper
import com.tgp.mockexam.core.model.PaperSummary
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * [PaperRepository] that serves the Room cache (synced from Appwrite), falling back to the bundled
 * assets on first launch / offline. A background [AppwriteContentSyncer.requestSync] then fills the
 * cache and the Room flow re-emits the authoritative content.
 */
@Singleton
class CachedPaperRepository @Inject constructor(
    private val assetRepository: AssetPaperRepository,
    private val dao: ContentDao,
    private val syncer: AppwriteContentSyncer,
    private val dispatchers: DispatcherProvider,
) : PaperRepository {

    override fun observePaper(paperId: String): Flow<Paper?> {
        val cached = combine(
            dao.observePaper(paperId),
            dao.observeQuestions(paperId),
        ) { paper, questions ->
            if (paper != null && questions.isNotEmpty()) paper.toDomain(questions) else null
        }
        return cached
            .map { paper ->
                if (paper != null) {
                    paper
                } else {
                    val fallback = assetRepository.getPaper(paperId)
                    if (fallback != null) syncer.requestSync()
                    fallback
                }
            }
            .flowOn(dispatchers.io)
    }

    override suspend fun getPaper(paperId: String): Paper? = withContext(dispatchers.io) {
        val paper = dao.getPaper(paperId)
        val questions = dao.listQuestions(paperId)
        if (paper != null && questions.isNotEmpty()) {
            paper.toDomain(questions)
        } else {
            assetRepository.getPaper(paperId).also { syncer.requestSync() }
        }
    }

    override suspend fun listPapers(): List<PaperSummary> = withContext(dispatchers.io) {
        val cached = dao.listPapers()
        if (cached.isNotEmpty()) {
            cached.map { it.toSummary() }
        } else {
            assetRepository.listPapers().also { syncer.requestSync() }
        }
    }
}