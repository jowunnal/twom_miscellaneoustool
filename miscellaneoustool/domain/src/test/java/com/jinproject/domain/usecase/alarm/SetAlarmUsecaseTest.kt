package com.jinproject.domain.usecase.alarm

import com.jinproject.core.util.day
import com.jinproject.core.util.hour
import com.jinproject.core.util.minute
import com.jinproject.core.util.second
import com.jinproject.domain.model.MonsterModel
import com.jinproject.domain.model.MonsterType
import com.jinproject.domain.model.TimerModel
import com.jinproject.domain.repository.DropListRepository
import com.jinproject.domain.repository.TimerRepository
import com.jinproject.domain.usecase.timer.Interval
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.reflection.compose
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import java.util.Calendar

@ExperimentalCoroutinesApi
class SetAlarmUsecaseTest : BehaviorSpec() {
    private val timerRepository: TimerRepository = mockk(relaxed = true)
    private val dropListRepository: DropListRepository = mockk(relaxed = true)
    private val setAlarmUsecase: SetAlarmUsecase = SetAlarmUsecase(
        dropListRepository = dropListRepository,
        timerRepository = timerRepository
    )

    init {
        runTest {
            given("몬스터 알람을 설정하는 상황에서") {
                val cal = Calendar.getInstance()
                val monsterName = "불도저"
                var monsterAlarmModel: SetAlarmUsecase.MonsterAlarmModel? = null
                var firstInter: Int? = null
                var secondInter: Int? = null

                and("몬스터가 불도저 이고, 알람주기는 각각 0분이며,") {
                    every { dropListRepository.getMonsInfo(monsterName) } returns flow {
                        emit(
                            MonsterModel(
                                name = "불도저",
                                level = 7,
                                genTime = 900,
                                imgName = "bulldozer",
                                type = MonsterType.NAMED,
                                item = emptyList()
                            )
                        )
                    }

                    every { timerRepository.getInterval() } returns flow {
                        emit(Interval(first = 0, second = 0))
                    }

                    and("이전에 등록한 알람이 있었을 때") {
                        every { timerRepository.getTimer() } returns flow {
                            emit(listOf(
                                TimerModel.getInitValue()
                            ))
                        }

                        `when`("알람을 설정 하면") {
                            setAlarmUsecase(
                                monsterName = monsterName,
                                monsDiedHour = cal.hour,
                                monsDiedMin = cal.minute,
                                monsDiedSec = cal.second,
                                makeAlarm = { firstInterval, secondInterval, monsAlarmModel ->
                                    firstInter = firstInterval
                                    secondInter = secondInterval
                                    monsterAlarmModel = monsAlarmModel
                                }
                            ).collect()

                            then("알람 설정이 정상적으로 수행된다.") {
                                coVerify(exactly = 1) {
                                    dropListRepository.getMonsInfo(monsterName)
                                    timerRepository.getTimer()
                                    timerRepository.getInterval()
                                    setAlarmUsecase.invoke(
                                        monsterName = monsterName,
                                        monsDiedHour = cal.hour,
                                        monsDiedMin = cal.minute,
                                        monsDiedSec = cal.second,
                                        makeAlarm = { firstInerval, secondInterval, monsAlarmModel ->
                                        }
                                    )
                                }
                                firstInter shouldBe 0
                                secondInter shouldBe 0
                                monsterAlarmModel shouldBe alarmMatch(monsterAlarmModel!!)

                            }
                        }
                    }

                    xand("이전에 등록한 알람이 없었을 때") {
                        every { timerRepository.getTimer() } returns flow {
                            emit(emptyList())
                        }

                        `when`("알람을 설정 하면") {
                            setAlarmUsecase(
                                monsterName = monsterName,
                                monsDiedHour = cal.hour,
                                monsDiedMin = cal.minute,
                                monsDiedSec = cal.second,
                                makeAlarm = { firstInterval, secondInterval, monsAlarmModel ->
                                    firstInter = firstInterval
                                    secondInter = secondInterval
                                    monsterAlarmModel = monsAlarmModel
                                }
                            ).collect()

                            then("알람 설정이 정상적으로 수행된다.") {
                                coVerify(exactly = 1) {
                                    dropListRepository.getMonsInfo(monsterName)
                                    timerRepository.getTimer()
                                    timerRepository.getInterval()
                                    setAlarmUsecase.invoke(
                                        monsterName = monsterName,
                                        monsDiedHour = cal.hour,
                                        monsDiedMin = cal.minute,
                                        monsDiedSec = cal.second,
                                        makeAlarm = { firstInerval, secondInterval, monsAlarmModel ->
                                        }
                                    )
                                }
                                firstInter shouldBe 0
                                secondInter shouldBe 0
                                monsterAlarmModel shouldBe alarmMatch(monsterAlarmModel!!)

                            }
                        }
                    }
                }
            }
        }
    }

    private fun isValueMatchAnswer(value: Int, answer: Int) = Matcher<Int> {
        MatcherResult(
            value == answer,
            { "$value should be $answer" },
            { "$value should not be $answer" }
        )
    }


    private fun isValueMatchAnswer(value: String, answer: String) = Matcher<String> {
        MatcherResult(
            value == answer,
            { "$value should be $answer" },
            { "$value should not be $answer" }
        )
    }

    private fun isValueMatchAnswer(value: Long) = Matcher<Long> {
        val calValue = Calendar.getInstance().apply { timeInMillis = value }
        val calAnswer = Calendar.getInstance().apply {
            minute += 15
        }
        MatcherResult(
            calValue.day == calAnswer.day && calValue.hour == calAnswer.hour && calValue.minute == calAnswer.minute,
            { "$value should be ${calAnswer.timeInMillis}" },
            { "$value should not be ${calAnswer.timeInMillis}" }
        )
    }

    private fun alarmMatch(monsAlarmModel: SetAlarmUsecase.MonsterAlarmModel) = Matcher.compose(
        isValueMatchAnswer(monsAlarmModel.code, 1) to SetAlarmUsecase.MonsterAlarmModel::code,
        isValueMatchAnswer(monsAlarmModel.gtime, 900) to SetAlarmUsecase.MonsterAlarmModel::gtime,
        isValueMatchAnswer(monsAlarmModel.img, "bulldozer") to SetAlarmUsecase.MonsterAlarmModel::img,
        isValueMatchAnswer(monsAlarmModel.name, "불도저") to SetAlarmUsecase.MonsterAlarmModel::name,
        isValueMatchAnswer(monsAlarmModel.nextGtime) to SetAlarmUsecase.MonsterAlarmModel::nextGtime
    )

}