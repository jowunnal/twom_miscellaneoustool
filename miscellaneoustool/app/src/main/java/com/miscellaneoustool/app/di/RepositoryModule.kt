package com.miscellaneoustool.app.di

import com.miscellaneoustool.app.data.repository.CollectionRepositoryImpl
import com.miscellaneoustool.app.data.repository.DropListRepositoryImpl
import com.miscellaneoustool.app.data.repository.TimerRepositoryImpl
import com.miscellaneoustool.app.domain.repository.CollectionRepository
import com.miscellaneoustool.app.domain.repository.DropListRepository
import com.miscellaneoustool.app.domain.repository.TimerRepository
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