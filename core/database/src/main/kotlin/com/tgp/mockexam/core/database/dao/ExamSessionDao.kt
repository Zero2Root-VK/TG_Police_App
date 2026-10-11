package com.tgp.mockexam.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.tgp.mockexam.core.database.model.AttemptAnswerEntity
import com.tgp.mockexam.core.database.model.ExamSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamSessionDao {

    @Query("SELECT * FROM exam_sessions WHERE paperId = :paperId")
    fun observeSession(paperId: String): Flow<ExamSessionEntity?>

    @Query("SELECT * FROM exam_sessions WHERE paperId = :paperId")
    suspend fun getSession(paperId: String): ExamSessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: ExamSessionEntity)

    @Query("DELETE FROM exam_sessions WHERE paperId = :paperId")
    suspend fun deleteSession(paperId: String)

    @Query("UPDATE exam_sessions SET status = :status WHERE paperId = :paperId")
    suspend fun updateStatus(paperId: String, status: String)

    @Query("UPDATE exam_sessions SET currentQuestionNumber = :questionNumber WHERE paperId = :paperId")
    suspend fun updateCurrentQuestion(paperId: String, questionNumber: Int)

    @Query(
        "UPDATE exam_sessions SET status = :status, submittedAtEpochMs = :submittedAtEpochMs WHERE paperId = :paperId",
    )
    suspend fun submit(paperId: String, status: String, submittedAtEpochMs: Long)

    @Query("SELECT * FROM attempt_answers WHERE paperId = :paperId")
    fun observeAnswers(paperId: String): Flow<List<AttemptAnswerEntity>>

    @Query("SELECT * FROM attempt_answers WHERE paperId = :paperId")
    suspend fun getAnswers(paperId: String): List<AttemptAnswerEntity>

    @Upsert
    suspend fun upsertAnswers(answers: List<AttemptAnswerEntity>)

    @Delete
    suspend fun deleteAnswers(answers: List<AttemptAnswerEntity>)
}