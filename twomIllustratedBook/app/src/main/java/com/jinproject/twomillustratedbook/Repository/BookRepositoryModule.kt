package com.jinproject.twomillustratedbook.Repository

import com.jinproject.twomillustratedbook.Database.BookDatabase
import com.jinproject.twomillustratedbook.Database.Dao.BookDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BookRepositoryModule {

    @Singleton
    @Provides
    fun bindBookRepository(bookDao: BookDao) : BookRepositoryImpl{
        return BookRepositoryImpl(bookDao)
    }
}