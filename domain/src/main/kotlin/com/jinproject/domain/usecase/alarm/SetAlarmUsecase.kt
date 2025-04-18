package com.jinproject.domain.usecase.alarm

import com.jinproject.domain.model.TimerModel
import com.jinproject.domain.repository.DropListRepository
import com.jinproject.domain.repository.TimerRepository
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

    var Calendar.day
        get() = get(Calendar.DAY_OF_WEEK)
        set(value) {
            set(Calendar.DAY_OF_WEEK, value)
        }

    var Calendar.hour
        get() = get(Calendar.HOUR_OF_DAY)
        set(value) {
            set(Calendar.HOUR_OF_DAY, value)
        }

    var Calendar.minute
        get() = get(Calendar.MINUTE)
        set(value) {
            set(Calendar.MINUTE, value)
        }

    var Calendar.second
        get() = get(Calendar.SECOND)
        set(value) {
            set(Calendar.SECOND, value)
        }

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
            }.zip(timerRepository.getInterval()) { monsterAlarmModel, interval ->
                makeAlarm(
                    interval.first,
                    interval.second,
                    monsterAlarmModel
                )
            }
}