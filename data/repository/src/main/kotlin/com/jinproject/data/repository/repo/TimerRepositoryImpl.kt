package com.jinproject.data.repository.repo

import com.jinproject.data.repository.datasource.CacheTimerDataSource
import com.jinproject.data.repository.model.TimerSetting
import com.jinproject.data.repository.model.toDomain
import com.jinproject.domain.repository.TimerRepository
import com.jinproject.domain.usecase.alarm.ManageTimerSettingUsecase
import com.jinproject.domain.usecase.alarm.SetAlarmUsecase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import java.time.LocalDateTime
import java.time.ZonedDateTime
import javax.inject.Inject

class TimerRepositoryImpl @Inject constructor(
    private val cacheTimerDataSource: CacheTimerDataSource,
) : TimerRepository {

    override fun getTimerList(filterOverlaying: Boolean): Flow<List<SetAlarmUsecase.Timer>> =
        cacheTimerDataSource.getTimerList().map { response ->
            response.filter { if(filterOverlaying) it.ota == 1 else true }.map { timer ->
                timer.toDomain()
            }
        }

    override suspend fun deleteTimer(bossName: String) {
        cacheTimerDataSource.deleteTimer(bossName)
    }

    override suspend fun getAndSetBossTimerList(
        bossName: String,
        nextSpawnDateTime: ZonedDateTime
    ) {
        val timers = cacheTimerDataSource.getTimerList().first()

        timers.find { it.monsterName == bossName }?.let {
            cacheTimerDataSource.updateTimer(
                monsName = it.monsterName,
                nextSpawnDateTime = nextSpawnDateTime
            )
        } ?: run {
            val ids = timers.map { it.timerId }
            var id = 1
            while (true) {
                if (id !in ids)
                    break
                id++
            }

            cacheTimerDataSource.setTimer(
                id = id,
                bossName = bossName,
                deadTime = nextSpawnDateTime
            )
        }
    }

    override fun getTimerSetting(): Flow<ManageTimerSettingUsecase.TimerSetting> =
        cacheTimerDataSource.getTimerSetting().map { it.toDomain() }

    override suspend fun updateTimerSetting(timerSetting: ManageTimerSettingUsecase.TimerSetting) {
        cacheTimerDataSource.updateTimerSetting(timerSetting = TimerSetting.fromDomain(timerSetting = timerSetting))
    }

    override fun getTimer(bossName: String): Flow<List<SetAlarmUsecase.Timer>> =
        cacheTimerDataSource.getTimerSetting()
            .zip(cacheTimerDataSource.getTimer(bossName)) { setting, timer ->
                if (timer == null)
                    emptyList<SetAlarmUsecase.Timer>()
                else {
                    val timerDomain = timer.toDomain()

                    listOf(
                        timerDomain
                            .copy(dateTime = timer.dateTime.minusMinutes(setting.firstInterval!!.toLong())),
                        timerDomain
                            .copy(dateTime = timer.dateTime.minusMinutes(setting.secondInterval!!.toLong())),
                    )
                }
            }


}