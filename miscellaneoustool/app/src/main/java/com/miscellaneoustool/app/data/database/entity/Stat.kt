package com.miscellaneoustool.app.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey


@Entity(
    primaryKeys = ["book_id", "type"],
    foreignKeys = [ForeignKey(
        entity = Book::class,
        parentColumns = ["bookId"],
        childColumns = ["book_id"]
    )]
)
data class Stat(
    val book_id: Int,
    val type: String,
    val value: Double
)
