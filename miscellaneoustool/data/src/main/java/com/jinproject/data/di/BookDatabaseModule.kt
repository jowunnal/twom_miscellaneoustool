package com.jinproject.data.di

import android.content.Context
import androidx.room.Room
import com.jinproject.core.util.doOnLocaleLanguage
import com.jinproject.data.datasource.cache.database.BookDatabase
import com.jinproject.data.datasource.cache.database.dao.CollectionDao
import com.jinproject.data.datasource.cache.database.dao.DropListDao
import com.jinproject.data.datasource.cache.database.dao.TimerDao
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
        val assetName = context.doOnLocaleLanguage(
            onKo = "database/db_twom_2.db",
            onElse = "database/db_twom_2_eng.db"
        )
        val migration = context.doOnLocaleLanguage(
            onKo = BookDatabase.MIGRATION_1_2_KOR,
            onElse = BookDatabase.MIGRATION_1_2_ELSE
        )
        return Room.databaseBuilder(context, BookDatabase::class.java,"BookDatabase")
            .createFromAsset(assetName)
            .addMigrations(migration)
            .build()
    }
}