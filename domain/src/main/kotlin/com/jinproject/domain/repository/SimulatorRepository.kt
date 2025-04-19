package com.jinproject.domain.repository

import com.jinproject.domain.entity.item.EnchantableEquipment
import com.jinproject.domain.entity.item.Equipment
import kotlinx.coroutines.flow.Flow

interface SimulatorRepository {
    fun getEnchantableItems(): Flow<List<Equipment>>
    fun getOwnedItems(): Flow<List<Equipment>>
    suspend fun addItemOnOwnedItemList(equipment: EnchantableEquipment)
    suspend fun removeItemOnOwnedItemList(uuid: String)
    suspend fun replaceOwnedItem(equipment: EnchantableEquipment)

    suspend fun updateOwnedItem(items: List<EnchantableEquipment>)
}