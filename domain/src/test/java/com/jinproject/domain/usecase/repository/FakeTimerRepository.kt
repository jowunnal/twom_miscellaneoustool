package com.jinproject.domain.usecase.repository

import com.jinproject.domain.repository.TimerRepository
import com.jinproject.domain.usecase.alarm.SetAlarmUsecase
import com.jinproject.domain.usecase.alarm.ManageTimerSettingUsecase
import jdk.jfr.internal.OldObjectSample.emit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime

class FakeTimerRepository : TimerRepository {
    var timerSetting: ManageTimerSettingUsecase.TimerSetting =
        ManageTimerSettingUsecase.TimerSetting()
    var timerList: MutableList<SetAlarmUsecase.Timer> = mutableListOf()

    override suspend fun deleteTimer(monsName: String) {
        timerList.removeIf { it.monsterName == monsName }
    }

    override fun getTimerList(filterOverlaying: Boolean): Flow<List<SetAlarmUsecase.Timer>> = flow {
        emit(timerList)
    }

    override suspend fun getAndSetBossTimerList(
        monsName: String,
        nextSpawnDateTime: LocalDateTime
    ) {
        val idx = timerList.indexOfFirst { it.monsterName == monsName }

        if (idx != -1) {
            timerList[idx] = timerList[idx].copy(dateTime = nextSpawnDateTime)
        } else {
            val ids = timerList.map { it.id }
            var id = 1
            while (true) {
                if (id !in ids)
                    break
                id++
            }

            timerList.add(
                SetAlarmUsecase.Timer(
                    id = id,
                    monsterName = monsName,
                    dateTime = nextSpawnDateTime,
                )
            )
        }
    }

    override fun getTimerSetting(): Flow<ManageTimerSettingUsecase.TimerSetting> = flow {
        emit(timerSetting)
    }

    override suspend fun updateTimerSetting(timerSetting: ManageTimerSettingUsecase.TimerSetting) {
        this.timerSetting = timerSetting
    }

    override fun getTimer(monsName: String): Flow<List<SetAlarmUsecase.Timer>> = flow {
        val timer = timerList.find { it.monsterName == monsName }

        emit(
            timer?.let {
                listOf(
                    it.copy(
                        dateTime = it.dateTime.minusMinutes(
                            timerSetting.interval?.firstInterval?.toLong() ?: 0L
                        )
                    ),
                    it.copy(
                        dateTime = it.dateTime.minusMinutes(
                            timerSetting.interval?.secondInterval?.toLong() ?: 5L
                        )
                    ),
                )
            } ?: emptyList()
        )
    }

}