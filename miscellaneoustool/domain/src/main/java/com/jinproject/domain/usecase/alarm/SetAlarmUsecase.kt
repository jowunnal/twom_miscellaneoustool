package com.jinproject.domain.usecase.alarm

import com.jinproject.domain.model.TimerModel
import com.jinproject.domain.repository.DropListRepository
import com.jinproject.domain.repository.TimerRepository
import com.jinproject.twomillustratedbook.utils.day
import com.jinproject.twomillustratedbook.utils.hour
import com.jinproject.twomillustratedbook.utils.minute
import com.jinproject.twomillustratedbook.utils.second
import kotlinx.coroutines.flow.zip
import java.util.Calendar
import javax.inject.Inject

class SetAlarmUsecase @Inject constructor(
    private val dropListRepository: DropListRepository,
    private val timerRepository: TimerRepository
) {
    data class MonsterAlarmModel(
        val code: Int,
        val img: String,
        val name: String,
        val gtime: Int,
        val nextGtime: Long
    )
    operator fun invoke(
        monsterName: String,
        monsDiedHour: Int,
        monsDiedMin: Int,
        monsDiedSec: Int,
        makeAlarm: (Int, Int, MonsterAlarmModel) -> Unit
    ) =
        dropListRepository.getMonsInfo(monsterName)
            .zip(timerRepository.getTimer()) { monsterModel, timerModels ->
                val genTime = Calendar.getInstance().apply {
                    hour = monsDiedHour
                    minute = monsDiedMin
                    second = monsDiedSec
                }.timeInMillis + (monsterModel.genTime * 1000).toLong()

                val cal = Calendar.getInstance().apply {
                    timeInMillis = genTime
                }

                val timer = timerModels.find { timerModel ->
                    timerModel.bossName == monsterName
                }

                val code = timer?.id
                    ?: if (timerModels.isNotEmpty()) timerModels.maxOf { item -> item.id } + 1 else 1

                when (timer) {
                    is TimerModel -> {
                        timerRepository.updateTimer(
                            id = code,
                            day = cal.day,
                            hour = cal.hour,
                            min = cal.minute,
                            sec = cal.second
                        )
                    }

                    null -> {
                        timerRepository.setTimer(
                            id = code,
                            day = com.jinproject.domain.model.WeekModel.findByCode(cal.day)
                                .getCodeByWeek(),
                            hour = cal.hour,
                            min = cal.minute,
                            sec = cal.second,
                            bossName = monsterName
                        )
                    }
                }
                MonsterAlarmModel(
                    code = code,
                    img = monsterModel.imgName,
                    name = monsterName,
                    gtime = monsterModel.genTime,
                    nextGtime = genTime
                )
            }.zip(timerRepository.getTimerPreferences()) { monsterAlarmModel, prefs ->
                makeAlarm(
                    prefs.intervalFirstTimerSetting,
                    prefs.intervalSecondTimerSetting,
                    monsterAlarmModel
                )
            }
}