package com.miscellaneoustool.data.datasource.cache.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import com.miscellaneoustool.data.datasource.cache.database.entity.Book
import com.miscellaneoustool.data.datasource.cache.database.entity.Item
import com.miscellaneoustool.data.datasource.cache.database.entity.Stat
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {
    @RewriteQueriesToDropUnusedColumns
    @Query("select bookId, itemName as name, rlItemEnchant as enchant, rlItemCount as count, itemPrice as price " +
            "from Book inner join RegisterItemToBook on Book.bookId = RegisterItemToBook.rlBookId " +
            "inner join Item on RegisterItemToBook.rlItemName = Item.itemName " +
            "where rlBookId in (select DISTINCT rlBookId from RegisterItemToBook inner join Item on Item.itemName = RegisterItemToBook.rlItemName where Item.itemType like :category)")
    fun getCollectionItems(category: String): Flow<Map<Book, List<com.miscellaneoustool.data.model.Item>>>

    @RewriteQueriesToDropUnusedColumns
    @Query("select * from Book inner join Stat on Book.bookId = Stat.book_id where bookId in (:bookList)")
    fun getCollectionStats(bookList: List<Int>) : Flow<Map<Book, List<Stat>>>

    @Query("select * from Item order by Item.itemType desc")
    fun getItems(): Flow<List<Item>>

    @Query("update Item set itemPrice = :price where itemName like :name")
    suspend fun updateItemPrice(name: String, price: Int)

}