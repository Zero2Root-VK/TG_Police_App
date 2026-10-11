package com.tgp.mockexam

import android.app.Application
import com.tgp.mockexam.core.data.sync.AppwriteContentSyncer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@HiltAndroidApp
class TgpApplication : Application() {

    @Inject
    lateinit var syncer: AppwriteContentSyncer

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        // Stream content from Appwrite into the Room cache on start; the UI reads Room with an
        // asset fallback, so a slow/failed sync never blocks the first screen.
        applicationScope.launch { syncer.sync() }
    }
}