package com.jinproject.data.repository.di

import com.jinproject.data.repository.repo.SimulatorRepositoryImpl
import com.jinproject.domain.repository.CollectionRepository
import com.jinproject.domain.repository.DropListRepository
import com.jinproject.domain.repository.SimulatorRepository
import com.jinproject.domain.repository.SymbolRepository
import com.jinproject.domain.repository.TimerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindDropListRepository(dropListRepositoryImpl: com.jinproject.data.repository.repo.DropListRepositoryImpl): DropListRepository

    @Singleton
    @Binds
    abstract fun bindCollectionRepository(collectionRepositoryImpl: com.jinproject.data.repository.repo.CollectionRepositoryImpl): CollectionRepository

    @Singleton
    @Binds
    abstract fun bindTimerRepository(timerRepositoryImpl: com.jinproject.data.repository.repo.TimerRepositoryImpl): TimerRepository

    @Singleton
    @Binds
    abstract fun bindSimulatorRepository(simulatorRepositoryImpl: SimulatorRepositoryImpl): SimulatorRepository

    @Singleton
    @Binds
    abstract fun bindSymbolRepo(symbolRepositoryImpl: com.jinproject.data.repository.repo.SymbolRepositoryImpl): SymbolRepository
}