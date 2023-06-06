package com.jinproject.twomillustratedbook.di

import android.content.Context
import androidx.room.Room
import com.jinproject.twomillustratedbook.data.database.BookDatabase
import com.jinproject.twomillustratedbook.data.database.dao.CollectionDao
import com.jinproject.twomillustratedbook.data.database.dao.DropListDao
import com.jinproject.twomillustratedbook.data.database.dao.TimerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BookDatabaseModule {

    @Provides
    @Singleton
    fun provideCollectionDao(bookDatabase: BookDatabase): CollectionDao {
        return bookDatabase.getCollectionDao()
    }
    @Provides
    @Singleton
    fun provideDropListDao(bookDatabase: BookDatabase): DropListDao {
        return bookDatabase.getDropListDao()
    }

    @Provides
    @Singleton
    fun provideTimerDao(bookDatabase: BookDatabase): TimerDao {
        return bookDatabase.getTimerDao()
    }

    @Provides
    @Singleton
    fun provideBookDatabaseInstance(@ApplicationContext context:Context): BookDatabase {
        return Room.databaseBuilder(context, BookDatabase::class.java,"BookDatabase")
            .createFromAsset("database/db_twom_2.db")
            .build()
    }
}