package com.jinproject.data.repository.repo

import com.jinproject.data.repository.datasource.CacheTimerDataSource
import com.jinproject.data.repository.model.toDomainModel
import com.jinproject.data.repository.model.toTimerModel
import com.jinproject.domain.model.MonsterType
import com.jinproject.domain.repository.TimerRepository
import com.jinproject.domain.usecase.timer.AlarmStoredBoss
import com.jinproject.domain.usecase.timer.Interval
import com.jinproject.domain.usecase.timer.OverlaySetting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TimerRepositoryImpl @Inject constructor(
    private val cacheTimerDataSource: CacheTimerDataSource
) : TimerRepository {

    override fun getOverlaySetting(): Flow<OverlaySetting> =
        cacheTimerDataSource.getOverlaySetting().map { it.toDomainModel() }

    override fun getInterval(): Flow<Interval> =
        cacheTimerDataSource.getInterval().map { it.toDomainModel() }

    override fun getAlarmStoredBoss(): Flow<AlarmStoredBoss> =
        cacheTimerDataSource.getAlarmStoredBoss().map { it.toDomainModel() }

    override suspend fun addBossToFrequentlyUsedList(bossName: String) {
        cacheTimerDataSource.addBossToFrequentlyUsedList(bossName)
    }

    override suspend fun setBossToFrequentlyUsedList(bossList: List<String>) {
        cacheTimerDataSource.setBossToFrequentlyUsedList(bossList)
    }

    override suspend fun setRecentlySelectedBossClassified(bossClassified: MonsterType) {
        cacheTimerDataSource.setRecentlySelectedBossClassified(bossClassified.name)
    }

    override suspend fun setRecentlySelectedBossName(bossName: String) {
        cacheTimerDataSource.setRecentlySelectedBossName(bossName)
    }

    override suspend fun setIntervalTimerSetting(first: Int, second: Int) {
        cacheTimerDataSource.setIntervalTimerSetting(first, second)
    }

    override suspend fun setTimerSetting(fontSize: Int, xPos: Int, yPos: Int) {
        cacheTimerDataSource.setTimerSetting(fontSize, xPos, yPos)
    }

    override suspend fun updateTimerInterval(firstIntervalTime: Int, secondIntervalTime: Int) {
        cacheTimerDataSource.updateTimerInterval(firstIntervalTime, secondIntervalTime)
    }

    override fun getTimer() = cacheTimerDataSource.getTimer().map { response ->
        response.map { timer ->
            timer.toTimerModel()
        }
    }

    override suspend fun setTimer(
        id: Int,
        day: Int,
        hour: Int,
        min: Int,
        sec: Int,
        bossName: String
    ) {
        cacheTimerDataSource.setTimer(id, day, hour, min, sec, bossName)
    }

    override suspend fun updateTimer(id: Int, day: Int, hour: Int, min: Int, sec: Int) {
        cacheTimerDataSource.updateTimer(
            id = id,
            day = day,
            hour = hour,
            min = min,
            sec = sec
        )
    }

    override suspend fun deleteTimer(bossName: String) {
        cacheTimerDataSource.deleteTimer(bossName)
    }

    override suspend fun setOta(ota: Int, bossName: String) {
        cacheTimerDataSource.setOta(ota, bossName)
    }

}