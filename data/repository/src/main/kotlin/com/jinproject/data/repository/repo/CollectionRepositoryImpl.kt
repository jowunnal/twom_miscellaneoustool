package com.jinproject.data.repository.repo

import com.jinproject.data.repository.datasource.CacheCollectionDataSource
import com.jinproject.domain.model.CollectionModel
import com.jinproject.domain.model.ItemModel
import com.jinproject.domain.model.ItemType
import com.jinproject.domain.model.Stat
import com.jinproject.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CollectionRepositoryImpl @Inject constructor(
    private val cacheCollectionDataSource: CacheCollectionDataSource
) : CollectionRepository {

    override fun getCollectionList(): Flow<List<CollectionModel>> =
        cacheCollectionDataSource.getCollectionItems().map { collectionModels ->
            collectionModels.map { collectionModel ->
                CollectionModel(
                    bookId = collectionModel.bookId,
                    stat = collectionModel.stat.mapNotNull { stat ->
                        runCatching {
                            Stat.valueOf(stat.key) to stat.value
                        }.getOrNull()
                    }.toMap(),
                    items = collectionModel.items.mapNotNull { item ->
                        runCatching {
                            ItemModel(
                                name = item.name,
                                count = item.count,
                                enchantNumber = item.enchantNumber,
                                price = item.price,
                                type = ItemType.findByStoredName(item.type)
                            )
                        }.getOrNull()
                    }
                )
            }
        }

    override fun getFilteredCollectionIds(): Flow<Set<Int>> =
        cacheCollectionDataSource.getFilteringCollectionList().map { it.toSet() }


    override suspend fun setFilteringCollections(collectionList: Set<Int>) {
        cacheCollectionDataSource.setFilteringCollectionList(collectionList.toList())
    }

    override suspend fun updateItemPrice(items: List<ItemModel>) {
        items.forEach { item ->
            cacheCollectionDataSource.updateItemPrice(name = item.name, price = item.price)
        }
    }
}