package com.jinproject.domain.repository

import com.jinproject.domain.model.ItemInfo
import kotlinx.coroutines.flow.Flow

interface SimulatorRepository {
    fun getItemInfo(itemName: String): Flow<ItemInfo>
    fun getAvailableItem(): Flow<List<ItemInfo>>
    fun getOwnedItems(): Flow<List<ItemInfo>>
    suspend fun addItemOnOwnedItemList(itemInfo: ItemInfo)
    suspend fun removeItemOnOwnedItemList(uuid: String)
    suspend fun replaceOwnedItem(item: ItemInfo)
}