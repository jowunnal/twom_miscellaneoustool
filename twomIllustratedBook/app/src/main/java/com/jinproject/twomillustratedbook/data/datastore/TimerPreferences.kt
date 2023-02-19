package com.jinproject.twomillustratedbook.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.jinproject.twomillustratedbook.TimerPreferences
import com.jinproject.twomillustratedbook.data.serializer.TimerPreferencesSerializer

private const val DATA_STORE_FILE_NAME = "timer_prefs.pb"

val Context.timerPreferencesStore: DataStore<TimerPreferences> by dataStore(
    fileName = DATA_STORE_FILE_NAME,
    serializer = TimerPreferencesSerializer
)