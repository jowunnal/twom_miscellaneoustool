package com.jinproject.data.datasource.cache.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import com.jinproject.data.datasource.cache.database.entity.Equipment
import com.jinproject.data.datasource.cache.database.entity.ItemInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface SimulatorDao {

    @RewriteQueriesToDropUnusedColumns
    @Query("select * from Equipment inner join ItemInfo on ItemInfo.item_name = Equipment.name where ItemInfo.item_name like:itemName")
    fun getItemInfo(itemName: String): Flow<Map<Equipment, List<ItemInfo>>>

    @Query("select DISTINCT name, level, img_name from ItemInfo inner join Equipment on ItemInfo.item_name = Equipment.name")
    fun getAvailableItem(): Flow<List<Equipment>>
}