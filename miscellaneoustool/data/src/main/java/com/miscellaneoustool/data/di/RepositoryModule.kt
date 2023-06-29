package com.miscellaneoustool.data.di

import com.miscellaneoustool.data.repository.CollectionRepositoryImpl
import com.miscellaneoustool.data.repository.DropListRepositoryImpl
import com.miscellaneoustool.data.repository.TimerRepositoryImpl
import com.miscellaneoustool.domain.repository.CollectionRepository
import com.miscellaneoustool.domain.repository.DropListRepository
import com.miscellaneoustool.domain.repository.TimerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule{

    @Singleton
    @Binds
    abstract fun bindDropListRepository (dropListRepositoryImpl: DropListRepositoryImpl) : DropListRepository

    @Singleton
    @Binds
    abstract fun bindCollectionRepository(collectionRepositoryImpl: CollectionRepositoryImpl): CollectionRepository

    @Singleton
    @Binds
    abstract fun bindTimerRepository(timerRepositoryImpl: TimerRepositoryImpl) : TimerRepository

}