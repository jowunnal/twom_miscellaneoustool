package com.jinproject.data.datasource

import android.content.Context
import androidx.room.Room
import com.jinproject.core.util.doOnLocaleLanguage
import com.jinproject.core.util.onLanguage
import com.jinproject.data.datasource.cache.database.BookDatabase
import com.jinproject.data.datasource.cache.database.BookMigration
import com.jinproject.data.datasource.cache.database.dao.CollectionDao
import com.jinproject.data.datasource.cache.database.dao.DropListDao
import com.jinproject.data.datasource.cache.database.dao.SimulatorDao
import com.jinproject.data.datasource.cache.database.dao.TimerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.Locale
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
    fun provideSimulatorDao(bookDatabase: BookDatabase): SimulatorDao =
        bookDatabase.getSimulatorDao()

    @Provides
    @Singleton
    fun provideBookDatabaseInstance(@ApplicationContext context: Context): BookDatabase {
        val language = Locale.getDefault().language

        val assetName = language.onLanguage(
            ko = "database/db_twom_3.db",
            en = "database/db_twom_3_eng.db"
        )
        val migration1to2 = language.onLanguage(
            ko = BookMigration.MIGRATION_1_2_KOR,
            en = BookMigration.MIGRATION_1_2_ELSE
        )
        val migration2to3 = language.onLanguage(
            ko = BookMigration.MIGRATION_2_3_KOR,
            en = BookMigration.MIGRATION_2_3_ELSE,
        )
        val migration3to4 = language.onLanguage(
            ko = BookMigration.MIGRATION_3_4_KOR,
            en = BookMigration.MIGRATION_3_4_ELSE,
        )
        val migration4to5 = language.onLanguage(
            ko = BookMigration.MIGRATION_4_5_KOR,
            en = BookMigration.MIGRATION_4_5_ELSE
        )
        val migration5to6 = language.onLanguage(
            ko = BookMigration.MIGRATION_5_6_KOR,
            en = BookMigration.MIGRATION_5_6_ELSE
        )
        val migration6to7 = language.onLanguage(
            ko = BookMigration.MIGRATION_6_7_KOR,
            en = BookMigration.MIGRATION_6_7_ELSE
        )
        val migration7to8 = language.onLanguage(
            ko = BookMigration.MIGRATION_7_8_KOR,
            en = BookMigration.MIGRATION_7_8_ELSE
        )

        return Room.databaseBuilder(context, BookDatabase::class.java, "BookDatabase")
            .createFromAsset(assetName)
            .addMigrations(
                migration1to2,
                migration2to3,
                migration3to4,
                migration4to5,
                migration5to6,
                migration6to7,
                migration7to8
            )
            .build()
    }
}