package com.jinproject.twomillustratedbook.data.repository

import com.jinproject.twomillustratedbook.TimerPreferences
import com.jinproject.twomillustratedbook.domain.model.MonsterType
import com.jinproject.twomillustratedbook.domain.model.TimerModel
import kotlinx.coroutines.flow.Flow

interface TimerRepository {

    val timerPreferences: Flow<TimerPreferences>
    suspend fun addBossToFrequentlyUsedList(bossName: String)
    suspend fun setBossToFrequentlyUsedList(bossList: List<String>)
    suspend fun setRecentlySelectedBossClassified(bossClassified: MonsterType)
    suspend fun setRecentlySelectedBossName(bossName: String)
    suspend fun setIntervalFirstTimerSetting(minutes: Int)
    suspend fun setIntervalSecondTimerSetting(minutes: Int)

    suspend fun updateTimerInterval(firstIntervalTime: Int, secondIntervalTime: Int)
    suspend fun setTimer(day:Int,hour:Int, min:Int,sec:Int, bossName:String)
    suspend fun deleteTimer(bossName: String)
    fun getTimer() : Flow<List<TimerModel>>
    suspend fun setOta(ota:Int,bossName:String)

}