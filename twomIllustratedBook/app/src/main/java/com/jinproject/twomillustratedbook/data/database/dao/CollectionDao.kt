package com.jinproject.twomillustratedbook.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import com.jinproject.twomillustratedbook.data.Entity.Book
import com.jinproject.twomillustratedbook.data.Entity.RegisterItemToBook
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {
    @RewriteQueriesToDropUnusedColumns
    @Query("select * from RegisterItemToBook inner join Book on RegisterItemToBook.rlBookId = Book.bookId where RegisterItemToBook.rlBookId in (select distinct rlBookId from Book inner join RegisterItemToBook on Book.bookId = RegisterItemToBook.rlBookId join Item on Item.itemName = RegisterItemToBook.rlItemName where Item.itemType like:category)")
    fun getCollectionList(category: String): Flow<Map<Book, List<RegisterItemToBook>>>
}