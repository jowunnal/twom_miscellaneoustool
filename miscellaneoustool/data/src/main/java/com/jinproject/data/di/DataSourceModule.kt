package com.jinproject.data.di

import com.jinproject.data.datasource.cache.CacheCollectionDataSource
import com.jinproject.data.datasource.cache.CacheCollectionDataSourceImpl
import com.jinproject.data.datasource.cache.CacheTimerDataSource
import com.jinproject.data.datasource.cache.CacheTimerDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindsCacheTimerDataSource(timerDataSourceImpl: CacheTimerDataSourceImpl): CacheTimerDataSource

    @Binds
    abstract fun bindsCacheCollectionDataSource(cacheCollectionDataSourceImpl: CacheCollectionDataSourceImpl): CacheCollectionDataSource
}