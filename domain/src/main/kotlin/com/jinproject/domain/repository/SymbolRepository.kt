package com.jinproject.domain.repository

import com.jinproject.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface SymbolRepository {

    suspend fun addPaidSymbol(uri: String)

    fun getPaidSymbolUris(): Flow<List<String>>

    suspend fun generateSymbolImage(prompt: String)

    fun getChatList(): Flow<List<Message>>

    suspend fun addChat(message: Message)

    suspend fun replaceChat(message: Message)

    suspend fun downloadImage(url: String, timeStamp: Long)

}