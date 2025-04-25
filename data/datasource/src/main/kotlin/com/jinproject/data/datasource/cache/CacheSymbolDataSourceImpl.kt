package com.jinproject.data.datasource.cache

import androidx.datastore.core.DataStore
import com.jinproject.data.CollectionPreferences
import com.jinproject.data.datasource.cache.mapper.toChatMessage
import com.jinproject.data.datasource.cache.mapper.toDataModels
import com.jinproject.data.repository.datasource.CacheSymbolDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.transform
import java.io.IOException
import javax.inject.Inject

class CacheSymbolDataSourceImpl @Inject constructor(
    val prefs: DataStore<CollectionPreferences>,
) : CacheSymbolDataSource {

    val data: Flow<CollectionPreferences> = prefs.data
        .catch { exception ->
            if (exception is IOException) {
                emit(CollectionPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }

    override fun getChatMessage(): Flow<List<com.jinproject.data.repository.model.ChatMessage>> =
        data.transform { prefs -> emit(prefs.messageList.toDataModels()) }

    override suspend fun addChat(message: com.jinproject.data.repository.model.ChatMessage) {
        prefs.updateData { prefs ->
            prefs.toBuilder()
                .addMessage(message.toChatMessage())
                .build()
        }
    }

    override suspend fun replaceChatMessage(url: String, timeStamp: Long) {
        prefs.updateData { prefs ->
            val idx = prefs.messageList.indexOfLast { it.timestamp == timeStamp }

            prefs.toBuilder()
                .setMessage(idx, prefs.messageList[idx].toBuilder().setData(url))
                .build()
        }
    }

    override suspend fun addPaidSymbol(uri: String) {
        prefs.updateData { prefs ->
            prefs.toBuilder().addStoredSymbolUri(uri).build()
        }
    }

    override fun getPaidSymbolUris(): Flow<List<String>> =
        data.transform { prefs ->
            emit(prefs.storedSymbolUriList)
        }

}