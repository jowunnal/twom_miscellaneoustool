package com.jinproject.data.datasource.cache

import android.net.Uri
import androidx.datastore.core.DataStore
import com.jinproject.data.ChatMessage
import com.jinproject.data.CollectionPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.transform
import java.io.IOException
import javax.inject.Inject

class CollectionDataStorePreferences @Inject constructor(
    private val dataStorePrefs: DataStore<CollectionPreferences>
) {

    private val collectionPreferences: Flow<CollectionPreferences> = dataStorePrefs.data
        .catch { exception ->
            if (exception is IOException) {
                emit(CollectionPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }

    fun getFilteringCollectionList(): Flow<List<Int>> =
        collectionPreferences.transform { prefs -> emit(prefs.filteredCollectionListList) }


    suspend fun setFilteringCollectionList(collectionList: List<Int>) {
        dataStorePrefs.updateData { prefs ->
            prefs.toBuilder()
                .clearFilteredCollectionList()
                .addAllFilteredCollectionList(collectionList)
                .build()
        }
    }

    suspend fun setSymbolUri(uri: Uri) {
        dataStorePrefs.updateData { prefs ->
            prefs.toBuilder().addStoredSymbolUri(uri.toString()).build()
        }
    }

    fun getSymbolUri(): Flow<List<String>> =
        collectionPreferences.transform { prefs ->
            emit(prefs.storedSymbolUriList)
        }

    fun getChatMessage(): Flow<List<ChatMessage>> =
        collectionPreferences.transform { prefs -> emit(prefs.messageList) }

    suspend fun addChat(message: ChatMessage) {
        dataStorePrefs.updateData { prefs ->
            prefs.toBuilder()
                .addMessage(message)
                .build()
        }
    }

    suspend fun replaceChatMessage(url: String, timeStamp: Long) {
        dataStorePrefs.updateData { prefs ->
            val idx = prefs.messageList.indexOfLast { it.timestamp == timeStamp }

            prefs.toBuilder()
                .setMessage(idx, prefs.messageList[idx].toBuilder().setData(url))
                .build()
        }
    }
}