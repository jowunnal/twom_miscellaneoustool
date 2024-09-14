package com.jinproject.data.di

import com.jinproject.data.repository.CollectionRepositoryImpl
import com.jinproject.data.repository.DropListRepositoryImpl
import com.jinproject.data.repository.SimulatorRepositoryImpl
import com.jinproject.data.repository.TimerRepositoryImpl
import com.jinproject.domain.repository.CollectionRepository
import com.jinproject.domain.repository.DropListRepository
import com.jinproject.domain.repository.SimulatorRepository
import com.jinproject.domain.repository.TimerRepository
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

    @Singleton
    @Binds
    abstract fun bindSimulatorRepository(simulatorRepositoryImpl: SimulatorRepositoryImpl) : SimulatorRepository

}