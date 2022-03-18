package com.jinproject.twomillustratedbook.Database.Entity

import androidx.room.*

@Entity(foreignKeys = [ForeignKey(entity = BookEntity::class, parentColumns = arrayOf("id"), childColumns = arrayOf("bookId"))], indices = [Index("bookId")])
data class BookContentEntity(
    var material : String,
    var count:Int,
    var bookId:Int,
    @PrimaryKey var _id:Int)
