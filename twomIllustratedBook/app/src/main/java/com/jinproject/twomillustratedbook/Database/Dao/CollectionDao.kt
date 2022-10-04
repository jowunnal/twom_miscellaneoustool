package com.jinproject.twomillustratedbook.Database.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import com.jinproject.twomillustratedbook.Database.Entity.Book
import com.jinproject.twomillustratedbook.Database.Entity.RegisterItemToBook

@Dao
interface CollectionDao {
    @RewriteQueriesToDropUnusedColumns
    @Query("select * from Book inner join RegisterItemToBook on Book.bookId = RegisterItemToBook.rlBookId join Item on Item.itemName = RegisterItemToBook.rlItemName where Item.itemType like:data")
    fun getContent(data:String): LiveData<Map<Book, List<RegisterItemToBook>>>
}