package com.jinproject.data.repository.datasource

import com.jinproject.data.repository.datasource.base.CacheDataStoreDataSource
import com.jinproject.data.repository.model.AlarmStoredBoss
import com.jinproject.data.repository.model.Interval
import com.jinproject.data.repository.model.OverlaySetting
import com.jinproject.data.repository.model.Timer
import kotlinx.coroutines.flow.Flow

interface CacheTimerDataSource<T> : CacheDataStoreDataSource<T> {
    fun getOverlaySetting(): Flow<OverlaySetting>
    fun getInterval(): Flow<Interval>
    fun getAlarmStoredBoss(): Flow<AlarmStoredBoss>
    suspend fun addBossToFrequentlyUsedList(bossName: String)
    suspend fun setBossToFrequentlyUsedList(bossList: List<String>)
    suspend fun setRecentlySelectedBossClassified(bossName: String)
    suspend fun setRecentlySelectedBossName(bossName: String)
    suspend fun setIntervalTimerSetting(first: Int, second: Int)
    suspend fun setTimerSetting(fontSize: Int, xPos: Int, yPos: Int)
    suspend fun updateTimerInterval(firstIntervalTime: Int, secondIntervalTime: Int)

    fun getTimer(): Flow<List<Timer>>
    suspend fun setTimer(
        id: Int,
        day: Int,
        hour: Int,
        min: Int,
        sec: Int,
        bossName: String,
    )

    suspend fun updateTimer(id: Int, day: Int, hour: Int, min: Int, sec: Int)
    suspend fun deleteTimer(bossName: String)
    suspend fun setOta(ota: Int, bossName: String)
}
