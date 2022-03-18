package com.jinproject.twomillustratedbook.Database

import android.app.Application
import com.jinproject.twomillustratedbook.Repository.BookRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class BookApplication : Application() {
    val aplicationScope= CoroutineScope(SupervisorJob())
    val database by lazy{BookDatabase.getInstance(this)}
    val repository by lazy { BookRepository(database.bookDao()) }
}