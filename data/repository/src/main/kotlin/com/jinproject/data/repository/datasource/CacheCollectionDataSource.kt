package com.jinproject.data.repository.datasource

import com.jinproject.data.repository.model.ChatMessage
import com.jinproject.data.repository.model.CollectionModel
import kotlinx.coroutines.flow.Flow

interface CacheCollectionDataSource {
    fun getFilteringCollectionList(): Flow<List<Int>>
    suspend fun setFilteringCollectionList(collectionList: List<Int>)
    suspend fun addPaidSymbol(uri: String)
    fun getPaidSymbolUris(): Flow<List<String>>
    fun getChatMessage(): Flow<List<ChatMessage>>
    suspend fun addChat(message: ChatMessage)
    suspend fun replaceChatMessage(url: String, timeStamp: Long)
    suspend fun updateItemPrice(name: String, price: Long)
    fun getCollectionItems(): Flow<List<CollectionModel>>
}
