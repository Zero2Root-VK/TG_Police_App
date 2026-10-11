package com.tgp.mockexam.core.database

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.tgp.mockexam.core.database.dao.ContentDao
import com.tgp.mockexam.core.database.dao.ContentDao_Impl
import com.tgp.mockexam.core.database.dao.ExamSessionDao
import com.tgp.mockexam.core.database.dao.ExamSessionDao_Impl
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class TgpDatabase_Impl : TgpDatabase() {
  private val _examSessionDao: Lazy<ExamSessionDao> = lazy {
    ExamSessionDao_Impl(this)
  }

  private val _contentDao: Lazy<ContentDao> = lazy {
    ContentDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(3, "d4efa59cf4fc143288a0f597121fdd2d", "43920761e987808811e4275cdd1fbfcc") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `exam_sessions` (`paperId` TEXT NOT NULL, `status` TEXT NOT NULL, `startedAtEpochMs` INTEGER NOT NULL, `startedAtRealtimeMs` INTEGER NOT NULL, `durationMins` INTEGER NOT NULL, `pausedAccumulatedMs` INTEGER NOT NULL, `currentQuestionNumber` INTEGER NOT NULL, `submittedAtEpochMs` INTEGER, `language` TEXT NOT NULL, PRIMARY KEY(`paperId`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `attempt_answers` (`paperId` TEXT NOT NULL, `questionNumber` INTEGER NOT NULL, `selectedOptionIndex` INTEGER, `markedForReview` INTEGER NOT NULL, `visited` INTEGER NOT NULL, `answeredAtEpochMs` INTEGER, PRIMARY KEY(`paperId`, `questionNumber`), FOREIGN KEY(`paperId`) REFERENCES `exam_sessions`(`paperId`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_attempt_answers_paperId` ON `attempt_answers` (`paperId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `cached_papers` (`id` TEXT NOT NULL, `track` TEXT NOT NULL, `stage` TEXT NOT NULL, `year` INTEGER NOT NULL, `paperNo` INTEGER NOT NULL, `series` TEXT NOT NULL, `titleEn` TEXT NOT NULL, `titleTe` TEXT NOT NULL, `marks` INTEGER NOT NULL, `durationMins` INTEGER NOT NULL, `positiveMarks` REAL NOT NULL, `negativeMarks` REAL NOT NULL, `totalQuestions` INTEGER NOT NULL, `reviewPending` INTEGER NOT NULL, `isFree` INTEGER NOT NULL, `contentHash` TEXT NOT NULL, `updatedAt` TEXT NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `cached_questions` (`paperId` TEXT NOT NULL, `n` INTEGER NOT NULL, `promptEn` TEXT NOT NULL, `promptTe` TEXT NOT NULL, `optionsEn` TEXT NOT NULL, `optionsTe` TEXT NOT NULL, `answer` INTEGER NOT NULL, `solutionEn` TEXT NOT NULL, `solutionTe` TEXT NOT NULL, `ruleEn` TEXT NOT NULL, `ruleTe` TEXT NOT NULL, `topic` TEXT NOT NULL, `conceptKey` TEXT NOT NULL, `version` INTEGER NOT NULL, `status` TEXT NOT NULL, `updatedAt` TEXT NOT NULL, PRIMARY KEY(`paperId`, `n`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'd4efa59cf4fc143288a0f597121fdd2d')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `exam_sessions`")
        connection.execSQL("DROP TABLE IF EXISTS `attempt_answers`")
        connection.execSQL("DROP TABLE IF EXISTS `cached_papers`")
        connection.execSQL("DROP TABLE IF EXISTS `cached_questions`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        connection.execSQL("PRAGMA foreign_keys = ON")
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsExamSessions: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsExamSessions.put("paperId", TableInfo.Column("paperId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsExamSessions.put("status", TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsExamSessions.put("startedAtEpochMs", TableInfo.Column("startedAtEpochMs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsExamSessions.put("startedAtRealtimeMs", TableInfo.Column("startedAtRealtimeMs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsExamSessions.put("durationMins", TableInfo.Column("durationMins", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsExamSessions.put("pausedAccumulatedMs", TableInfo.Column("pausedAccumulatedMs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsExamSessions.put("currentQuestionNumber", TableInfo.Column("currentQuestionNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsExamSessions.put("submittedAtEpochMs", TableInfo.Column("submittedAtEpochMs", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsExamSessions.put("language", TableInfo.Column("language", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysExamSessions: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesExamSessions: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoExamSessions: TableInfo = TableInfo("exam_sessions", _columnsExamSessions, _foreignKeysExamSessions, _indicesExamSessions)
        val _existingExamSessions: TableInfo = read(connection, "exam_sessions")
        if (!_infoExamSessions.equals(_existingExamSessions)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |exam_sessions(com.tgp.mockexam.core.database.model.ExamSessionEntity).
              | Expected:
              |""".trimMargin() + _infoExamSessions + """
              |
              | Found:
              |""".trimMargin() + _existingExamSessions)
        }
        val _columnsAttemptAnswers: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsAttemptAnswers.put("paperId", TableInfo.Column("paperId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAttemptAnswers.put("questionNumber", TableInfo.Column("questionNumber", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAttemptAnswers.put("selectedOptionIndex", TableInfo.Column("selectedOptionIndex", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAttemptAnswers.put("markedForReview", TableInfo.Column("markedForReview", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAttemptAnswers.put("visited", TableInfo.Column("visited", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAttemptAnswers.put("answeredAtEpochMs", TableInfo.Column("answeredAtEpochMs", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysAttemptAnswers: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysAttemptAnswers.add(TableInfo.ForeignKey("exam_sessions", "CASCADE", "NO ACTION", listOf("paperId"), listOf("paperId")))
        val _indicesAttemptAnswers: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesAttemptAnswers.add(TableInfo.Index("index_attempt_answers_paperId", false, listOf("paperId"), listOf("ASC")))
        val _infoAttemptAnswers: TableInfo = TableInfo("attempt_answers", _columnsAttemptAnswers, _foreignKeysAttemptAnswers, _indicesAttemptAnswers)
        val _existingAttemptAnswers: TableInfo = read(connection, "attempt_answers")
        if (!_infoAttemptAnswers.equals(_existingAttemptAnswers)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |attempt_answers(com.tgp.mockexam.core.database.model.AttemptAnswerEntity).
              | Expected:
              |""".trimMargin() + _infoAttemptAnswers + """
              |
              | Found:
              |""".trimMargin() + _existingAttemptAnswers)
        }
        val _columnsCachedPapers: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsCachedPapers.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedPapers.put("track", TableInfo.Column("track", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedPapers.put("stage", TableInfo.Column("stage", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedPapers.put("year", TableInfo.Column("year", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedPapers.put("paperNo", TableInfo.Column("paperNo", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedPapers.put("series", TableInfo.Column("series", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedPapers.put("titleEn", TableInfo.Column("titleEn", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedPapers.put("titleTe", TableInfo.Column("titleTe", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedPapers.put("marks", TableInfo.Column("marks", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedPapers.put("durationMins", TableInfo.Column("durationMins", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedPapers.put("positiveMarks", TableInfo.Column("positiveMarks", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedPapers.put("negativeMarks", TableInfo.Column("negativeMarks", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedPapers.put("totalQuestions", TableInfo.Column("totalQuestions", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedPapers.put("reviewPending", TableInfo.Column("reviewPending", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedPapers.put("isFree", TableInfo.Column("isFree", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedPapers.put("contentHash", TableInfo.Column("contentHash", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedPapers.put("updatedAt", TableInfo.Column("updatedAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysCachedPapers: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesCachedPapers: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoCachedPapers: TableInfo = TableInfo("cached_papers", _columnsCachedPapers, _foreignKeysCachedPapers, _indicesCachedPapers)
        val _existingCachedPapers: TableInfo = read(connection, "cached_papers")
        if (!_infoCachedPapers.equals(_existingCachedPapers)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |cached_papers(com.tgp.mockexam.core.database.model.CachedPaperEntity).
              | Expected:
              |""".trimMargin() + _infoCachedPapers + """
              |
              | Found:
              |""".trimMargin() + _existingCachedPapers)
        }
        val _columnsCachedQuestions: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsCachedQuestions.put("paperId", TableInfo.Column("paperId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedQuestions.put("n", TableInfo.Column("n", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedQuestions.put("promptEn", TableInfo.Column("promptEn", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedQuestions.put("promptTe", TableInfo.Column("promptTe", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedQuestions.put("optionsEn", TableInfo.Column("optionsEn", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedQuestions.put("optionsTe", TableInfo.Column("optionsTe", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedQuestions.put("answer", TableInfo.Column("answer", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedQuestions.put("solutionEn", TableInfo.Column("solutionEn", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedQuestions.put("solutionTe", TableInfo.Column("solutionTe", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedQuestions.put("ruleEn", TableInfo.Column("ruleEn", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedQuestions.put("ruleTe", TableInfo.Column("ruleTe", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedQuestions.put("topic", TableInfo.Column("topic", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedQuestions.put("conceptKey", TableInfo.Column("conceptKey", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedQuestions.put("version", TableInfo.Column("version", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedQuestions.put("status", TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCachedQuestions.put("updatedAt", TableInfo.Column("updatedAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysCachedQuestions: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesCachedQuestions: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoCachedQuestions: TableInfo = TableInfo("cached_questions", _columnsCachedQuestions, _foreignKeysCachedQuestions, _indicesCachedQuestions)
        val _existingCachedQuestions: TableInfo = read(connection, "cached_questions")
        if (!_infoCachedQuestions.equals(_existingCachedQuestions)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |cached_questions(com.tgp.mockexam.core.database.model.CachedQuestionEntity).
              | Expected:
              |""".trimMargin() + _infoCachedQuestions + """
              |
              | Found:
              |""".trimMargin() + _existingCachedQuestions)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "exam_sessions", "attempt_answers", "cached_papers", "cached_questions")
  }

  public override fun clearAllTables() {
    super.performClear(true, "exam_sessions", "attempt_answers", "cached_papers", "cached_questions")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(ExamSessionDao::class, ExamSessionDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(ContentDao::class, ContentDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun examSessionDao(): ExamSessionDao = _examSessionDao.value

  public override fun contentDao(): ContentDao = _contentDao.value
}
