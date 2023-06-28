package com.miscellaneoustool.app.data.repository

import com.miscellaneoustool.app.data.datasource.cache.CacheCollectionDataSource
import com.miscellaneoustool.app.data.datasource.cache.database.dao.CollectionDao
import com.miscellaneoustool.app.data.mapper.fromItemsWithStatsToCollectionModel
import com.miscellaneoustool.app.domain.model.Category
import com.miscellaneoustool.app.domain.model.CollectionModel
import com.miscellaneoustool.app.domain.repository.CollectionRepository
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
    override fun getCollectionList(category: Category): Flow<List<CollectionModel>> =
        collectionDao.getCollectionItems(category.storedName).flatMapConcat { items ->
            collectionDao.getCollectionStats(items.keys.map { it.bookId }).map { stats ->
                fromItemsWithStatsToCollectionModel(items, stats)
            }
        }.zip(cacheCollectionDataSource.getFilteringCollectionList()) { collectionModels, filterList ->
            val list = filterList.toMutableList()

            collectionModels.filter { collectionModel ->
                if(collectionModel.bookId in list) {
                    list.remove(collectionModel.bookId)
                    false
                } else
                    true
            }
        }

    override suspend fun deleteCollection(collectionList: List<Int>) {
        cacheCollectionDataSource.setFilteringCollectionList(collectionList)
    }

}