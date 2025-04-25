package com.jinproject.data.repository.model

import com.jinproject.domain.entity.item.Item
import com.jinproject.domain.entity.item.Miscellaneous
import com.jinproject.domain.entity.item.Skill

data class ItemModel(
    val name: String,
    val count: Int,
    val enchantNumber: Int,
    val price: Long,
    val type: String,
)

class Item(
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
    private val item: com.jinproject.data.repository.model.Item
) : ItemDomainFactory() {
    override fun create(): Item {
        return Miscellaneous(
            name = item.name,
            price = item.price,
            imageName = item.imageName,
        )
    }
}

class SkillItemDomainFactory(
    private val item: com.jinproject.data.repository.model.Item,
    private val limitedLevel: Int,
) : ItemDomainFactory() {
    override fun create(): Item {
        return Skill(
            name = item.name,
            price = item.price,
            imageName = item.imageName,
            limitedLevel = limitedLevel
        )
    }
}
