package com.jinproject.data.datasource.cache.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jinproject.data.repository.model.EquipmentEntity
import com.jinproject.data.repository.model.EquipmentInfo

@Entity
data class Item(
    @PrimaryKey val itemName: String,
    val itemType: String,
    val itemPrice: Long,
    val img_name: String,
) {
    fun toItemData(enchantNumber: Int = 0): com.jinproject.data.repository.model.GetItemDomainFactory =
        when (itemType) {
            com.jinproject.data.repository.model.NotEquipmentItem.MISCELLANEOUS_KO,
            com.jinproject.data.repository.model.NotEquipmentItem.MISCELLANEOUS_EN,
            com.jinproject.data.repository.model.NotEquipmentItem.SKILL_KO,
            com.jinproject.data.repository.model.NotEquipmentItem.SKILL_EN -> com.jinproject.data.repository.model.NotEquipmentItem(
                name = itemName,
                price = itemPrice,
                itemType = itemType,
                imageName = img_name,
                limitedLevel = 0,
            )

            else -> EquipmentEntity(
                price = itemPrice,
                itemType = itemType,
                imageName = img_name,
                level = 0,
            ).toEquipment(
                equipmentInfo = EquipmentInfo.getInitValues().copy(name = itemName, enchantNumber = enchantNumber)
            )
        }
}

fun List<Item>.toItemDataModelList(): List<com.jinproject.data.repository.model.GetItemDomainFactory> = map { it.toItemData() }