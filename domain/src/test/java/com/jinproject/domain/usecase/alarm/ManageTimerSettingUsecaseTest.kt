package com.jinproject.domain.usecase.alarm

import com.jinproject.domain.usecase.alarm.ManageTimerSettingUsecase.TimerInterval
import com.jinproject.domain.usecase.alarm.ManageTimerSettingUsecase.TimerSetting
import com.jinproject.domain.usecase.repository.FakeTimerRepository
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest

class ManageTimerSettingUsecaseTest : BehaviorSpec() {
    private val timerRepository = FakeTimerRepository()
    private val manageTimerSettingUsecase = ManageTimerSettingUsecase(timerRepository)

    init {
        runTest {
            given("알람 간격이 첫번째 0분, 두번째 0분으로 설정된 상황에서") {
                timerRepository.timerSetting =
                    TimerSetting(interval = TimerInterval(firstInterval = 0, secondInterval = 0))

                `when`("첫번째 알람 간격이 5분이고, 두번째 알람 간격이 10분일 때, 알람 설정을 하면") {
                    val timerInterval = TimerInterval(firstInterval = 5, secondInterval = 10)

                    then("InvalidIntervalException 예외가 발생한다.") {
                        shouldThrowExactly<ManageTimerSettingUsecase.ManageTimerSettingException.InvalidIntervalException> {
                            manageTimerSettingUsecase.updateTimerSetting(
                                TimerSetting(interval = timerInterval)
                            )
                        }
                        timerRepository.timerSetting shouldBe TimerSetting(
                            interval = TimerInterval(
                                firstInterval = 0,
                                secondInterval = 0
                            )
                        )
                    }
                }

                `when`("첫번째 알람 간격이 5분이고, 두번째 알람 간격이 null 일 때, 알람 설정을 하면") {
                    val timerInterval = TimerInterval(firstInterval = 5, secondInterval = null)
                    val result =
                        manageTimerSettingUsecase.updateTimerSetting(TimerSetting(interval = timerInterval))

                    then("첫번째 알람 간격은 5분, 두번째는 null 분으로 설정된다.") {
                        result shouldBe Unit
                        timerRepository.timerSetting shouldBe TimerSetting(
                            interval = timerInterval.copy(
                                secondInterval = null
                            )
                        )
                    }
                }

                `when`("첫번째 알람 간격이 null 이고, 두번째 알람 간격이 5 일 때, 알람 설정을 하면") {
                    val timerInterval = TimerInterval(firstInterval = null, secondInterval = 5)

                    then("기본값 (0,0) 에 두번째 알람 간격 5분이 설정되면서 (0,5) 로 인해 InvalidIntervalException 예외가 발생한다.") {
                        shouldThrowExactly<ManageTimerSettingUsecase.ManageTimerSettingException.InvalidIntervalException> {
                            manageTimerSettingUsecase.updateTimerSetting(
                                TimerSetting(interval = timerInterval)
                            )
                        }
                    }
                }

                `when`("첫번째 알람 간격이 10분이고, 두번째 알람 간격이 5분일 때, 알람 설정을 하면") {
                    val timerInterval = TimerInterval(firstInterval = 10, secondInterval = 5)
                    val result =
                        manageTimerSettingUsecase.updateTimerSetting(TimerSetting(interval = timerInterval))

                    then("첫번째 알람 간격은 10분, 두번째는 5분으로 설정된다.") {
                        result shouldBe Unit
                        timerRepository.timerSetting shouldBe TimerSetting(interval = timerInterval)
                    }
                }
            }
        }
    }
}