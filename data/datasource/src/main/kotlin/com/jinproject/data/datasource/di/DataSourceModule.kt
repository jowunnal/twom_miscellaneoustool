package com.jinproject.data.datasource.di

import com.jinproject.core.TimerPreferences
import com.jinproject.data.CollectionPreferences
import com.jinproject.data.SimulatorPreferences
import com.jinproject.data.datasource.cache.CacheCollectionDataSourceImpl
import com.jinproject.data.datasource.cache.CacheDropListDataSourceImpl
import com.jinproject.data.datasource.cache.CacheSimulatorDataSourceImpl
import com.jinproject.data.datasource.cache.CacheTimerDataSourceImpl
import com.jinproject.data.datasource.remote.GenerateImageDataSourceImpl
import com.jinproject.data.datasource.remote.RemoteImageDownloadManagerImpl
import com.jinproject.data.repository.datasource.CacheCollectionDataSource
import com.jinproject.data.repository.datasource.CacheDropListDataSource
import com.jinproject.data.repository.datasource.CacheSimulatorDataSource
import com.jinproject.data.repository.datasource.CacheTimerDataSource
import com.jinproject.data.repository.datasource.RemoteGenerateImageDataSource
import com.jinproject.data.repository.datasource.RemoteImageDownloadManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindsCacheSimulatorDataSource(cacheSimulatorDataSourceImpl: CacheSimulatorDataSourceImpl): CacheSimulatorDataSource<SimulatorPreferences>

    @Singleton
    @Binds
    abstract fun bindsCacheCollectionDataSource(cacheCollectionDataSourceImpl: CacheCollectionDataSourceImpl): CacheCollectionDataSource<CollectionPreferences>

    @Singleton
    @Binds
    abstract fun bindsCacheTimerDataSource(cacheTimerDataSourceImpl: CacheTimerDataSourceImpl): CacheTimerDataSource<TimerPreferences>

    @Singleton
    @Binds
    abstract fun bindsRemoteGenerateImageDataSource(remoteGenerateImageDataSourceImpl: GenerateImageDataSourceImpl): RemoteGenerateImageDataSource

    @Singleton
    @Binds
    abstract fun bindsRemoteImageDownloadManager(remoteImageDownloadManagerImpl: RemoteImageDownloadManagerImpl): RemoteImageDownloadManager

    @Singleton
    @Binds
    abstract fun bindsCacheDropListDataSource(dropListDataSourceImpl: CacheDropListDataSourceImpl): CacheDropListDataSource
}