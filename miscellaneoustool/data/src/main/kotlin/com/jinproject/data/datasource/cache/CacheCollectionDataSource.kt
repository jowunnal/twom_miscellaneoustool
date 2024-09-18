package com.jinproject.data.datasource.cache

import android.net.Uri
import androidx.datastore.core.DataStore
import com.jinproject.data.CollectionPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.transform
import java.io.IOException
import javax.inject.Inject

class CacheCollectionDataSource @Inject constructor(
    private val dataStorePrefs: DataStore<CollectionPreferences>
) {

    private val collectionPreferences: Flow<CollectionPreferences> = dataStorePrefs.data
        .catch { exception ->
            if (exception is IOException) {
                emit(CollectionPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }

    fun getFilteringCollectionList(): Flow<List<Int>> =
        collectionPreferences.transform { prefs -> emit(prefs.filteredCollectionListList) }


    suspend fun setFilteringCollectionList(collectionList: List<Int>) {
        dataStorePrefs.updateData { prefs ->
            prefs.toBuilder()
                .addAllFilteredCollectionList(collectionList)
                .build()
        }
    }

    suspend fun deleteFilter(id: Int) {
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

    suspend fun setSymbolUri(uri: Uri) {
        dataStorePrefs.updateData { prefs ->
            prefs.toBuilder().addStoredSymbolUri(uri.toString()).build()
        }
    }

    fun getSymbolUri(): Flow<List<String>> =
        collectionPreferences.transform { prefs ->
            emit(prefs.storedSymbolUriList)
        }
}