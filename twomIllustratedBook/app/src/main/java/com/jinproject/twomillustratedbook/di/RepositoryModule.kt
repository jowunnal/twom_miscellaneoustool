package com.jinproject.twomillustratedbook.di

import com.jinproject.twomillustratedbook.domain.repository.CollectionRepository
import com.jinproject.twomillustratedbook.domain.repository.DropListRepository
import com.jinproject.twomillustratedbook.domain.repository.TimerRepository
import com.jinproject.twomillustratedbook.data.repository.CollectionRepositoryImpl
import com.jinproject.twomillustratedbook.data.repository.DropListRepositoryImpl
import com.jinproject.twomillustratedbook.data.repository.TimerRepositoryImpl
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