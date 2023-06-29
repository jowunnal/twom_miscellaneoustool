package com.miscellaneoustool.app.domain.repository

import com.miscellaneoustool.app.domain.model.Category
import com.miscellaneoustool.app.domain.model.CollectionModel
import com.miscellaneoustool.app.domain.model.ItemModel
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {
    fun getCollectionList(category: Category, filter: Boolean): Flow<List<CollectionModel>>

    suspend fun deleteCollection(collectionList: List<Int>)

    fun getItems(): Flow<List<ItemModel>>

    suspend fun updateItemPrice(name: String, price: Int)

    suspend fun deleteFilter(id: Int)

}