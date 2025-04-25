package com.jinproject.data.datasource.cache.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jinproject.data.repository.model.Equipment
import com.jinproject.data.repository.model.EquipmentEntity
import com.jinproject.data.repository.model.EquipmentInfo

@Entity
data class Item(
    @PrimaryKey val itemName: String,
    val itemType: String,
    val itemPrice: Long,
) {
    fun toItemData(): com.jinproject.data.repository.model.GetItemDomainFactory =
        when (itemType) {
            com.jinproject.data.repository.model.Item.MISCELLANEOUS_KO,
            com.jinproject.data.repository.model.Item.MISCELLANEOUS_EN,
            com.jinproject.data.repository.model.Item.SKILL_KO,
            com.jinproject.data.repository.model.Item.SKILL_EN -> com.jinproject.data.repository.model.Item(
                name = itemName,
                price = itemPrice,
                itemType = itemType,
                imageName = "",
                limitedLevel = 0,
            )

            else -> EquipmentEntity(
                price = itemPrice,
                itemType = itemType,
                imageName = "",
                level = 0,
            ).toEquipment(
                equipmentInfo = EquipmentInfo.getInitValues().copy(name = itemName)
            )
        }
}

fun List<Item>.toItemDataModelList(): List<com.jinproject.data.repository.model.GetItemDomainFactory> = map { it.toItemData() }