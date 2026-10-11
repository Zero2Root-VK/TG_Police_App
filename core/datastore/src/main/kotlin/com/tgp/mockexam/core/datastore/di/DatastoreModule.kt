package com.tgp.mockexam.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.tgp.mockexam.core.datastore.DefaultSessionRepository
import com.tgp.mockexam.core.datastore.DefaultUserPreferencesRepository
import com.tgp.mockexam.core.datastore.tgpPreferencesDataStore
import com.tgp.mockexam.core.domain.repository.SessionRepository
import com.tgp.mockexam.core.domain.repository.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.tgpPreferencesDataStore
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DatastoreBindingsModule {

    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(
        repository: DefaultUserPreferencesRepository,
    ): UserPreferencesRepository

    @Binds
    @Singleton
    abstract fun bindSessionRepository(
        repository: DefaultSessionRepository,
    ): SessionRepository
}
