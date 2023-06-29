package com.miscellaneoustool.data.datasource.cache.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.miscellaneoustool.core.TimerPreferences
import com.miscellaneoustool.data.CollectionPreferences
import com.miscellaneoustool.data.serializer.CollectionPreferencesSerializer
import com.miscellaneoustool.data.serializer.TimerPreferencesSerializer

private const val DATA_STORE_FILE_NAME = "timer_prefs.pb"
private const val COLLECTION_DATA_STORE_FILE_NAME = "collection_prefs.pb"

val Context.timerPreferencesStore: DataStore<TimerPreferences> by dataStore(
    fileName = DATA_STORE_FILE_NAME,
    serializer = TimerPreferencesSerializer
)

val Context.collectionPreferencesStore: DataStore<CollectionPreferences> by dataStore(
    fileName = COLLECTION_DATA_STORE_FILE_NAME,
    serializer = CollectionPreferencesSerializer
)
