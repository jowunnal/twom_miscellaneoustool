package com.jinproject.data.repository.model

import com.jinproject.domain.entity.item.Item
import com.jinproject.domain.entity.item.Miscellaneous
import com.jinproject.domain.entity.item.Skill

class NotEquipmentItem(
    val itemType: String,
    val name: String,
    val price: Long,
    val imageName: String,
    val limitedLevel: Int,
) : GetItemDomainFactory {
    override fun getDomainFactory(): ItemDomainFactory =
        when (itemType) {
            MISCELLANEOUS_KO, MISCELLANEOUS_EN -> MiscellaneousItemDomainFactory(this)
            SKILL_KO, SKILL_EN -> SkillItemDomainFactory(this, limitedLevel)
            else -> throw IllegalArgumentException("")
        }

    companion object {
        const val MISCELLANEOUS_KO = "잡탬"
        const val MISCELLANEOUS_EN = "miscellaneous"
        const val SKILL_KO = "스킬"
        const val SKILL_EN = "skill"
    }
}

class MiscellaneousItemDomainFactory(
    private val notEquipmentItem: NotEquipmentItem
) : ItemDomainFactory() {
    override fun create(): Item {
        return Miscellaneous(
            name = notEquipmentItem.name,
            price = notEquipmentItem.price,
            imageName = notEquipmentItem.imageName,
        )
    }
}

class SkillItemDomainFactory(
    private val notEquipmentItem: NotEquipmentItem,
    private val limitedLevel: Int,
) : ItemDomainFactory() {
    override fun create(): Item {
        return Skill(
            name = notEquipmentItem.name,
            price = notEquipmentItem.price,
            imageName = notEquipmentItem.imageName,
            limitedLevel = limitedLevel
        )
    }
}
