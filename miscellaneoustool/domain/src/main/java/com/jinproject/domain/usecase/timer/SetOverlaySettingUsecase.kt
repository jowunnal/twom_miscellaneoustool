package com.jinproject.domain.usecase.timer

import com.jinproject.domain.repository.TimerRepository
import javax.inject.Inject

class SetOverlaySettingUsecase @Inject constructor(
    private val timerRepository: TimerRepository
) {
    suspend operator fun invoke(fontSize: Int, xPos: Int, yPos: Int) {
        timerRepository.setTimerSetting(fontSize, xPos, yPos)
    }
}