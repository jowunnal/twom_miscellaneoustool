package com.jinproject.data.repository

import com.jinproject.data.datasource.cache.CollectionDataStorePreferences
import com.jinproject.data.datasource.cache.database.dao.CollectionDao
import com.jinproject.domain.model.CollectionModel
import com.jinproject.domain.model.ItemModel
import com.jinproject.domain.model.ItemType
import com.jinproject.domain.model.Stat
import com.jinproject.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CollectionRepositoryImpl @Inject constructor(
    private val collectionDao: CollectionDao,
    private val collectionDataStorePreferences: CollectionDataStorePreferences
) : CollectionRepository {

    override fun getCollectionList(): Flow<List<CollectionModel>> =
        collectionDao.getCollectionItems().map { collectionItems ->
            collectionItems.map { collectionItem ->
                CollectionModel(
                    bookId = collectionItem.book.bookId,
                    stat = collectionItem.stats.associate { stat -> Stat.valueOf(stat.type) to stat.value },
                    items = collectionItem.items.map { item -> ItemModel(
                        name = item.item.itemName,
                        count = item.registerItemToBook.rlItemCount,
                        enchantNumber = item.registerItemToBook.rlItemEnchant,
                        price = item.item.itemPrice,
                        type = ItemType.findByStoredName(item.item.itemType)
                    ) }
                )
            }
        }

    override fun getFilteredCollectionIds(): Flow<Set<Int>> =
        collectionDataStorePreferences.getFilteringCollectionList().map { it.toSet() }


    override suspend fun setFilteringCollections(collectionList: Set<Int>) {
        collectionDataStorePreferences.setFilteringCollectionList(collectionList.toList())
    }

    override suspend fun updateItemPrice(items: List<ItemModel>) {
        items.forEach { item ->
            collectionDao.updateItemPrice(name = item.name, price = item.price)
        }
    }
}