package com.jinproject.data.repository.repo

import com.jinproject.data.repository.datasource.CacheCollectionDataSource
import com.jinproject.domain.entity.ItemCollection
import com.jinproject.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CollectionRepositoryImpl @Inject constructor(
    private val cacheCollectionDataSource: CacheCollectionDataSource
) : CollectionRepository {

    override fun getCollectionList(): Flow<List<ItemCollection>> =
        cacheCollectionDataSource.getCollectionItems().map { collectionModels ->
            collectionModels.map { it.toCollectionDomain() }
        }

    override fun getFilteredCollectionIds(): Flow<List<Int>> =
        cacheCollectionDataSource.getFilteringCollectionList()


    override suspend fun setFilteringCollections(collectionList: List<Int>) {
        cacheCollectionDataSource.setFilteringCollectionList(collectionList)
    }

    override suspend fun addFilteringCollection(id: Int) {
        cacheCollectionDataSource.addFilteringCollection(id)
    }

    override suspend fun updateItemPrice(name: String, price: Long) {
        cacheCollectionDataSource.updateItemPrice(name = name, price = price)
    }
}