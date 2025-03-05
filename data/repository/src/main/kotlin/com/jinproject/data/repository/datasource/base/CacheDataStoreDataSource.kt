package com.jinproject.data.repository.datasource.base

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow

interface CacheDataStoreDataSource<T> {
    val prefs: DataStore<T>

    val data: Flow<T>
}