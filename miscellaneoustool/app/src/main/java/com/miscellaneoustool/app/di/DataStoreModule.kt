package com.miscellaneoustool.app.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.miscellaneoustool.app.CollectionPreferences
import com.miscellaneoustool.app.TimerPreferences
import com.miscellaneoustool.app.data.datasource.cache.datastore.collectionPreferencesStore
import com.miscellaneoustool.app.data.datasource.cache.datastore.timerPreferencesStore
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