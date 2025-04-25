package com.jinproject.data.datasource.cache

import androidx.datastore.core.DataStore
import com.jinproject.data.CollectionPreferences
import com.jinproject.data.datasource.cache.database.dao.CollectionDao
import com.jinproject.data.repository.datasource.CacheCollectionDataSource
import com.jinproject.data.repository.model.CollectionModel
import com.jinproject.data.repository.model.GetItemDomainFactory
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
        data.map { it.filteredCollectionListList }

    override suspend fun setFilteringCollectionList(collectionList: List<Int>) {
        prefs.updateData { prefs ->
            prefs.toBuilder()
                .clearFilteredCollectionList()
                .addAllFilteredCollectionList(collectionList)
                .build()
        }
    }

    override suspend fun addFilteringCollection(id: Int) {
        prefs.updateData { prefs ->
            prefs.toBuilder()
                .addFilteredCollectionList(id)
                .build()
        }
    }

    override suspend fun updateItemPrice(name: String, price: Long) {
        collectionDao.updateItemPrice(name = name, price = price)
    }

    override fun getCollectionItems(): Flow<List<CollectionModel>> =
        collectionDao.getCollectionItems().map { collectionItems ->
            collectionItems.map { collectionItem ->
                CollectionModel(
                    bookId = collectionItem.book.bookId,
                    stat = collectionItem.stats.associate { stat -> stat.type to stat.value.toFloat() },
                    items = collectionItem.items.flatMap { item ->
                        mutableListOf<GetItemDomainFactory>().apply {
                            repeat(item.registerItemToBook.rlItemCount) {
                                add(item.item.toItemData(enchantNumber = item.registerItemToBook.rlItemEnchant))
                            }
                        }
                    }
                )
            }
        }

}