package com.jinproject.data.datasource.cache.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["bookId", "itemName", "rlItemEnchant"],
    foreignKeys = [
        ForeignKey(
            entity = Book::class,
            parentColumns = arrayOf("bookId"),
            childColumns = arrayOf("bookId")
        ),
        ForeignKey(
            entity = Item::class,
            parentColumns = arrayOf("itemName"),
            childColumns = arrayOf("itemName")
        )
    ],
    indices = [Index("itemName")],
)
data class RegisterItemToBook(
    val bookId: Int,
    val itemName: String,
    val rlItemCount: Int,
    val rlItemEnchant: Int
)