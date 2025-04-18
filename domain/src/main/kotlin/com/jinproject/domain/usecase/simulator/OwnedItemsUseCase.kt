package com.jinproject.domain.usecase.simulator

import com.jinproject.domain.entity.item.EnchantableEquipment
import com.jinproject.domain.entity.item.Equipment
import com.jinproject.domain.repository.SimulatorRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OwnedItemsUseCase @Inject constructor(
    private val simulatorRepository: SimulatorRepository
) {
    suspend fun add(equipment: EnchantableEquipment) {
        val map = mutableMapOf<String, Float>().apply {
            equipment.stats.forEach { s ->
                put(
                    s.key,
                    (0..13).random().toFloat()
                )
            }
        }

        val new = when (equipment) {
            is com.jinproject.domain.entity.item.Weapon -> equipment.copy(stats = map)
            is com.jinproject.domain.entity.item.Armor -> equipment.copy(stats = map)
            is com.jinproject.domain.entity.item.Accessory -> equipment.copy(stats = map)
            else -> throw OwnedItemManagingFailedException.NotAllowedEquipment("지원하지 않는 장비입니다.")
        }

        simulatorRepository.addItemOnOwnedItemList(new)
    }

    suspend fun remove(uuid: String) = simulatorRepository.removeItemOnOwnedItemList(uuid)

    suspend fun replace(equipment: EnchantableEquipment) =
        simulatorRepository.replaceOwnedItem(equipment)

    fun getItems(): Flow<List<Equipment>> = simulatorRepository.getOwnedItems()

    suspend fun update(items: List<EnchantableEquipment>) {
        simulatorRepository.updateOwnedItem(items)
    }

    sealed class OwnedItemManagingFailedException : Exception() {
        data class NotAllowedEquipment(override val message: String) :
            OwnedItemManagingFailedException()
    }
}