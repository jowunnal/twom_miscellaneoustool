package com.miscellaneoustool.data.datasource.cache

import android.system.Os.remove
import androidx.datastore.core.DataStore
import com.miscellaneoustool.data.CollectionPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.transform
import java.io.IOException
import javax.inject.Inject

class CacheCollectionDataSourceImpl @Inject constructor(
    private val dataStorePrefs: DataStore<CollectionPreferences>
) : CacheCollectionDataSource {

    private val collectionPreferences: Flow<CollectionPreferences> = dataStorePrefs.data
        .catch { exception ->
            if (exception is IOException) {
                emit(CollectionPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }

    override fun getFilteringCollectionList(): Flow<List<Int>> =
        collectionPreferences.transform { prefs -> emit(prefs.filteredCollectionListList) }


    override suspend fun setFilteringCollectionList(collectionList: List<Int>) {
        dataStorePrefs.updateData { prefs ->
            prefs.toBuilder()
                .addAllFilteredCollectionList(collectionList)
                .build()
        }
    }

    override suspend fun deleteFilter(id: Int) {
        dataStorePrefs.updateData { prefs ->
            val old = prefs.filteredCollectionListList.toMutableList().apply {
                remove(id)
            }
            prefs.toBuilder()
                .clearFilteredCollectionList()
                .addAllFilteredCollectionList(old)
                .build()
        }
    }
}