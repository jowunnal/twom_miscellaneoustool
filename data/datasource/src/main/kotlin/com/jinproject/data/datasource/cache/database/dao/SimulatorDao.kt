package com.jinproject.data.datasource.cache.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.jinproject.data.datasource.cache.database.entity.ItemInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface SimulatorDao {

    @Transaction
    @Query(
        """
        select * 
        from Item 
        inner join ItemInfo on ItemInfo.item_name = Item.itemName
        inner join Equipment on Item.itemName = Equipment.name
        where Item.itemName = :itemName
    """
    )
    fun getItemInfo(itemName: String): Flow<Map<ItemWithEquipmentInfo, List<ItemInfo>>>

    @Transaction
    @Query(
        """
        select * 
        from Item 
        inner join ItemInfo on ItemInfo.item_name = Item.itemName
        inner join Equipment on Item.itemName = Equipment.name
    """
    )
    fun getItemInfos(): Flow<Map<ItemWithEquipmentInfo, List<ItemInfo>>>
}

data class ItemWithEquipmentInfo(
    val itemName: String,
    val itemType: String,
    val itemPrice: Long,
    val level: Int,
    val img_name: String
)