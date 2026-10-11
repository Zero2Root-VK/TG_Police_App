package com.tgp.mockexam.core.database.di

import android.content.Context
import androidx.room.Room
import com.tgp.mockexam.core.database.TgpDatabase
import com.tgp.mockexam.core.database.dao.ContentDao
import com.tgp.mockexam.core.database.dao.ExamSessionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTgpDatabase(@ApplicationContext context: Context): TgpDatabase =
        Room.databaseBuilder(context, TgpDatabase::class.java, "tgp.db")
            .addMigrations(TgpDatabase.MIGRATION_1_2, TgpDatabase.MIGRATION_2_3)
            .build()

    @Provides
    fun provideExamSessionDao(database: TgpDatabase): ExamSessionDao = database.examSessionDao()

    @Provides
    fun provideContentDao(database: TgpDatabase): ContentDao = database.contentDao()
}