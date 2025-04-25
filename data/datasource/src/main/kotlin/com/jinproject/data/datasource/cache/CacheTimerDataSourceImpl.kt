package com.jinproject.data.datasource.cache

import android.util.Log
import androidx.datastore.core.DataStore
import com.jinproject.core.TimerPreferences
import com.jinproject.core.util.toEpochMilli
import com.jinproject.data.datasource.cache.database.dao.TimerDao
import com.jinproject.data.datasource.cache.database.entity.toTimerDataModel
import com.jinproject.data.datasource.cache.mapper.toTimerSetting
import com.jinproject.data.repository.datasource.CacheTimerDataSource
import com.jinproject.data.repository.model.Timer
import com.jinproject.data.repository.model.TimerSetting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.io.IOException
import java.time.LocalDateTime
import java.time.ZonedDateTime
import javax.inject.Inject

class CacheTimerDataSourceImpl @Inject constructor(
    private val prefs: DataStore<TimerPreferences>,
    private val timerDao: TimerDao,
) : CacheTimerDataSource {

    val data: Flow<TimerPreferences> = prefs.data
        .catch { exception ->
            if (exception is IOException) {
                emit(TimerPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }

    override suspend fun addBossToFrequentlyUsedList(bossName: String) {
        prefs.updateData { prefs ->
            prefs.toBuilder()
                .addFrequentlyUsedBossList(bossName)
                .build()
        }
    }

    override suspend fun updateTimerSetting(timerSetting: TimerSetting) {
        prefs.updateData { prefs ->
            with(prefs.toBuilder()) {
                timerSetting.xPos?.let {
                    xPos = it
                }
                timerSetting.yPos?.let {
                    yPos = it
                }
                timerSetting.fontSize?.let {
                    fontSize = it
                }
                timerSetting.firstInterval?.let {
                    intervalFirstTimerSetting = it
                }
                timerSetting.secondInterval?.let {
                    intervalSecondTimerSetting = it
                }
                timerSetting.frequentlyUsedBossList?.let {
                    clearFrequentlyUsedBossList()
                    addAllFrequentlyUsedBossList(it)
                }
                timerSetting.overlaidMonsterList?.let {
                    clearOverlayBossList()
                    addAllOverlayBossList(it)
                    it.forEach { overlaidMonster ->
                        setMonsterOverlay(overlaidMonster)
                    }
                }
                build()
            }
        }
    }

    override fun getTimerSetting(): Flow<TimerSetting> = data.map {
        it.toTimerSetting()
    }

    override fun getTimer(bossName: String): Flow<Timer?> =
        timerDao.getTimer(name = bossName).map { it?.toTimerDataModel() }

    override fun getTimerList(): Flow<List<Timer>> = timerDao.getTimerList().map { response ->
        response.map { timer ->
            timer.toTimerDataModel()
        }
    }

    override suspend fun setTimer(id: Int, bossName: String, deadTime: ZonedDateTime) {
        timerDao.setTimer(
            id = id,
            epochMilli = deadTime.toEpochMilli(),
            name = bossName,
            isOverlaying = data.first().overlayBossListList.contains(bossName)
        )
    }

    override suspend fun updateTimer(monsName: String, nextSpawnDateTime: ZonedDateTime) {
        timerDao.updateTimer(
            monsName = monsName,
            epochMilli = nextSpawnDateTime.toEpochMilli(),
            isOverlaying = data.first().overlayBossListList.contains(monsName)
        )
    }

    private suspend fun setMonsterOverlay(monsName: String) = timerDao.setMonsterOverlaid(monsName)

    override suspend fun deleteTimer(bossName: String) = timerDao.deleteTimer(bossName)
}