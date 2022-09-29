package com.jinproject.twomillustratedbook.Database

import android.content.Context
import androidx.room.Room
import com.jinproject.twomillustratedbook.Database.Dao.BookDao
import com.jinproject.twomillustratedbook.Database.Dao.LoginDao
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
    fun provideBookDao(bookdDatabase: BookDatabase):BookDao{
        return bookdDatabase.bookDao()
    }

    @Provides
    @Singleton
    fun provideUserDao(bookDatabase: BookDatabase):LoginDao{
        return bookDatabase.loginDao()
    }

    @Provides
    @Singleton
    fun provideBookDatabaseInstance(@ApplicationContext context:Context):BookDatabase{
        return Room.databaseBuilder(context,BookDatabase::class.java,"BookDatabase")
            .createFromAsset("database/db_twom_2.db")
            .build()
    }

    /*
    companion object{
        @Volatile
        private var bookInstance : BookDatabase ?= null

        fun getInstance(context: Context) : BookDatabase {
            return bookInstance ?: synchronized(BookDatabase::class){ //syncronized()는 클래스파일에 멀티스레드가 접근하지못하도록 하기위해 사용
                val instance = Room.databaseBuilder(context.applicationContext,
                BookDatabase::class.java, "book_Database").createFromAsset("database/db_twom_2.db")
                    .addMigrations().fallbackToDestructiveMigration().build()
                bookInstance=instance
                instance
            }
        }
    }
     */
}