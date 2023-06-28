package com.miscellaneoustool.app.data.datasource.cache.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import com.miscellaneoustool.app.data.datasource.cache.database.entity.Book
import com.miscellaneoustool.app.data.datasource.cache.database.entity.RegisterItemToBook
import com.miscellaneoustool.app.data.datasource.cache.database.entity.Stat
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {
    @RewriteQueriesToDropUnusedColumns
    @Query("select * " +
            "from RegisterItemToBook inner join Book on RegisterItemToBook.rlBookId = Book.bookId " +
            "where RegisterItemToBook.rlBookId in (select distinct rlBookId " +
            "from Book inner join RegisterItemToBook on Book.bookId = RegisterItemToBook.rlBookId join Item on Item.itemName = RegisterItemToBook.rlItemName " +
            "where Item.itemType like:category)")
    fun getCollectionItems(category: String): Flow<Map<Book, List<RegisterItemToBook>>>

    @RewriteQueriesToDropUnusedColumns
    @Query("select * from Book inner join Stat on Book.bookId = Stat.book_id where bookId in (:bookList)")
    fun getCollectionStats(bookList: List<Int>) : Flow<Map<Book, List<Stat>>>

}