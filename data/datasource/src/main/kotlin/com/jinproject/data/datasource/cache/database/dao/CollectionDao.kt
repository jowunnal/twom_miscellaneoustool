package com.jinproject.data.datasource.cache.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.jinproject.data.datasource.CollectionMapping
import com.jinproject.data.datasource.cache.database.entity.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {
    @Transaction
    @Query("select * from Book")
    fun getCollectionItems(): Flow<List<CollectionMapping>>

    @Query("select * from Item order by Item.itemType desc")
    fun getItems(): Flow<List<Item>>

    @Query("update Item set itemPrice = :price where itemName like :name")
    suspend fun updateItemPrice(name: String, price: Long)

}

