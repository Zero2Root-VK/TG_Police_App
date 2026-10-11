package com.tgp.mockexam.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.tgp.mockexam.core.database.model.CachedPaperEntity
import com.tgp.mockexam.core.database.model.CachedQuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContentDao {

    @Upsert
    suspend fun upsertPapers(papers: List<CachedPaperEntity>)

    @Upsert
    suspend fun upsertQuestions(questions: List<CachedQuestionEntity>)

    @Query("SELECT * FROM cached_papers ORDER BY track, stage, year")
    suspend fun listPapers(): List<CachedPaperEntity>

    @Query("SELECT * FROM cached_papers WHERE id = :id")
    fun observePaper(id: String): Flow<CachedPaperEntity?>

    @Query("SELECT * FROM cached_papers WHERE id = :id")
    suspend fun getPaper(id: String): CachedPaperEntity?

    @Query("SELECT * FROM cached_questions WHERE paperId = :paperId ORDER BY n")
    fun observeQuestions(paperId: String): Flow<List<CachedQuestionEntity>>

    @Query("SELECT * FROM cached_questions WHERE paperId = :paperId ORDER BY n")
    suspend fun listQuestions(paperId: String): List<CachedQuestionEntity>

    @Query("SELECT COUNT(*) FROM cached_questions WHERE paperId = :paperId")
    suspend fun countQuestions(paperId: String): Int

    @Query("SELECT contentHash FROM cached_papers WHERE id = :id")
    suspend fun contentHash(id: String): String?

    @Query("SELECT COUNT(*) FROM cached_papers")
    suspend fun paperCount(): Int
}