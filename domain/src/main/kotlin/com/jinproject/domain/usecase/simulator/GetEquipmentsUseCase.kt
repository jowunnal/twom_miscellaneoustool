package com.jinproject.domain.usecase.simulator

import com.jinproject.domain.entity.item.Equipment
import com.jinproject.domain.repository.SimulatorRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEquipmentsUseCase @Inject constructor(
    private val simulatorRepository: SimulatorRepository,
) {
    operator fun invoke(): Flow<List<Equipment>> = simulatorRepository.getEnchantableItems()
}