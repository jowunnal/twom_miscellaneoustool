package com.miscellaneoustool.app.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
    @PrimaryKey val bookId: Int
)