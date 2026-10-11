package com.tgp.mockexam.core.data.di

import com.tgp.mockexam.core.data.auth.AppwriteAuthRepository
import com.tgp.mockexam.core.data.repository.AssetPaperRepository
import com.tgp.mockexam.core.data.repository.CachedPaperRepository
import com.tgp.mockexam.core.data.repository.CurrentAffairsRepositoryImpl
import com.tgp.mockexam.core.data.repository.ProfileRepositoryImpl
import com.tgp.mockexam.core.data.repository.RoomExamSessionRepository
import com.tgp.mockexam.core.domain.repository.AuthRepository
import com.tgp.mockexam.core.domain.repository.CurrentAffairsRepository
import com.tgp.mockexam.core.domain.repository.ExamSessionRepository
import com.tgp.mockexam.core.domain.repository.PaperRepository
import com.tgp.mockexam.core.domain.repository.ProfileRepository
import com.tgp.mockexam.core.network.AccountClient
import com.tgp.mockexam.core.network.AppwriteConfig
import com.tgp.mockexam.core.network.HttpAccountClient
import com.tgp.mockexam.core.network.HttpTablesClient
import com.tgp.mockexam.core.network.TablesClient
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        repository: ProfileRepositoryImpl,
    ): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindExamSessionRepository(
        repository: RoomExamSessionRepository,
    ): ExamSessionRepository

    @Binds
    @Singleton
    abstract fun bindPaperRepository(
        repository: CachedPaperRepository,
    ): PaperRepository

    @Binds
    @Singleton
    abstract fun bindCurrentAffairsRepository(
        repository: CurrentAffairsRepositoryImpl,
    ): CurrentAffairsRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        repository: AppwriteAuthRepository,
    ): AuthRepository

    companion object {

        @Provides
        @Singleton
        fun provideAppwriteConfig(): AppwriteConfig = AppwriteConfig()

        @Provides
        @Singleton
        fun provideTablesClient(config: AppwriteConfig): TablesClient = HttpTablesClient(config)

        @Provides
        @Singleton
        fun provideAccountClient(config: AppwriteConfig): AccountClient = HttpAccountClient(config)

        @Provides
        @Singleton
        fun provideAdminWriteClient(config: AppwriteConfig): com.tgp.mockexam.core.network.AdminWriteClient = com.tgp.mockexam.core.network.HttpAdminWriteClient(config)
    }
}