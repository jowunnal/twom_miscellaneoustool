package com.jinproject.data.datasource.cache.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import com.jinproject.data.repository.model.ItemModel

@Entity(
    primaryKeys = ["mdMonsName", "mdItemName"],
    foreignKeys = [
        ForeignKey(
            entity = Monster::class,
            parentColumns = arrayOf("monsName"),
            childColumns = arrayOf("mdMonsName")
        ),
        ForeignKey(
            entity = Item::class,
            parentColumns = arrayOf("itemName"),
            childColumns = arrayOf("mdItemName")
        )
    ],
)
data class MonsDropItem(
    val mdMonsName: String,
    val mdItemName: String
)

fun MonsDropItem.toItemDataModel() = ItemModel(
    name = mdItemName,
    count = 0,
    enchantNumber = 0,
    price = 0,
    type = "miscellaneous"
)

fun List<MonsDropItem>.toItemDataModelList() = map { it.toItemDataModel() }