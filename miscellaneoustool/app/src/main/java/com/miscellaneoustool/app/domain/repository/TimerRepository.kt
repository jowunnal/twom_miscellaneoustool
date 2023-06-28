package com.miscellaneoustool.app.domain.repository

import com.miscellaneoustool.app.TimerPreferences
import com.miscellaneoustool.app.domain.model.MonsterType
import com.miscellaneoustool.app.domain.model.TimerModel
import kotlinx.coroutines.flow.Flow

interface TimerRepository {

    fun getTimerPreferences(): Flow<TimerPreferences>
    suspend fun addBossToFrequentlyUsedList(bossName: String)
    suspend fun setBossToFrequentlyUsedList(bossList: List<String>)
    suspend fun setRecentlySelectedBossClassified(bossClassified: MonsterType)
    suspend fun setRecentlySelectedBossName(bossName: String)
    suspend fun setIntervalFirstTimerSetting(minutes: Int)
    suspend fun setIntervalSecondTimerSetting(minutes: Int)
    suspend fun setFontSize(size: Int)

    suspend fun updateTimerInterval(firstIntervalTime: Int, secondIntervalTime: Int)
    suspend fun setTimer(id: Int, day: Int, hour: Int, min: Int, sec: Int, bossName: String)
    suspend fun updateTimer(id: Int, day: Int, hour: Int, min: Int, sec: Int)
    suspend fun deleteTimer(bossName: String)
    fun getTimer(): Flow<List<TimerModel>>
    suspend fun setOta(ota: Int, bossName: String)

}