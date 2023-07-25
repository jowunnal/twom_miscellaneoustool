package com.jinproject.domain.usecase.timer

import com.jinproject.domain.repository.TimerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class AlarmStoredBoss(
    val list: List<String>,
    val classified: String,
    val name: String
)

class GetAlarmStoredBossUsecase @Inject constructor(
    private val timerRepository: TimerRepository
) {
    operator fun invoke(): Flow<AlarmStoredBoss> = timerRepository.getAlarmStoredBoss()
}