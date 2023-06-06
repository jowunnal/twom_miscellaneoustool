package com.jinproject.twomillustratedbook.data.repository

import androidx.datastore.core.DataStore
import com.jinproject.twomillustratedbook.TimerPreferences
import com.jinproject.twomillustratedbook.data.database.dao.TimerDao
import com.jinproject.twomillustratedbook.domain.model.MonsterType
import com.jinproject.twomillustratedbook.domain.model.TimerModel
import com.jinproject.twomillustratedbook.domain.repository.TimerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class TimerRepositoryImpl @Inject constructor(
    private val timerDao: TimerDao,
    private val timerDataStore: DataStore<TimerPreferences>
) : TimerRepository {

    override val timerPreferences: Flow<TimerPreferences> = timerDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(TimerPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }

    override suspend fun addBossToFrequentlyUsedList(bossName: String) {
        timerDataStore.updateData { prefs ->
            prefs.toBuilder().addFrequentlyUsedBossList(bossName).build()
        }
    }

    override suspend fun setBossToFrequentlyUsedList(bossList: List<String>) {
        timerDataStore.updateData { prefs ->
            prefs.toBuilder().clearFrequentlyUsedBossList().addAllFrequentlyUsedBossList(bossList)
                .build()
        }
    }

    override suspend fun setRecentlySelectedBossClassified(bossClassified: MonsterType) {
        timerDataStore.updateData { prefs ->
            prefs.toBuilder().setRecentlySelectedBossClassified(bossClassified.storedName).build()
        }
    }

    override suspend fun setRecentlySelectedBossName(bossName: String) {
        timerDataStore.updateData { prefs ->
            prefs.toBuilder().setRecentlySelectedBossName(bossName).build()
        }
    }

    override suspend fun setIntervalFirstTimerSetting(minutes: Int) {
        timerDataStore.updateData { prefs ->
            prefs.toBuilder().setIntervalFirstTimerSetting(minutes).build()
        }
    }

    override suspend fun setIntervalSecondTimerSetting(minutes: Int) {
        timerDataStore.updateData { prefs ->
            prefs.toBuilder().setIntervalSecondTimerSetting(minutes).build()
        }
    }

    override suspend fun setFontSize(size: Int) {
        timerDataStore.updateData { prefs ->
            prefs.toBuilder().setFontSize(size).build()
        }
    }

    override suspend fun updateTimerInterval(firstIntervalTime: Int, secondIntervalTime: Int) {
        timerDataStore.updateData { prefs ->
            prefs.toBuilder()
                .setIntervalFirstTimerSetting(firstIntervalTime)
                .setIntervalSecondTimerSetting(secondIntervalTime)
                .build()
        }
    }

    override fun getTimer() = timerDao.getTimer().map { response ->
        response.map { timer ->
            TimerModel.fromTimerResponse(timer)
        }
    }

    override suspend fun setTimer(
        id: Int,
        day: Int,
        hour: Int,
        min: Int,
        sec: Int,
        bossName: String
    ) = timerDao.setTimer(id, day, hour, min, sec, bossName)

    override suspend fun updateTimer(id: Int, day: Int, hour: Int, min: Int, sec: Int) {
        timerDao.updateTimer(
            id = id,
            day = day,
            hour = hour,
            min = min,
            sec = sec
        )
    }

    override suspend fun deleteTimer(bossName: String) = timerDao.deleteTimer(bossName)
    override suspend fun setOta(ota: Int, bossName: String) = timerDao.setOta(ota, bossName)

}