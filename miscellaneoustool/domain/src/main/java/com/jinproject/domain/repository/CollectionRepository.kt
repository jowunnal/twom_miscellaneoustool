package com.jinproject.domain.repository

import android.net.Uri
import com.jinproject.domain.model.Category
import com.jinproject.domain.model.CollectionModel
import com.jinproject.domain.model.ItemModel
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {
    fun getCollectionList(category: Category, filter: Boolean): Flow<List<CollectionModel>>

    suspend fun deleteCollection(collectionList: List<Int>)

    fun getItems(): Flow<List<ItemModel>>

    suspend fun updateItemPrice(name: String, price: Int)

    suspend fun deleteFilter(id: Int)

    suspend fun setSymbolUri(uri: Uri)

    fun getSymbolUri(): Flow<List<String>>

}