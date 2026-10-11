package com.tgp.mockexam.core.database.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.EntityUpsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.tgp.mockexam.core.database.model.AttemptAnswerEntity
import com.tgp.mockexam.core.database.model.ExamSessionEntity
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class ExamSessionDao_Impl(
  __db: RoomDatabase,
) : ExamSessionDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfExamSessionEntity: EntityInsertAdapter<ExamSessionEntity>

  private val __deleteAdapterOfAttemptAnswerEntity: EntityDeleteOrUpdateAdapter<AttemptAnswerEntity>

  private val __upsertAdapterOfAttemptAnswerEntity: EntityUpsertAdapter<AttemptAnswerEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfExamSessionEntity = object : EntityInsertAdapter<ExamSessionEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `exam_sessions` (`paperId`,`status`,`startedAtEpochMs`,`startedAtRealtimeMs`,`durationMins`,`pausedAccumulatedMs`,`currentQuestionNumber`,`submittedAtEpochMs`,`language`) VALUES (?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ExamSessionEntity) {
        statement.bindText(1, entity.paperId)
        statement.bindText(2, entity.status)
        statement.bindLong(3, entity.startedAtEpochMs)
        statement.bindLong(4, entity.startedAtRealtimeMs)
        statement.bindLong(5, entity.durationMins.toLong())
        statement.bindLong(6, entity.pausedAccumulatedMs)
        statement.bindLong(7, entity.currentQuestionNumber.toLong())
        val _tmpSubmittedAtEpochMs: Long? = entity.submittedAtEpochMs
        if (_tmpSubmittedAtEpochMs == null) {
          statement.bindNull(8)
        } else {
          statement.bindLong(8, _tmpSubmittedAtEpochMs)
        }
        statement.bindText(9, entity.language)
      }
    }
    this.__deleteAdapterOfAttemptAnswerEntity = object : EntityDeleteOrUpdateAdapter<AttemptAnswerEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `attempt_answers` WHERE `paperId` = ? AND `questionNumber` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: AttemptAnswerEntity) {
        statement.bindText(1, entity.paperId)
        statement.bindLong(2, entity.questionNumber.toLong())
      }
    }
    this.__upsertAdapterOfAttemptAnswerEntity = EntityUpsertAdapter<AttemptAnswerEntity>(object : EntityInsertAdapter<AttemptAnswerEntity>() {
      protected override fun createQuery(): String = "INSERT INTO `attempt_answers` (`paperId`,`questionNumber`,`selectedOptionIndex`,`markedForReview`,`visited`,`answeredAtEpochMs`) VALUES (?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: AttemptAnswerEntity) {
        statement.bindText(1, entity.paperId)
        statement.bindLong(2, entity.questionNumber.toLong())
        val _tmpSelectedOptionIndex: Int? = entity.selectedOptionIndex
        if (_tmpSelectedOptionIndex == null) {
          statement.bindNull(3)
        } else {
          statement.bindLong(3, _tmpSelectedOptionIndex.toLong())
        }
        val _tmp: Int = if (entity.markedForReview) 1 else 0
        statement.bindLong(4, _tmp.toLong())
        val _tmp_1: Int = if (entity.visited) 1 else 0
        statement.bindLong(5, _tmp_1.toLong())
        val _tmpAnsweredAtEpochMs: Long? = entity.answeredAtEpochMs
        if (_tmpAnsweredAtEpochMs == null) {
          statement.bindNull(6)
        } else {
          statement.bindLong(6, _tmpAnsweredAtEpochMs)
        }
      }
    }, object : EntityDeleteOrUpdateAdapter<AttemptAnswerEntity>() {
      protected override fun createQuery(): String = "UPDATE `attempt_answers` SET `paperId` = ?,`questionNumber` = ?,`selectedOptionIndex` = ?,`markedForReview` = ?,`visited` = ?,`answeredAtEpochMs` = ? WHERE `paperId` = ? AND `questionNumber` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: AttemptAnswerEntity) {
        statement.bindText(1, entity.paperId)
        statement.bindLong(2, entity.questionNumber.toLong())
        val _tmpSelectedOptionIndex: Int? = entity.selectedOptionIndex
        if (_tmpSelectedOptionIndex == null) {
          statement.bindNull(3)
        } else {
          statement.bindLong(3, _tmpSelectedOptionIndex.toLong())
        }
        val _tmp: Int = if (entity.markedForReview) 1 else 0
        statement.bindLong(4, _tmp.toLong())
        val _tmp_1: Int = if (entity.visited) 1 else 0
        statement.bindLong(5, _tmp_1.toLong())
        val _tmpAnsweredAtEpochMs: Long? = entity.answeredAtEpochMs
        if (_tmpAnsweredAtEpochMs == null) {
          statement.bindNull(6)
        } else {
          statement.bindLong(6, _tmpAnsweredAtEpochMs)
        }
        statement.bindText(7, entity.paperId)
        statement.bindLong(8, entity.questionNumber.toLong())
      }
    })
  }

  public override suspend fun insertSession(session: ExamSessionEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfExamSessionEntity.insert(_connection, session)
  }

  public override suspend fun deleteAnswers(answers: List<AttemptAnswerEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfAttemptAnswerEntity.handleMultiple(_connection, answers)
  }

  public override suspend fun upsertAnswers(answers: List<AttemptAnswerEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __upsertAdapterOfAttemptAnswerEntity.upsert(_connection, answers)
  }

  public override fun observeSession(paperId: String): Flow<ExamSessionEntity?> {
    val _sql: String = "SELECT * FROM exam_sessions WHERE paperId = ?"
    return createFlow(__db, false, arrayOf("exam_sessions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, paperId)
        val _columnIndexOfPaperId: Int = getColumnIndexOrThrow(_stmt, "paperId")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfStartedAtEpochMs: Int = getColumnIndexOrThrow(_stmt, "startedAtEpochMs")
        val _columnIndexOfStartedAtRealtimeMs: Int = getColumnIndexOrThrow(_stmt, "startedAtRealtimeMs")
        val _columnIndexOfDurationMins: Int = getColumnIndexOrThrow(_stmt, "durationMins")
        val _columnIndexOfPausedAccumulatedMs: Int = getColumnIndexOrThrow(_stmt, "pausedAccumulatedMs")
        val _columnIndexOfCurrentQuestionNumber: Int = getColumnIndexOrThrow(_stmt, "currentQuestionNumber")
        val _columnIndexOfSubmittedAtEpochMs: Int = getColumnIndexOrThrow(_stmt, "submittedAtEpochMs")
        val _columnIndexOfLanguage: Int = getColumnIndexOrThrow(_stmt, "language")
        val _result: ExamSessionEntity?
        if (_stmt.step()) {
          val _tmpPaperId: String
          _tmpPaperId = _stmt.getText(_columnIndexOfPaperId)
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          val _tmpStartedAtEpochMs: Long
          _tmpStartedAtEpochMs = _stmt.getLong(_columnIndexOfStartedAtEpochMs)
          val _tmpStartedAtRealtimeMs: Long
          _tmpStartedAtRealtimeMs = _stmt.getLong(_columnIndexOfStartedAtRealtimeMs)
          val _tmpDurationMins: Int
          _tmpDurationMins = _stmt.getLong(_columnIndexOfDurationMins).toInt()
          val _tmpPausedAccumulatedMs: Long
          _tmpPausedAccumulatedMs = _stmt.getLong(_columnIndexOfPausedAccumulatedMs)
          val _tmpCurrentQuestionNumber: Int
          _tmpCurrentQuestionNumber = _stmt.getLong(_columnIndexOfCurrentQuestionNumber).toInt()
          val _tmpSubmittedAtEpochMs: Long?
          if (_stmt.isNull(_columnIndexOfSubmittedAtEpochMs)) {
            _tmpSubmittedAtEpochMs = null
          } else {
            _tmpSubmittedAtEpochMs = _stmt.getLong(_columnIndexOfSubmittedAtEpochMs)
          }
          val _tmpLanguage: String
          _tmpLanguage = _stmt.getText(_columnIndexOfLanguage)
          _result = ExamSessionEntity(_tmpPaperId,_tmpStatus,_tmpStartedAtEpochMs,_tmpStartedAtRealtimeMs,_tmpDurationMins,_tmpPausedAccumulatedMs,_tmpCurrentQuestionNumber,_tmpSubmittedAtEpochMs,_tmpLanguage)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getSession(paperId: String): ExamSessionEntity? {
    val _sql: String = "SELECT * FROM exam_sessions WHERE paperId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, paperId)
        val _columnIndexOfPaperId: Int = getColumnIndexOrThrow(_stmt, "paperId")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfStartedAtEpochMs: Int = getColumnIndexOrThrow(_stmt, "startedAtEpochMs")
        val _columnIndexOfStartedAtRealtimeMs: Int = getColumnIndexOrThrow(_stmt, "startedAtRealtimeMs")
        val _columnIndexOfDurationMins: Int = getColumnIndexOrThrow(_stmt, "durationMins")
        val _columnIndexOfPausedAccumulatedMs: Int = getColumnIndexOrThrow(_stmt, "pausedAccumulatedMs")
        val _columnIndexOfCurrentQuestionNumber: Int = getColumnIndexOrThrow(_stmt, "currentQuestionNumber")
        val _columnIndexOfSubmittedAtEpochMs: Int = getColumnIndexOrThrow(_stmt, "submittedAtEpochMs")
        val _columnIndexOfLanguage: Int = getColumnIndexOrThrow(_stmt, "language")
        val _result: ExamSessionEntity?
        if (_stmt.step()) {
          val _tmpPaperId: String
          _tmpPaperId = _stmt.getText(_columnIndexOfPaperId)
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          val _tmpStartedAtEpochMs: Long
          _tmpStartedAtEpochMs = _stmt.getLong(_columnIndexOfStartedAtEpochMs)
          val _tmpStartedAtRealtimeMs: Long
          _tmpStartedAtRealtimeMs = _stmt.getLong(_columnIndexOfStartedAtRealtimeMs)
          val _tmpDurationMins: Int
          _tmpDurationMins = _stmt.getLong(_columnIndexOfDurationMins).toInt()
          val _tmpPausedAccumulatedMs: Long
          _tmpPausedAccumulatedMs = _stmt.getLong(_columnIndexOfPausedAccumulatedMs)
          val _tmpCurrentQuestionNumber: Int
          _tmpCurrentQuestionNumber = _stmt.getLong(_columnIndexOfCurrentQuestionNumber).toInt()
          val _tmpSubmittedAtEpochMs: Long?
          if (_stmt.isNull(_columnIndexOfSubmittedAtEpochMs)) {
            _tmpSubmittedAtEpochMs = null
          } else {
            _tmpSubmittedAtEpochMs = _stmt.getLong(_columnIndexOfSubmittedAtEpochMs)
          }
          val _tmpLanguage: String
          _tmpLanguage = _stmt.getText(_columnIndexOfLanguage)
          _result = ExamSessionEntity(_tmpPaperId,_tmpStatus,_tmpStartedAtEpochMs,_tmpStartedAtRealtimeMs,_tmpDurationMins,_tmpPausedAccumulatedMs,_tmpCurrentQuestionNumber,_tmpSubmittedAtEpochMs,_tmpLanguage)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeAnswers(paperId: String): Flow<List<AttemptAnswerEntity>> {
    val _sql: String = "SELECT * FROM attempt_answers WHERE paperId = ?"
    return createFlow(__db, false, arrayOf("attempt_answers")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, paperId)
        val _columnIndexOfPaperId: Int = getColumnIndexOrThrow(_stmt, "paperId")
        val _columnIndexOfQuestionNumber: Int = getColumnIndexOrThrow(_stmt, "questionNumber")
        val _columnIndexOfSelectedOptionIndex: Int = getColumnIndexOrThrow(_stmt, "selectedOptionIndex")
        val _columnIndexOfMarkedForReview: Int = getColumnIndexOrThrow(_stmt, "markedForReview")
        val _columnIndexOfVisited: Int = getColumnIndexOrThrow(_stmt, "visited")
        val _columnIndexOfAnsweredAtEpochMs: Int = getColumnIndexOrThrow(_stmt, "answeredAtEpochMs")
        val _result: MutableList<AttemptAnswerEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: AttemptAnswerEntity
          val _tmpPaperId: String
          _tmpPaperId = _stmt.getText(_columnIndexOfPaperId)
          val _tmpQuestionNumber: Int
          _tmpQuestionNumber = _stmt.getLong(_columnIndexOfQuestionNumber).toInt()
          val _tmpSelectedOptionIndex: Int?
          if (_stmt.isNull(_columnIndexOfSelectedOptionIndex)) {
            _tmpSelectedOptionIndex = null
          } else {
            _tmpSelectedOptionIndex = _stmt.getLong(_columnIndexOfSelectedOptionIndex).toInt()
          }
          val _tmpMarkedForReview: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfMarkedForReview).toInt()
          _tmpMarkedForReview = _tmp != 0
          val _tmpVisited: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfVisited).toInt()
          _tmpVisited = _tmp_1 != 0
          val _tmpAnsweredAtEpochMs: Long?
          if (_stmt.isNull(_columnIndexOfAnsweredAtEpochMs)) {
            _tmpAnsweredAtEpochMs = null
          } else {
            _tmpAnsweredAtEpochMs = _stmt.getLong(_columnIndexOfAnsweredAtEpochMs)
          }
          _item = AttemptAnswerEntity(_tmpPaperId,_tmpQuestionNumber,_tmpSelectedOptionIndex,_tmpMarkedForReview,_tmpVisited,_tmpAnsweredAtEpochMs)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAnswers(paperId: String): List<AttemptAnswerEntity> {
    val _sql: String = "SELECT * FROM attempt_answers WHERE paperId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, paperId)
        val _columnIndexOfPaperId: Int = getColumnIndexOrThrow(_stmt, "paperId")
        val _columnIndexOfQuestionNumber: Int = getColumnIndexOrThrow(_stmt, "questionNumber")
        val _columnIndexOfSelectedOptionIndex: Int = getColumnIndexOrThrow(_stmt, "selectedOptionIndex")
        val _columnIndexOfMarkedForReview: Int = getColumnIndexOrThrow(_stmt, "markedForReview")
        val _columnIndexOfVisited: Int = getColumnIndexOrThrow(_stmt, "visited")
        val _columnIndexOfAnsweredAtEpochMs: Int = getColumnIndexOrThrow(_stmt, "answeredAtEpochMs")
        val _result: MutableList<AttemptAnswerEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: AttemptAnswerEntity
          val _tmpPaperId: String
          _tmpPaperId = _stmt.getText(_columnIndexOfPaperId)
          val _tmpQuestionNumber: Int
          _tmpQuestionNumber = _stmt.getLong(_columnIndexOfQuestionNumber).toInt()
          val _tmpSelectedOptionIndex: Int?
          if (_stmt.isNull(_columnIndexOfSelectedOptionIndex)) {
            _tmpSelectedOptionIndex = null
          } else {
            _tmpSelectedOptionIndex = _stmt.getLong(_columnIndexOfSelectedOptionIndex).toInt()
          }
          val _tmpMarkedForReview: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfMarkedForReview).toInt()
          _tmpMarkedForReview = _tmp != 0
          val _tmpVisited: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfVisited).toInt()
          _tmpVisited = _tmp_1 != 0
          val _tmpAnsweredAtEpochMs: Long?
          if (_stmt.isNull(_columnIndexOfAnsweredAtEpochMs)) {
            _tmpAnsweredAtEpochMs = null
          } else {
            _tmpAnsweredAtEpochMs = _stmt.getLong(_columnIndexOfAnsweredAtEpochMs)
          }
          _item = AttemptAnswerEntity(_tmpPaperId,_tmpQuestionNumber,_tmpSelectedOptionIndex,_tmpMarkedForReview,_tmpVisited,_tmpAnsweredAtEpochMs)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteSession(paperId: String) {
    val _sql: String = "DELETE FROM exam_sessions WHERE paperId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, paperId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateStatus(paperId: String, status: String) {
    val _sql: String = "UPDATE exam_sessions SET status = ? WHERE paperId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, status)
        _argIndex = 2
        _stmt.bindText(_argIndex, paperId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateCurrentQuestion(paperId: String, questionNumber: Int) {
    val _sql: String = "UPDATE exam_sessions SET currentQuestionNumber = ? WHERE paperId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, questionNumber.toLong())
        _argIndex = 2
        _stmt.bindText(_argIndex, paperId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun submit(
    paperId: String,
    status: String,
    submittedAtEpochMs: Long,
  ) {
    val _sql: String = "UPDATE exam_sessions SET status = ?, submittedAtEpochMs = ? WHERE paperId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, status)
        _argIndex = 2
        _stmt.bindLong(_argIndex, submittedAtEpochMs)
        _argIndex = 3
        _stmt.bindText(_argIndex, paperId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
