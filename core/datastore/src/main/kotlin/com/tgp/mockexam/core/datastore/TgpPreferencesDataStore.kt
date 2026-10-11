package com.tgp.mockexam.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

/** Single app-wide [DataStore] for lightweight preferences and the persisted auth session. */
val Context.tgpPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "tgp_user_prefs")
