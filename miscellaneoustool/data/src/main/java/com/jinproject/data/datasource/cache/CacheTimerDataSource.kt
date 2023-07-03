package com.jinproject.data.datasource.cache

import com.jinproject.core.TimerPreferences
import com.jinproject.domain.model.MonsterType
import kotlinx.coroutines.flow.Flow

interface CacheTimerDataSource {

    val timerPreferences: Flow<TimerPreferences>

    suspend fun addBossToFrequentlyUsedList(bossName: String)

    suspend fun setBossToFrequentlyUsedList(bossList: List<String>)

    suspend fun setRecentlySelectedBossClassified(bossClassified: MonsterType)

    suspend fun setRecentlySelectedBossName(bossName: String)

    suspend fun setIntervalTimerSetting(first:Int, second: Int)

    suspend fun setTimerSetting(fontSize: Int, xPos: Int, yPos: Int)

    suspend fun updateTimerInterval(firstIntervalTime: Int, secondIntervalTime: Int)

}