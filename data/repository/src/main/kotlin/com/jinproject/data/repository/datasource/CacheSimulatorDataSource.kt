package com.jinproject.data.repository.datasource

import com.jinproject.data.repository.model.Equipment
import com.jinproject.data.repository.model.EquipmentEntity
import com.jinproject.data.repository.model.EquipmentInfo
import kotlinx.coroutines.flow.Flow

interface CacheSimulatorDataSource {
    fun getItemInfo(itemName: String): Flow<EquipmentEntity>
    fun getItemInfos(): Flow<List<EquipmentEntity>>
    fun getOwnedItems(): Flow<List<EquipmentInfo>>
    suspend fun addItemOnOwnedItemList(equipment: Equipment)
    suspend fun removeItemOnOwnedItemList(uuid: String)
    suspend fun replaceOwnedItem(equipment: Equipment)

    suspend fun updateOwnedItems(items: List<Equipment>)
}
