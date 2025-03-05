package com.jinproject.data.datasource.cache.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.jinproject.core.TimerPreferences
import com.jinproject.data.CollectionPreferences
import com.jinproject.data.SimulatorPreferences
import com.jinproject.data.datasource.cache.datastore.serializer.CollectionPreferencesSerializer
import com.jinproject.data.datasource.cache.datastore.serializer.SimulatorPreferencesSerializer
import com.jinproject.data.datasource.cache.datastore.serializer.TimerPreferencesSerializer

private const val DATA_STORE_FILE_NAME = "timer_prefs.pb"

internal val Context.timerPreferencesStore: DataStore<TimerPreferences> by dataStore(
    fileName = DATA_STORE_FILE_NAME,
    serializer = TimerPreferencesSerializer
)

internal val Context.collectionPreferencesStore: DataStore<CollectionPreferences> by dataStore(
    fileName = "collection_prefs.pb",
    serializer = CollectionPreferencesSerializer
)

internal val Context.simulatorPreferencesStore: DataStore<SimulatorPreferences> by dataStore(
    fileName = "simulator_prefs.pb",
    serializer = SimulatorPreferencesSerializer,
)
