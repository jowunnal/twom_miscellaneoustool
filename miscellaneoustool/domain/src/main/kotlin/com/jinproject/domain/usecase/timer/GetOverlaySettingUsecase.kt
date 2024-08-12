package com.jinproject.domain.usecase.timer

import com.jinproject.domain.repository.TimerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class OverlaySetting(
    val fontSize: Int,
    val xPos: Int,
    val yPos: Int,
    val frequentlyUsedBossList: List<String>
)

class GetOverlaySettingUsecase @Inject constructor(
    private val timerRepository: TimerRepository
) {
    operator fun invoke(): Flow<OverlaySetting> =
        timerRepository.getOverlaySetting()
}