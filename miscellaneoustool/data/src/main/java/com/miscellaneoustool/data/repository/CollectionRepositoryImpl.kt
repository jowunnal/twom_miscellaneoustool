package com.miscellaneoustool.data.repository

import com.miscellaneoustool.data.datasource.cache.CacheCollectionDataSource
import com.miscellaneoustool.data.datasource.cache.database.dao.CollectionDao
import com.miscellaneoustool.data.mapper.fromItemsToItemModel
import com.miscellaneoustool.data.mapper.fromItemsWithStatsToCollectionModel
import com.miscellaneoustool.domain.model.Category
import com.miscellaneoustool.domain.model.CollectionModel
import com.miscellaneoustool.domain.model.ItemModel
import com.miscellaneoustool.domain.repository.CollectionRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

class CollectionRepositoryImpl @Inject constructor(
    private val collectionDao: CollectionDao,
    private val cacheCollectionDataSource: CacheCollectionDataSource
) : CollectionRepository {

    @OptIn(FlowPreview::class)
    override fun getCollectionList(category: Category, filter: Boolean): Flow<List<CollectionModel>> =
        collectionDao.getCollectionItems(category.storedName).flatMapConcat { items ->
            collectionDao.getCollectionStats(items.keys.map { it.bookId }).map { stats ->
                fromItemsWithStatsToCollectionModel(items, stats)
            }
        }.zip(cacheCollectionDataSource.getFilteringCollectionList()) { collectionModels, filterList ->
            val list = filterList.toMutableList()

            collectionModels.filter { collectionModel ->
                if(collectionModel.bookId in list) {
                    list.remove(collectionModel.bookId)
                    !filter
                } else
                    filter
            }
        }

    override suspend fun deleteCollection(collectionList: List<Int>) {
        cacheCollectionDataSource.setFilteringCollectionList(collectionList)
    }

    override fun getItems(): Flow<List<ItemModel>> =
        collectionDao.getItems().map { items ->
            fromItemsToItemModel(items)
        }

    override suspend fun updateItemPrice(name: String, price: Int) {
        collectionDao.updateItemPrice(name = name, price = price)
    }

    override suspend fun deleteFilter(id: Int) {
        cacheCollectionDataSource.deleteFilter(id)
    }

}