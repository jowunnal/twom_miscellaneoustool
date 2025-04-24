package com.jinproject.data.datasource.cache

import androidx.datastore.core.DataStore
import com.jinproject.data.CollectionPreferences
import com.jinproject.data.datasource.cache.database.dao.CollectionDao
import com.jinproject.data.datasource.cache.mapper.toChatMessage
import com.jinproject.data.datasource.cache.mapper.toDataModels
import com.jinproject.data.repository.datasource.CacheCollectionDataSource
import com.jinproject.data.repository.model.CollectionModel
import com.jinproject.data.repository.model.ItemModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import java.io.IOException
import javax.inject.Inject

class CacheCollectionDataSourceImpl @Inject constructor(
    val prefs: DataStore<CollectionPreferences>,
    private val collectionDao: CollectionDao,
) : CacheCollectionDataSource {

    val data: Flow<CollectionPreferences> = prefs.data
        .catch { exception ->
            if (exception is IOException) {
                emit(CollectionPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }

    override fun getFilteringCollectionList(): Flow<List<Int>> =
        data.transform { prefs -> emit(prefs.filteredCollectionListList) }


    override suspend fun setFilteringCollectionList(collectionList: List<Int>) {
        prefs.updateData { prefs ->
            prefs.toBuilder()
                .clearFilteredCollectionList()
                .addAllFilteredCollectionList(collectionList)
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

    override suspend fun updateItemPrice(name: String, price: Long) {
        collectionDao.updateItemPrice(name = name, price = price)
    }

    override fun getCollectionItems(): Flow<List<CollectionModel>> = collectionDao.getCollectionItems().map { collectionItems ->
        collectionItems.map { collectionItem ->
            CollectionModel(
                bookId = collectionItem.book.bookId,
                stat = collectionItem.stats.associate { stat -> stat.type to stat.value },
                items = collectionItem.items.map { item ->
                    ItemModel(
                    name = item.item.itemName,
                    count = item.registerItemToBook.rlItemCount,
                    enchantNumber = item.registerItemToBook.rlItemEnchant,
                    price = item.item.itemPrice,
                    type = item.item.itemType,
                ) }
            )
        }
    }
}