package com.jinproject.domain.repository

import com.jinproject.domain.model.MonsterType
import com.jinproject.domain.model.TimerModel
import com.jinproject.domain.usecase.timer.AlarmStoredBoss
import com.jinproject.domain.usecase.timer.Interval
import com.jinproject.domain.usecase.timer.OverlaySetting
import kotlinx.coroutines.flow.Flow

interface TimerRepository {

    fun getOverlaySetting(): Flow<OverlaySetting>
    fun getInterval(): Flow<Interval>

    fun getAlarmStoredBoss(): Flow<AlarmStoredBoss>
    suspend fun addBossToFrequentlyUsedList(bossName: String)
    suspend fun setBossToFrequentlyUsedList(bossList: List<String>)
    suspend fun setRecentlySelectedBossClassified(bossClassified: MonsterType)
    suspend fun setRecentlySelectedBossName(bossName: String)
    suspend fun setIntervalTimerSetting(first: Int, second: Int)
    suspend fun setTimerSetting(fontSize: Int, xPos: Int, yPos: Int)

    suspend fun updateTimerInterval(firstIntervalTime: Int, secondIntervalTime: Int)
    suspend fun setTimer(id: Int, day: Int, hour: Int, min: Int, sec: Int, bossName: String)
    suspend fun updateTimer(id: Int, day: Int, hour: Int, min: Int, sec: Int)
    suspend fun deleteTimer(bossName: String)
    fun getTimer(): Flow<List<TimerModel>>
    suspend fun setOta(ota: Int, bossName: String)

}