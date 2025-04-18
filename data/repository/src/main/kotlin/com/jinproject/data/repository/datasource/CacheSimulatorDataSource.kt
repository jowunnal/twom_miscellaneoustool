package com.jinproject.data.repository.datasource

import com.jinproject.data.repository.model.Equipment
import kotlinx.coroutines.flow.Flow

interface CacheSimulatorDataSource {
    fun getItemInfo(itemName: String): Flow<Equipment>
    fun getItemInfos(): Flow<List<Equipment>>
    fun getOwnedItems(): Flow<List<Equipment>>
    suspend fun addItemOnOwnedItemList(equipment: Equipment)
    suspend fun removeItemOnOwnedItemList(uuid: String)
    suspend fun replaceOwnedItem(equipment: Equipment)

    suspend fun updateOwnedItems(items: List<Equipment>)
}
