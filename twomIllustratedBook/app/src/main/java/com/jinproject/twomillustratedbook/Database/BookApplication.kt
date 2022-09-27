package com.jinproject.twomillustratedbook.Database

import android.app.Application
import com.jinproject.twomillustratedbook.Repository.BookRepositoryModule
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class BookApplication : Application() {
}