package com.jinproject.data.repository.datasource

import com.jinproject.data.repository.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface CacheSymbolDataSource {
    suspend fun addPaidSymbol(uri: String)
    fun getPaidSymbolUris(): Flow<List<String>>
    fun getChatMessage(): Flow<List<ChatMessage>>
    suspend fun addChat(message: ChatMessage)
    suspend fun replaceChatMessage(url: String, timeStamp: Long)
}