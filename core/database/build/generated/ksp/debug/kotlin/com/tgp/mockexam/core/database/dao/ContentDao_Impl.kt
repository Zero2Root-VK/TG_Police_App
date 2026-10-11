package com.tgp.mockexam.core.database.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.EntityUpsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.tgp.mockexam.core.database.model.CachedPaperEntity
import com.tgp.mockexam.core.database.model.CachedQuestionEntity
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
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
public class ContentDao_Impl(
  __db: RoomDatabase,
) : ContentDao {
  private val __db: RoomDatabase

  private val __upsertAdapterOfCachedPaperEntity: EntityUpsertAdapter<CachedPaperEntity>

  private val __upsertAdapterOfCachedQuestionEntity: EntityUpsertAdapter<CachedQuestionEntity>
  init {
    this.__db = __db
    this.__upsertAdapterOfCachedPaperEntity = EntityUpsertAdapter<CachedPaperEntity>(object : EntityInsertAdapter<CachedPaperEntity>() {
      protected override fun createQuery(): String = "INSERT INTO `cached_papers` (`id`,`track`,`stage`,`year`,`paperNo`,`series`,`titleEn`,`titleTe`,`marks`,`durationMins`,`positiveMarks`,`negativeMarks`,`totalQuestions`,`reviewPending`,`isFree`,`contentHash`,`updatedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: CachedPaperEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.track)
        statement.bindText(3, entity.stage)
        statement.bindLong(4, entity.year.toLong())
        statement.bindLong(5, entity.paperNo.toLong())
        statement.bindText(6, entity.series)
        statement.bindText(7, entity.titleEn)
        statement.bindText(8, entity.titleTe)
        statement.bindLong(9, entity.marks.toLong())
        statement.bindLong(10, entity.durationMins.toLong())
        statement.bindDouble(11, entity.positiveMarks)
        statement.bindDouble(12, entity.negativeMarks)
        statement.bindLong(13, entity.totalQuestions.toLong())
        val _tmp: Int = if (entity.reviewPending) 1 else 0
        statement.bindLong(14, _tmp.toLong())
        val _tmp_1: Int = if (entity.isFree) 1 else 0
        statement.bindLong(15, _tmp_1.toLong())
        statement.bindText(16, entity.contentHash)
        statement.bindText(17, entity.updatedAt)
      }
    }, object : EntityDeleteOrUpdateAdapter<CachedPaperEntity>() {
      protected override fun createQuery(): String = "UPDATE `cached_papers` SET `id` = ?,`track` = ?,`stage` = ?,`year` = ?,`paperNo` = ?,`series` = ?,`titleEn` = ?,`titleTe` = ?,`marks` = ?,`durationMins` = ?,`positiveMarks` = ?,`negativeMarks` = ?,`totalQuestions` = ?,`reviewPending` = ?,`isFree` = ?,`contentHash` = ?,`updatedAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: CachedPaperEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.track)
        statement.bindText(3, entity.stage)
        statement.bindLong(4, entity.year.toLong())
        statement.bindLong(5, entity.paperNo.toLong())
        statement.bindText(6, entity.series)
        statement.bindText(7, entity.titleEn)
        statement.bindText(8, entity.titleTe)
        statement.bindLong(9, entity.marks.toLong())
        statement.bindLong(10, entity.durationMins.toLong())
        statement.bindDouble(11, entity.positiveMarks)
        statement.bindDouble(12, entity.negativeMarks)
        statement.bindLong(13, entity.totalQuestions.toLong())
        val _tmp: Int = if (entity.reviewPending) 1 else 0
        statement.bindLong(14, _tmp.toLong())
        val _tmp_1: Int = if (entity.isFree) 1 else 0
        statement.bindLong(15, _tmp_1.toLong())
        statement.bindText(16, entity.contentHash)
        statement.bindText(17, entity.updatedAt)
        statement.bindText(18, entity.id)
      }
    })
    this.__upsertAdapterOfCachedQuestionEntity = EntityUpsertAdapter<CachedQuestionEntity>(object : EntityInsertAdapter<CachedQuestionEntity>() {
      protected override fun createQuery(): String = "INSERT INTO `cached_questions` (`paperId`,`n`,`promptEn`,`promptTe`,`optionsEn`,`optionsTe`,`answer`,`solutionEn`,`solutionTe`,`ruleEn`,`ruleTe`,`topic`,`conceptKey`,`version`,`status`,`updatedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: CachedQuestionEntity) {
        statement.bindText(1, entity.paperId)
        statement.bindLong(2, entity.n.toLong())
        statement.bindText(3, entity.promptEn)
        statement.bindText(4, entity.promptTe)
        statement.bindText(5, entity.optionsEn)
        statement.bindText(6, entity.optionsTe)
        statement.bindLong(7, entity.answer.toLong())
        statement.bindText(8, entity.solutionEn)
        statement.bindText(9, entity.solutionTe)
        statement.bindText(10, entity.ruleEn)
        statement.bindText(11, entity.ruleTe)
        statement.bindText(12, entity.topic)
        statement.bindText(13, entity.conceptKey)
        statement.bindLong(14, entity.version.toLong())
        statement.bindText(15, entity.status)
        statement.bindText(16, entity.updatedAt)
      }
    }, object : EntityDeleteOrUpdateAdapter<CachedQuestionEntity>() {
      protected override fun createQuery(): String = "UPDATE `cached_questions` SET `paperId` = ?,`n` = ?,`promptEn` = ?,`promptTe` = ?,`optionsEn` = ?,`optionsTe` = ?,`answer` = ?,`solutionEn` = ?,`solutionTe` = ?,`ruleEn` = ?,`ruleTe` = ?,`topic` = ?,`conceptKey` = ?,`version` = ?,`status` = ?,`updatedAt` = ? WHERE `paperId` = ? AND `n` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: CachedQuestionEntity) {
        statement.bindText(1, entity.paperId)
        statement.bindLong(2, entity.n.toLong())
        statement.bindText(3, entity.promptEn)
        statement.bindText(4, entity.promptTe)
        statement.bindText(5, entity.optionsEn)
        statement.bindText(6, entity.optionsTe)
        statement.bindLong(7, entity.answer.toLong())
        statement.bindText(8, entity.solutionEn)
        statement.bindText(9, entity.solutionTe)
        statement.bindText(10, entity.ruleEn)
        statement.bindText(11, entity.ruleTe)
        statement.bindText(12, entity.topic)
        statement.bindText(13, entity.conceptKey)
        statement.bindLong(14, entity.version.toLong())
        statement.bindText(15, entity.status)
        statement.bindText(16, entity.updatedAt)
        statement.bindText(17, entity.paperId)
        statement.bindLong(18, entity.n.toLong())
      }
    })
  }

  public override suspend fun upsertPapers(papers: List<CachedPaperEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __upsertAdapterOfCachedPaperEntity.upsert(_connection, papers)
  }

  public override suspend fun upsertQuestions(questions: List<CachedQuestionEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __upsertAdapterOfCachedQuestionEntity.upsert(_connection, questions)
  }

  public override suspend fun listPapers(): List<CachedPaperEntity> {
    val _sql: String = "SELECT * FROM cached_papers ORDER BY track, stage, year"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTrack: Int = getColumnIndexOrThrow(_stmt, "track")
        val _columnIndexOfStage: Int = getColumnIndexOrThrow(_stmt, "stage")
        val _columnIndexOfYear: Int = getColumnIndexOrThrow(_stmt, "year")
        val _columnIndexOfPaperNo: Int = getColumnIndexOrThrow(_stmt, "paperNo")
        val _columnIndexOfSeries: Int = getColumnIndexOrThrow(_stmt, "series")
        val _columnIndexOfTitleEn: Int = getColumnIndexOrThrow(_stmt, "titleEn")
        val _columnIndexOfTitleTe: Int = getColumnIndexOrThrow(_stmt, "titleTe")
        val _columnIndexOfMarks: Int = getColumnIndexOrThrow(_stmt, "marks")
        val _columnIndexOfDurationMins: Int = getColumnIndexOrThrow(_stmt, "durationMins")
        val _columnIndexOfPositiveMarks: Int = getColumnIndexOrThrow(_stmt, "positiveMarks")
        val _columnIndexOfNegativeMarks: Int = getColumnIndexOrThrow(_stmt, "negativeMarks")
        val _columnIndexOfTotalQuestions: Int = getColumnIndexOrThrow(_stmt, "totalQuestions")
        val _columnIndexOfReviewPending: Int = getColumnIndexOrThrow(_stmt, "reviewPending")
        val _columnIndexOfIsFree: Int = getColumnIndexOrThrow(_stmt, "isFree")
        val _columnIndexOfContentHash: Int = getColumnIndexOrThrow(_stmt, "contentHash")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _result: MutableList<CachedPaperEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: CachedPaperEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTrack: String
          _tmpTrack = _stmt.getText(_columnIndexOfTrack)
          val _tmpStage: String
          _tmpStage = _stmt.getText(_columnIndexOfStage)
          val _tmpYear: Int
          _tmpYear = _stmt.getLong(_columnIndexOfYear).toInt()
          val _tmpPaperNo: Int
          _tmpPaperNo = _stmt.getLong(_columnIndexOfPaperNo).toInt()
          val _tmpSeries: String
          _tmpSeries = _stmt.getText(_columnIndexOfSeries)
          val _tmpTitleEn: String
          _tmpTitleEn = _stmt.getText(_columnIndexOfTitleEn)
          val _tmpTitleTe: String
          _tmpTitleTe = _stmt.getText(_columnIndexOfTitleTe)
          val _tmpMarks: Int
          _tmpMarks = _stmt.getLong(_columnIndexOfMarks).toInt()
          val _tmpDurationMins: Int
          _tmpDurationMins = _stmt.getLong(_columnIndexOfDurationMins).toInt()
          val _tmpPositiveMarks: Double
          _tmpPositiveMarks = _stmt.getDouble(_columnIndexOfPositiveMarks)
          val _tmpNegativeMarks: Double
          _tmpNegativeMarks = _stmt.getDouble(_columnIndexOfNegativeMarks)
          val _tmpTotalQuestions: Int
          _tmpTotalQuestions = _stmt.getLong(_columnIndexOfTotalQuestions).toInt()
          val _tmpReviewPending: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfReviewPending).toInt()
          _tmpReviewPending = _tmp != 0
          val _tmpIsFree: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsFree).toInt()
          _tmpIsFree = _tmp_1 != 0
          val _tmpContentHash: String
          _tmpContentHash = _stmt.getText(_columnIndexOfContentHash)
          val _tmpUpdatedAt: String
          _tmpUpdatedAt = _stmt.getText(_columnIndexOfUpdatedAt)
          _item = CachedPaperEntity(_tmpId,_tmpTrack,_tmpStage,_tmpYear,_tmpPaperNo,_tmpSeries,_tmpTitleEn,_tmpTitleTe,_tmpMarks,_tmpDurationMins,_tmpPositiveMarks,_tmpNegativeMarks,_tmpTotalQuestions,_tmpReviewPending,_tmpIsFree,_tmpContentHash,_tmpUpdatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observePaper(id: String): Flow<CachedPaperEntity?> {
    val _sql: String = "SELECT * FROM cached_papers WHERE id = ?"
    return createFlow(__db, false, arrayOf("cached_papers")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTrack: Int = getColumnIndexOrThrow(_stmt, "track")
        val _columnIndexOfStage: Int = getColumnIndexOrThrow(_stmt, "stage")
        val _columnIndexOfYear: Int = getColumnIndexOrThrow(_stmt, "year")
        val _columnIndexOfPaperNo: Int = getColumnIndexOrThrow(_stmt, "paperNo")
        val _columnIndexOfSeries: Int = getColumnIndexOrThrow(_stmt, "series")
        val _columnIndexOfTitleEn: Int = getColumnIndexOrThrow(_stmt, "titleEn")
        val _columnIndexOfTitleTe: Int = getColumnIndexOrThrow(_stmt, "titleTe")
        val _columnIndexOfMarks: Int = getColumnIndexOrThrow(_stmt, "marks")
        val _columnIndexOfDurationMins: Int = getColumnIndexOrThrow(_stmt, "durationMins")
        val _columnIndexOfPositiveMarks: Int = getColumnIndexOrThrow(_stmt, "positiveMarks")
        val _columnIndexOfNegativeMarks: Int = getColumnIndexOrThrow(_stmt, "negativeMarks")
        val _columnIndexOfTotalQuestions: Int = getColumnIndexOrThrow(_stmt, "totalQuestions")
        val _columnIndexOfReviewPending: Int = getColumnIndexOrThrow(_stmt, "reviewPending")
        val _columnIndexOfIsFree: Int = getColumnIndexOrThrow(_stmt, "isFree")
        val _columnIndexOfContentHash: Int = getColumnIndexOrThrow(_stmt, "contentHash")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _result: CachedPaperEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTrack: String
          _tmpTrack = _stmt.getText(_columnIndexOfTrack)
          val _tmpStage: String
          _tmpStage = _stmt.getText(_columnIndexOfStage)
          val _tmpYear: Int
          _tmpYear = _stmt.getLong(_columnIndexOfYear).toInt()
          val _tmpPaperNo: Int
          _tmpPaperNo = _stmt.getLong(_columnIndexOfPaperNo).toInt()
          val _tmpSeries: String
          _tmpSeries = _stmt.getText(_columnIndexOfSeries)
          val _tmpTitleEn: String
          _tmpTitleEn = _stmt.getText(_columnIndexOfTitleEn)
          val _tmpTitleTe: String
          _tmpTitleTe = _stmt.getText(_columnIndexOfTitleTe)
          val _tmpMarks: Int
          _tmpMarks = _stmt.getLong(_columnIndexOfMarks).toInt()
          val _tmpDurationMins: Int
          _tmpDurationMins = _stmt.getLong(_columnIndexOfDurationMins).toInt()
          val _tmpPositiveMarks: Double
          _tmpPositiveMarks = _stmt.getDouble(_columnIndexOfPositiveMarks)
          val _tmpNegativeMarks: Double
          _tmpNegativeMarks = _stmt.getDouble(_columnIndexOfNegativeMarks)
          val _tmpTotalQuestions: Int
          _tmpTotalQuestions = _stmt.getLong(_columnIndexOfTotalQuestions).toInt()
          val _tmpReviewPending: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfReviewPending).toInt()
          _tmpReviewPending = _tmp != 0
          val _tmpIsFree: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsFree).toInt()
          _tmpIsFree = _tmp_1 != 0
          val _tmpContentHash: String
          _tmpContentHash = _stmt.getText(_columnIndexOfContentHash)
          val _tmpUpdatedAt: String
          _tmpUpdatedAt = _stmt.getText(_columnIndexOfUpdatedAt)
          _result = CachedPaperEntity(_tmpId,_tmpTrack,_tmpStage,_tmpYear,_tmpPaperNo,_tmpSeries,_tmpTitleEn,_tmpTitleTe,_tmpMarks,_tmpDurationMins,_tmpPositiveMarks,_tmpNegativeMarks,_tmpTotalQuestions,_tmpReviewPending,_tmpIsFree,_tmpContentHash,_tmpUpdatedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getPaper(id: String): CachedPaperEntity? {
    val _sql: String = "SELECT * FROM cached_papers WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTrack: Int = getColumnIndexOrThrow(_stmt, "track")
        val _columnIndexOfStage: Int = getColumnIndexOrThrow(_stmt, "stage")
        val _columnIndexOfYear: Int = getColumnIndexOrThrow(_stmt, "year")
        val _columnIndexOfPaperNo: Int = getColumnIndexOrThrow(_stmt, "paperNo")
        val _columnIndexOfSeries: Int = getColumnIndexOrThrow(_stmt, "series")
        val _columnIndexOfTitleEn: Int = getColumnIndexOrThrow(_stmt, "titleEn")
        val _columnIndexOfTitleTe: Int = getColumnIndexOrThrow(_stmt, "titleTe")
        val _columnIndexOfMarks: Int = getColumnIndexOrThrow(_stmt, "marks")
        val _columnIndexOfDurationMins: Int = getColumnIndexOrThrow(_stmt, "durationMins")
        val _columnIndexOfPositiveMarks: Int = getColumnIndexOrThrow(_stmt, "positiveMarks")
        val _columnIndexOfNegativeMarks: Int = getColumnIndexOrThrow(_stmt, "negativeMarks")
        val _columnIndexOfTotalQuestions: Int = getColumnIndexOrThrow(_stmt, "totalQuestions")
        val _columnIndexOfReviewPending: Int = getColumnIndexOrThrow(_stmt, "reviewPending")
        val _columnIndexOfIsFree: Int = getColumnIndexOrThrow(_stmt, "isFree")
        val _columnIndexOfContentHash: Int = getColumnIndexOrThrow(_stmt, "contentHash")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _result: CachedPaperEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTrack: String
          _tmpTrack = _stmt.getText(_columnIndexOfTrack)
          val _tmpStage: String
          _tmpStage = _stmt.getText(_columnIndexOfStage)
          val _tmpYear: Int
          _tmpYear = _stmt.getLong(_columnIndexOfYear).toInt()
          val _tmpPaperNo: Int
          _tmpPaperNo = _stmt.getLong(_columnIndexOfPaperNo).toInt()
          val _tmpSeries: String
          _tmpSeries = _stmt.getText(_columnIndexOfSeries)
          val _tmpTitleEn: String
          _tmpTitleEn = _stmt.getText(_columnIndexOfTitleEn)
          val _tmpTitleTe: String
          _tmpTitleTe = _stmt.getText(_columnIndexOfTitleTe)
          val _tmpMarks: Int
          _tmpMarks = _stmt.getLong(_columnIndexOfMarks).toInt()
          val _tmpDurationMins: Int
          _tmpDurationMins = _stmt.getLong(_columnIndexOfDurationMins).toInt()
          val _tmpPositiveMarks: Double
          _tmpPositiveMarks = _stmt.getDouble(_columnIndexOfPositiveMarks)
          val _tmpNegativeMarks: Double
          _tmpNegativeMarks = _stmt.getDouble(_columnIndexOfNegativeMarks)
          val _tmpTotalQuestions: Int
          _tmpTotalQuestions = _stmt.getLong(_columnIndexOfTotalQuestions).toInt()
          val _tmpReviewPending: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfReviewPending).toInt()
          _tmpReviewPending = _tmp != 0
          val _tmpIsFree: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsFree).toInt()
          _tmpIsFree = _tmp_1 != 0
          val _tmpContentHash: String
          _tmpContentHash = _stmt.getText(_columnIndexOfContentHash)
          val _tmpUpdatedAt: String
          _tmpUpdatedAt = _stmt.getText(_columnIndexOfUpdatedAt)
          _result = CachedPaperEntity(_tmpId,_tmpTrack,_tmpStage,_tmpYear,_tmpPaperNo,_tmpSeries,_tmpTitleEn,_tmpTitleTe,_tmpMarks,_tmpDurationMins,_tmpPositiveMarks,_tmpNegativeMarks,_tmpTotalQuestions,_tmpReviewPending,_tmpIsFree,_tmpContentHash,_tmpUpdatedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeQuestions(paperId: String): Flow<List<CachedQuestionEntity>> {
    val _sql: String = "SELECT * FROM cached_questions WHERE paperId = ? ORDER BY n"
    return createFlow(__db, false, arrayOf("cached_questions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, paperId)
        val _columnIndexOfPaperId: Int = getColumnIndexOrThrow(_stmt, "paperId")
        val _columnIndexOfN: Int = getColumnIndexOrThrow(_stmt, "n")
        val _columnIndexOfPromptEn: Int = getColumnIndexOrThrow(_stmt, "promptEn")
        val _columnIndexOfPromptTe: Int = getColumnIndexOrThrow(_stmt, "promptTe")
        val _columnIndexOfOptionsEn: Int = getColumnIndexOrThrow(_stmt, "optionsEn")
        val _columnIndexOfOptionsTe: Int = getColumnIndexOrThrow(_stmt, "optionsTe")
        val _columnIndexOfAnswer: Int = getColumnIndexOrThrow(_stmt, "answer")
        val _columnIndexOfSolutionEn: Int = getColumnIndexOrThrow(_stmt, "solutionEn")
        val _columnIndexOfSolutionTe: Int = getColumnIndexOrThrow(_stmt, "solutionTe")
        val _columnIndexOfRuleEn: Int = getColumnIndexOrThrow(_stmt, "ruleEn")
        val _columnIndexOfRuleTe: Int = getColumnIndexOrThrow(_stmt, "ruleTe")
        val _columnIndexOfTopic: Int = getColumnIndexOrThrow(_stmt, "topic")
        val _columnIndexOfConceptKey: Int = getColumnIndexOrThrow(_stmt, "conceptKey")
        val _columnIndexOfVersion: Int = getColumnIndexOrThrow(_stmt, "version")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _result: MutableList<CachedQuestionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: CachedQuestionEntity
          val _tmpPaperId: String
          _tmpPaperId = _stmt.getText(_columnIndexOfPaperId)
          val _tmpN: Int
          _tmpN = _stmt.getLong(_columnIndexOfN).toInt()
          val _tmpPromptEn: String
          _tmpPromptEn = _stmt.getText(_columnIndexOfPromptEn)
          val _tmpPromptTe: String
          _tmpPromptTe = _stmt.getText(_columnIndexOfPromptTe)
          val _tmpOptionsEn: String
          _tmpOptionsEn = _stmt.getText(_columnIndexOfOptionsEn)
          val _tmpOptionsTe: String
          _tmpOptionsTe = _stmt.getText(_columnIndexOfOptionsTe)
          val _tmpAnswer: Int
          _tmpAnswer = _stmt.getLong(_columnIndexOfAnswer).toInt()
          val _tmpSolutionEn: String
          _tmpSolutionEn = _stmt.getText(_columnIndexOfSolutionEn)
          val _tmpSolutionTe: String
          _tmpSolutionTe = _stmt.getText(_columnIndexOfSolutionTe)
          val _tmpRuleEn: String
          _tmpRuleEn = _stmt.getText(_columnIndexOfRuleEn)
          val _tmpRuleTe: String
          _tmpRuleTe = _stmt.getText(_columnIndexOfRuleTe)
          val _tmpTopic: String
          _tmpTopic = _stmt.getText(_columnIndexOfTopic)
          val _tmpConceptKey: String
          _tmpConceptKey = _stmt.getText(_columnIndexOfConceptKey)
          val _tmpVersion: Int
          _tmpVersion = _stmt.getLong(_columnIndexOfVersion).toInt()
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          val _tmpUpdatedAt: String
          _tmpUpdatedAt = _stmt.getText(_columnIndexOfUpdatedAt)
          _item = CachedQuestionEntity(_tmpPaperId,_tmpN,_tmpPromptEn,_tmpPromptTe,_tmpOptionsEn,_tmpOptionsTe,_tmpAnswer,_tmpSolutionEn,_tmpSolutionTe,_tmpRuleEn,_tmpRuleTe,_tmpTopic,_tmpConceptKey,_tmpVersion,_tmpStatus,_tmpUpdatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun listQuestions(paperId: String): List<CachedQuestionEntity> {
    val _sql: String = "SELECT * FROM cached_questions WHERE paperId = ? ORDER BY n"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, paperId)
        val _columnIndexOfPaperId: Int = getColumnIndexOrThrow(_stmt, "paperId")
        val _columnIndexOfN: Int = getColumnIndexOrThrow(_stmt, "n")
        val _columnIndexOfPromptEn: Int = getColumnIndexOrThrow(_stmt, "promptEn")
        val _columnIndexOfPromptTe: Int = getColumnIndexOrThrow(_stmt, "promptTe")
        val _columnIndexOfOptionsEn: Int = getColumnIndexOrThrow(_stmt, "optionsEn")
        val _columnIndexOfOptionsTe: Int = getColumnIndexOrThrow(_stmt, "optionsTe")
        val _columnIndexOfAnswer: Int = getColumnIndexOrThrow(_stmt, "answer")
        val _columnIndexOfSolutionEn: Int = getColumnIndexOrThrow(_stmt, "solutionEn")
        val _columnIndexOfSolutionTe: Int = getColumnIndexOrThrow(_stmt, "solutionTe")
        val _columnIndexOfRuleEn: Int = getColumnIndexOrThrow(_stmt, "ruleEn")
        val _columnIndexOfRuleTe: Int = getColumnIndexOrThrow(_stmt, "ruleTe")
        val _columnIndexOfTopic: Int = getColumnIndexOrThrow(_stmt, "topic")
        val _columnIndexOfConceptKey: Int = getColumnIndexOrThrow(_stmt, "conceptKey")
        val _columnIndexOfVersion: Int = getColumnIndexOrThrow(_stmt, "version")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _result: MutableList<CachedQuestionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: CachedQuestionEntity
          val _tmpPaperId: String
          _tmpPaperId = _stmt.getText(_columnIndexOfPaperId)
          val _tmpN: Int
          _tmpN = _stmt.getLong(_columnIndexOfN).toInt()
          val _tmpPromptEn: String
          _tmpPromptEn = _stmt.getText(_columnIndexOfPromptEn)
          val _tmpPromptTe: String
          _tmpPromptTe = _stmt.getText(_columnIndexOfPromptTe)
          val _tmpOptionsEn: String
          _tmpOptionsEn = _stmt.getText(_columnIndexOfOptionsEn)
          val _tmpOptionsTe: String
          _tmpOptionsTe = _stmt.getText(_columnIndexOfOptionsTe)
          val _tmpAnswer: Int
          _tmpAnswer = _stmt.getLong(_columnIndexOfAnswer).toInt()
          val _tmpSolutionEn: String
          _tmpSolutionEn = _stmt.getText(_columnIndexOfSolutionEn)
          val _tmpSolutionTe: String
          _tmpSolutionTe = _stmt.getText(_columnIndexOfSolutionTe)
          val _tmpRuleEn: String
          _tmpRuleEn = _stmt.getText(_columnIndexOfRuleEn)
          val _tmpRuleTe: String
          _tmpRuleTe = _stmt.getText(_columnIndexOfRuleTe)
          val _tmpTopic: String
          _tmpTopic = _stmt.getText(_columnIndexOfTopic)
          val _tmpConceptKey: String
          _tmpConceptKey = _stmt.getText(_columnIndexOfConceptKey)
          val _tmpVersion: Int
          _tmpVersion = _stmt.getLong(_columnIndexOfVersion).toInt()
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          val _tmpUpdatedAt: String
          _tmpUpdatedAt = _stmt.getText(_columnIndexOfUpdatedAt)
          _item = CachedQuestionEntity(_tmpPaperId,_tmpN,_tmpPromptEn,_tmpPromptTe,_tmpOptionsEn,_tmpOptionsTe,_tmpAnswer,_tmpSolutionEn,_tmpSolutionTe,_tmpRuleEn,_tmpRuleTe,_tmpTopic,_tmpConceptKey,_tmpVersion,_tmpStatus,_tmpUpdatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun countQuestions(paperId: String): Int {
    val _sql: String = "SELECT COUNT(*) FROM cached_questions WHERE paperId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, paperId)
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun contentHash(id: String): String? {
    val _sql: String = "SELECT contentHash FROM cached_papers WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _result: String?
        if (_stmt.step()) {
          if (_stmt.isNull(0)) {
            _result = null
          } else {
            _result = _stmt.getText(0)
          }
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun paperCount(): Int {
    val _sql: String = "SELECT COUNT(*) FROM cached_papers"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
