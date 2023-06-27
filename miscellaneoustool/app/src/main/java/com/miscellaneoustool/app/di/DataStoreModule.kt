package com.miscellaneoustool.app.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.jinproject.twomillustratedbook.TimerPreferences
import com.miscellaneoustool.app.data.datastore.timerPreferencesStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Singleton
    @Provides
    fun providesDataStoreInstance(@ApplicationContext context:Context): DataStore<TimerPreferences> {
        return context.timerPreferencesStore
    }
}