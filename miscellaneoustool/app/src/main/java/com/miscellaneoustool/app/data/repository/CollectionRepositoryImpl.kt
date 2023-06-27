package com.miscellaneoustool.app.data.repository

import com.miscellaneoustool.app.data.database.dao.CollectionDao
import com.miscellaneoustool.app.data.mapper.fromItemsWithStatsToCollectionModel
import com.miscellaneoustool.app.domain.model.Category
import com.miscellaneoustool.app.domain.model.CollectionModel
import com.miscellaneoustool.app.domain.repository.CollectionRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CollectionRepositoryImpl @Inject constructor(
    private val collectionDao: CollectionDao
) : CollectionRepository {

    @OptIn(FlowPreview::class)
    override fun getCollectionList(category: Category): Flow<List<CollectionModel>> =
        collectionDao.getCollectionItems(category.storedName).flatMapConcat { items ->
            collectionDao.getCollectionStats(items.keys.map { it.bookId }).map { stats ->
                fromItemsWithStatsToCollectionModel(items, stats)
            }
        }

}