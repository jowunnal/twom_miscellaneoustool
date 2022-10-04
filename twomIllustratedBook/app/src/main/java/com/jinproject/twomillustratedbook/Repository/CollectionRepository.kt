package com.jinproject.twomillustratedbook.Repository

import androidx.lifecycle.LiveData
import com.jinproject.twomillustratedbook.Database.Entity.Book
import com.jinproject.twomillustratedbook.Database.Entity.RegisterItemToBook

interface CollectionRepository {

    fun bookList(data:String): LiveData<Map<Book, List<RegisterItemToBook>>>

}