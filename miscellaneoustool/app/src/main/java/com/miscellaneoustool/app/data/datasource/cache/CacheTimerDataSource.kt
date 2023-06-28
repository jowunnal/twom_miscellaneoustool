package com.miscellaneoustool.app.data.datasource.cache

import com.miscellaneoustool.app.TimerPreferences
import com.miscellaneoustool.app.domain.model.MonsterType
import kotlinx.coroutines.flow.Flow

interface CacheTimerDataSource {

    val timerPreferences: Flow<TimerPreferences>

    suspend fun addBossToFrequentlyUsedList(bossName: String)

    suspend fun setBossToFrequentlyUsedList(bossList: List<String>)

    suspend fun setRecentlySelectedBossClassified(bossClassified: MonsterType)

    suspend fun setRecentlySelectedBossName(bossName: String)

    suspend fun setIntervalFirstTimerSetting(minutes: Int)

    suspend fun setIntervalSecondTimerSetting(minutes: Int)

    suspend fun setFontSize(size: Int)

    suspend fun updateTimerInterval(firstIntervalTime: Int, secondIntervalTime: Int)

}