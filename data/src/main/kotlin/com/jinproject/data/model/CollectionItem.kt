package com.jinproject.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.jinproject.data.datasource.cache.database.entity.Book
import com.jinproject.data.datasource.cache.database.entity.Item
import com.jinproject.data.datasource.cache.database.entity.RegisterItemToBook
import com.jinproject.data.datasource.cache.database.entity.Stat

data class RegisterItemToBookMapping(
    @Embedded
    val registerItemToBook: RegisterItemToBook,
    @Relation(
        parentColumn = "itemName",
        entityColumn = "itemName",
    )
    val item: Item,
)

data class CollectionMapping(
    @Embedded
    val book: Book,
    @Relation(
        parentColumn = "bookId",
        entityColumn = "book_id"
    )
    val stats: List<Stat>,
    @Relation(
        entity = RegisterItemToBook::class,
        parentColumn = "bookId",
        entityColumn = "bookId",
    )
    val items: List<RegisterItemToBookMapping>,
)
