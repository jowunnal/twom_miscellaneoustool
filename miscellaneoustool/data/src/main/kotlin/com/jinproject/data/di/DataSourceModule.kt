package com.jinproject.data.di

import com.jinproject.data.datasource.cache.CacheCollectionDataSource
import com.jinproject.data.datasource.cache.CacheCollectionDataSourceImpl
import com.jinproject.data.datasource.cache.CacheSimulatorDataSource
import com.jinproject.data.datasource.cache.CacheSimulatorDataSourceImpl
import com.jinproject.data.datasource.cache.CacheTimerDataSource
import com.jinproject.data.datasource.cache.CacheTimerDataSourceImpl
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
    abstract fun bindsCacheTimerDataSource(timerDataSourceImpl: CacheTimerDataSourceImpl): CacheTimerDataSource

    @Singleton
    @Binds
    abstract fun bindsCacheCollectionDataSource(cacheCollectionDataSourceImpl: CacheCollectionDataSourceImpl): CacheCollectionDataSource

    @Singleton
    @Binds
    abstract fun bindsCacheSimulatorDataSource(cacheSimulatorDataSourceImpl: CacheSimulatorDataSourceImpl): CacheSimulatorDataSource
}