package com.jinproject.domain.usecase.timer

import com.jinproject.domain.repository.TimerRepository
import javax.inject.Inject

class SetIntervalSettingUsecase @Inject constructor(
    private val timerRepository: TimerRepository
) {
    suspend operator fun invoke(first: Int, second: Int) {
        timerRepository.setIntervalTimerSetting(first, second)
    }
}