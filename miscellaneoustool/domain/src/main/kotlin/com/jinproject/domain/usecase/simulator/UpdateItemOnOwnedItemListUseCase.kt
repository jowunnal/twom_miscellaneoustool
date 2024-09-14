package com.jinproject.domain.usecase.simulator

import com.jinproject.domain.model.ItemInfo
import com.jinproject.domain.repository.SimulatorRepository
import javax.inject.Inject

class UpdateItemOnOwnedItemListUseCase @Inject constructor(
    private val simulatorRepository: SimulatorRepository
) {
    suspend fun add(itemInfo: ItemInfo) = simulatorRepository.addItemOnOwnedItemList(itemInfo)
    suspend fun remove(uuid: String) = simulatorRepository.removeItemOnOwnedItemList(uuid)
    suspend fun replace(item: ItemInfo) = simulatorRepository.replaceOwnedItem(item)
}