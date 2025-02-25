package com.jinproject.domain.usecase.timer

import com.jinproject.domain.repository.TimerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class Interval(
    val first: Int,
    val second: Int
)

class GetIntervalUsecase @Inject constructor(
    private val timerRepository: TimerRepository
) {
    operator fun invoke(): Flow<Interval> = timerRepository.getInterval()
}