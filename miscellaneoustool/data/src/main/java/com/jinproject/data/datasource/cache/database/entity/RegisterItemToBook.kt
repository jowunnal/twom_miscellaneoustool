package com.jinproject.data.datasource.cache.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["rlBookId", "rlItemName","rlItemEnchant"],
    foreignKeys = [ForeignKey(
        entity = Book::class,
        parentColumns = arrayOf("bookId"),
        childColumns = arrayOf("rlBookId")
    ),
        ForeignKey(
            entity = Item::class,
            parentColumns = arrayOf("itemName"),
            childColumns = arrayOf("rlItemName")
        )], indices = [Index("rlItemName")]
)
data class RegisterItemToBook(
    val rlBookId: Int,
    val rlItemName: String,
    val rlItemCount: Int,
    val rlItemEnchant: Int
)