package com.jinproject.data.datasource.cache

import androidx.datastore.core.DataStore
import com.jinproject.core.TimerPreferences
import com.jinproject.data.datasource.cache.database.dao.TimerDao
import com.jinproject.data.datasource.cache.database.entity.toTimerDataModel
import com.jinproject.data.datasource.cache.mapper.toAlarmStoredBoss
import com.jinproject.data.datasource.cache.mapper.toInterval
import com.jinproject.data.datasource.cache.mapper.toOverlaySetting
import com.jinproject.data.repository.datasource.CacheTimerDataSource
import com.jinproject.data.repository.model.AlarmStoredBoss
import com.jinproject.data.repository.model.Interval
import com.jinproject.data.repository.model.OverlaySetting
import com.jinproject.data.repository.model.Timer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
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

    override fun getOverlaySetting(): Flow<OverlaySetting> =
        data.map { prefs ->
            prefs.toOverlaySetting()
        }

    override fun getInterval(): Flow<Interval> =
        data.map { prefs ->
            prefs.toInterval()
        }

    override fun getAlarmStoredBoss(): Flow<AlarmStoredBoss> =
        data.map { prefs ->
            prefs.toAlarmStoredBoss()
        }

    override suspend fun addBossToFrequentlyUsedList(bossName: String) {
        prefs.updateData { prefs ->
            prefs.toBuilder()
                .addFrequentlyUsedBossList(bossName)
                .build()
        }
    }

    override suspend fun setBossToFrequentlyUsedList(bossList: List<String>) {
        prefs.updateData { prefs ->
            prefs.toBuilder()
                .clearFrequentlyUsedBossList()
                .addAllFrequentlyUsedBossList(bossList)
                .build()
        }
    }

    override suspend fun setRecentlySelectedBossClassified(bossName: String) {
        //TODO 보스 타입을 데이터베이스의 문자 기준으로 바꿧기 때문에, 실유저 기기에서 다른 문자가 들어와서 오류 발생 가능
        prefs.updateData { prefs ->
            prefs.toBuilder()
                .setRecentlySelectedBossClassified(bossName)
                .build()
        }
    }

    override suspend fun setRecentlySelectedBossName(bossName: String) {
        prefs.updateData { prefs ->
            prefs.toBuilder()
                .setRecentlySelectedBossName(bossName)
                .build()
        }
    }

    override suspend fun setIntervalTimerSetting(first: Int, second: Int) {
        prefs.updateData { prefs ->
            prefs.toBuilder()
                .setIntervalFirstTimerSetting(first)
                .setIntervalSecondTimerSetting(second)
                .build()
        }
    }

    override suspend fun setTimerSetting(fontSize: Int, xPos: Int, yPos: Int) {
        prefs.updateData { prefs ->
            prefs.toBuilder()
                .setXPos(xPos)
                .setYPos(yPos)
                .setFontSize(fontSize)
                .build()
        }
    }

    override suspend fun updateTimerInterval(firstIntervalTime: Int, secondIntervalTime: Int) {
        prefs.updateData { prefs ->
            prefs.toBuilder()
                .setIntervalFirstTimerSetting(firstIntervalTime)
                .setIntervalSecondTimerSetting(secondIntervalTime)
                .build()
        }
    }

    override fun getTimer(): Flow<List<Timer>> = timerDao.getTimer().map { response ->
        response.map { timer ->
            timer.toTimerDataModel()
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