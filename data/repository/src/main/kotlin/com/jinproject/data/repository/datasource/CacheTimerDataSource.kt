package com.jinproject.data.repository.datasource

import com.jinproject.data.repository.model.Timer
import com.jinproject.data.repository.model.TimerSetting
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.ZonedDateTime

interface CacheTimerDataSource {
    suspend fun addBossToFrequentlyUsedList(bossName: String)
    suspend fun updateTimerSetting(timerSetting: TimerSetting)
    fun getTimerSetting(): Flow<TimerSetting>

    fun getTimerList(): Flow<List<Timer>>
    fun getTimer(bossName: String): Flow<Timer?>
    suspend fun setTimer(
        id: Int,
        bossName: String,
        deadTime: ZonedDateTime,
    )

    suspend fun updateTimer(monsName: String, nextSpawnDateTime: ZonedDateTime)
    suspend fun deleteTimer(bossName: String)
}
