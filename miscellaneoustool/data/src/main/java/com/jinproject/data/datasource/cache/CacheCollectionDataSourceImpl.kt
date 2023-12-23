package com.jinproject.data.datasource.cache

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.datastore.core.DataStore
import com.jinproject.data.CollectionPreferences
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

    override suspend fun setSymbolUri(uri: Uri) {
        dataStorePrefs.updateData { prefs ->
            prefs.toBuilder().addStoredSymbolUri(uri.toString()).build()
        }
    }

    override fun getSymbolUri(): Flow<List<String>> =
        collectionPreferences.transform { prefs ->
            emit(prefs.storedSymbolUriList)
        }
}