package com.miscellaneoustool.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.miscellaneoustool.core.TimerPreferences
import com.miscellaneoustool.data.CollectionPreferences
import com.miscellaneoustool.data.datasource.cache.datastore.collectionPreferencesStore
import com.miscellaneoustool.data.datasource.cache.datastore.timerPreferencesStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Singleton
    @Provides
    fun providesDataStoreInstance(@ApplicationContext context:Context): DataStore<TimerPreferences> {
        return context.timerPreferencesStore
    }

    @Singleton
    @Provides
    fun providesCollectionDataStore(@ApplicationContext context: Context): DataStore<CollectionPreferences> {
        return context.collectionPreferencesStore
    }

}