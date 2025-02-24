package com.jinproject.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.jinproject.core.TimerPreferences
import com.jinproject.data.CollectionPreferences
import com.jinproject.data.SimulatorPreferences
import com.jinproject.data.datasource.cache.datastore.collectionPreferencesStore
import com.jinproject.data.datasource.cache.datastore.simulatorPreferencesStore
import com.jinproject.data.datasource.cache.datastore.timerPreferencesStore
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

    @Singleton
    @Provides
    fun providesSimulatorPreferencesStore(@ApplicationContext context: Context): DataStore<SimulatorPreferences> {
        return context.simulatorPreferencesStore
    }

}