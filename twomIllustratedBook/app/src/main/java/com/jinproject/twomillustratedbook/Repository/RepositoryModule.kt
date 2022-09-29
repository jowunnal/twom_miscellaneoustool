package com.jinproject.twomillustratedbook.Repository

import com.jinproject.twomillustratedbook.Database.BookDatabase
import com.jinproject.twomillustratedbook.Database.Dao.BookDao
import com.jinproject.twomillustratedbook.Database.Dao.LoginDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule{

    @Provides
    @Singleton
    fun bindRepository(loginDao: LoginDao):UserRepositoryImpl{
        return UserRepositoryImpl(loginDao)
    }
}