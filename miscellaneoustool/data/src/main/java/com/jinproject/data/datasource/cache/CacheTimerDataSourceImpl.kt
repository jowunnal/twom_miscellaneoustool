package com.jinproject.data.datasource.cache

import androidx.datastore.core.DataStore
import com.jinproject.core.TimerPreferences
import com.jinproject.domain.model.MonsterType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException
import javax.inject.Inject

class CacheTimerDataSourceImpl @Inject constructor(private val dataStorePrefs: DataStore<TimerPreferences>) :
    CacheTimerDataSource {

    override val timerPreferences: Flow<TimerPreferences> = dataStorePrefs.data
        .catch { exception ->
            if (exception is IOException) {
                emit(TimerPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }

    override suspend fun addBossToFrequentlyUsedList(bossName: String) {
        dataStorePrefs.updateData { prefs ->
            prefs.toBuilder()
                .addFrequentlyUsedBossList(bossName)
                .build()
        }
    }

    override suspend fun setBossToFrequentlyUsedList(bossList: List<String>) {
        dataStorePrefs.updateData { prefs ->
            prefs.toBuilder()
                .clearFrequentlyUsedBossList()
                .addAllFrequentlyUsedBossList(bossList)
                .build()
        }
    }

    override suspend fun setRecentlySelectedBossClassified(bossClassified: MonsterType) {
        dataStorePrefs.updateData { prefs ->
            prefs.toBuilder()
                .setRecentlySelectedBossClassified(bossClassified.storedName)
                .build()
        }
    }

    override suspend fun setRecentlySelectedBossName(bossName: String) {
        dataStorePrefs.updateData { prefs ->
            prefs.toBuilder()
                .setRecentlySelectedBossName(bossName)
                .build()
        }
    }

    override suspend fun setIntervalFirstTimerSetting(minutes: Int) {
        dataStorePrefs.updateData { prefs ->
            prefs.toBuilder()
                .setIntervalFirstTimerSetting(minutes)
                .build()
        }
    }

    override suspend fun setIntervalSecondTimerSetting(minutes: Int) {
        dataStorePrefs.updateData { prefs ->
            prefs.toBuilder()
                .setIntervalSecondTimerSetting(minutes)
                .build()
        }
    }

    override suspend fun setTimerSetting(fontSize: Int, xPos: Int, yPos: Int) {
        dataStorePrefs.updateData { prefs ->
            prefs.toBuilder()
                .setXPos(xPos)
                .setYPos(yPos)
                .setFontSize(fontSize)
                .build()
        }
    }

    override suspend fun updateTimerInterval(firstIntervalTime: Int, secondIntervalTime: Int) {
        dataStorePrefs.updateData { prefs ->
            prefs.toBuilder()
                .setIntervalFirstTimerSetting(firstIntervalTime)
                .setIntervalSecondTimerSetting(secondIntervalTime)
                .build()
        }
    }
}