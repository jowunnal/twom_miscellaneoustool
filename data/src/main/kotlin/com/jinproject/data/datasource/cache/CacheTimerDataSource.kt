package com.jinproject.data.datasource.cache

import androidx.datastore.core.DataStore
import com.jinproject.core.TimerPreferences
import com.jinproject.domain.model.MonsterType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException
import javax.inject.Inject

class CacheTimerDataSource @Inject constructor(private val dataStorePrefs: DataStore<TimerPreferences>) {

    val timerPreferences: Flow<TimerPreferences> = dataStorePrefs.data
        .catch { exception ->
            if (exception is IOException) {
                emit(TimerPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }

    suspend fun addBossToFrequentlyUsedList(bossName: String) {
        dataStorePrefs.updateData { prefs ->
            prefs.toBuilder()
                .addFrequentlyUsedBossList(bossName)
                .build()
        }
    }

    suspend fun setBossToFrequentlyUsedList(bossList: List<String>) {
        dataStorePrefs.updateData { prefs ->
            prefs.toBuilder()
                .clearFrequentlyUsedBossList()
                .addAllFrequentlyUsedBossList(bossList)
                .build()
        }
    }

    suspend fun setRecentlySelectedBossClassified(bossClassified: MonsterType) {
        //TODO 보스 타입을 데이터베이스의 문자 기준으로 바꿧기 때문에, 실유저 기기에서 다른 문자가 들어와서 오류 발생 가능
        dataStorePrefs.updateData { prefs ->
            prefs.toBuilder()
                .setRecentlySelectedBossClassified(bossClassified.name)
                .build()
        }
    }

    suspend fun setRecentlySelectedBossName(bossName: String) {
        dataStorePrefs.updateData { prefs ->
            prefs.toBuilder()
                .setRecentlySelectedBossName(bossName)
                .build()
        }
    }

    suspend fun setIntervalTimerSetting(first: Int, second: Int) {
        dataStorePrefs.updateData { prefs ->
            prefs.toBuilder()
                .setIntervalFirstTimerSetting(first)
                .setIntervalSecondTimerSetting(second)
                .build()
        }
    }

    suspend fun setTimerSetting(fontSize: Int, xPos: Int, yPos: Int) {
        dataStorePrefs.updateData { prefs ->
            prefs.toBuilder()
                .setXPos(xPos)
                .setYPos(yPos)
                .setFontSize(fontSize)
                .build()
        }
    }

    suspend fun updateTimerInterval(firstIntervalTime: Int, secondIntervalTime: Int) {
        dataStorePrefs.updateData { prefs ->
            prefs.toBuilder()
                .setIntervalFirstTimerSetting(firstIntervalTime)
                .setIntervalSecondTimerSetting(secondIntervalTime)
                .build()
        }
    }
}