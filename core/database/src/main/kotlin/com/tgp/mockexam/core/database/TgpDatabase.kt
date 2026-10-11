package com.tgp.mockexam.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tgp.mockexam.core.database.dao.ContentDao
import com.tgp.mockexam.core.database.dao.ExamSessionDao
import com.tgp.mockexam.core.database.model.AttemptAnswerEntity
import com.tgp.mockexam.core.database.model.CachedPaperEntity
import com.tgp.mockexam.core.database.model.CachedQuestionEntity
import com.tgp.mockexam.core.database.model.ExamSessionEntity

@Database(
    entities = [
        ExamSessionEntity::class,
        AttemptAnswerEntity::class,
        CachedPaperEntity::class,
        CachedQuestionEntity::class,
    ],
    version = 3,
    exportSchema = false,
)
abstract class TgpDatabase : RoomDatabase() {
    abstract fun examSessionDao(): ExamSessionDao
    abstract fun contentDao(): ContentDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `cached_papers` (
                        `id` TEXT NOT NULL,
                        `track` TEXT NOT NULL,
                        `stage` TEXT NOT NULL,
                        `year` INTEGER NOT NULL,
                        `paperNo` INTEGER NOT NULL,
                        `series` TEXT NOT NULL,
                        `titleEn` TEXT NOT NULL,
                        `titleTe` TEXT NOT NULL,
                        `marks` INTEGER NOT NULL,
                        `durationMins` INTEGER NOT NULL,
                        `totalQuestions` INTEGER NOT NULL,
                        `reviewPending` INTEGER NOT NULL,
                        `isFree` INTEGER NOT NULL,
                        `contentHash` TEXT NOT NULL,
                        `updatedAt` TEXT NOT NULL,
                        PRIMARY KEY(`id`)
                    )
                    """.trimIndent(),
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `cached_questions` (
                        `paperId` TEXT NOT NULL,
                        `n` INTEGER NOT NULL,
                        `promptEn` TEXT NOT NULL,
                        `promptTe` TEXT NOT NULL,
                        `optionsEn` TEXT NOT NULL,
                        `optionsTe` TEXT NOT NULL,
                        `answer` INTEGER NOT NULL,
                        `solutionEn` TEXT NOT NULL,
                        `solutionTe` TEXT NOT NULL,
                        `ruleEn` TEXT NOT NULL,
                        `ruleTe` TEXT NOT NULL,
                        `topic` TEXT NOT NULL,
                        `conceptKey` TEXT NOT NULL,
                        `version` INTEGER NOT NULL,
                        `status` TEXT NOT NULL,
                        `updatedAt` TEXT NOT NULL,
                        PRIMARY KEY(`paperId`, `n`)
                    )
                    """.trimIndent(),
                )
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE `cached_papers` ADD COLUMN `positiveMarks` REAL NOT NULL DEFAULT 1.0")
                db.execSQL("ALTER TABLE `cached_papers` ADD COLUMN `negativeMarks` REAL NOT NULL DEFAULT 0.0")
            }
        }
    }
}