package com.jinproject.domain.usecase.simulator

import com.jinproject.domain.model.ItemInfo
import com.jinproject.domain.repository.SimulatorRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAvailableItemInfoUseCase @Inject constructor(
    private val simulatorRepository: SimulatorRepository,
) {
    operator fun invoke(): Flow<List<ItemInfo>> = simulatorRepository.getAvailableItem()
}