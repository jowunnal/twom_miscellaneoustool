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

data class Item(
    val name: String,
    val price: Long,
    val imageName: String,
    val itemType: String,
): GetProperDomainFactory {
    override fun getDomainFactory(): ItemDomainFactory =
        when(itemType) {
            "잡탬", "miscellaneous" -> MiscellaneousItemDomainFactory(this)
            else -> throw IllegalArgumentException("[$this] 는 올바른 아이템 타입이 아닙니다.")
        }
}

data class Skill(
    val item: com.jinproject.data.repository.model.Item,
    val limitedLevel: Int
): GetProperDomainFactory {
    override fun getDomainFactory(): ItemDomainFactory =
        when(item.itemType) {
            "스킬", "skill" -> SkillItemDomainFactory(item = item, limitedLevel = limitedLevel)
            else -> throw IllegalArgumentException("[$this] 는 올바른 아이템 타입이 아닙니다.")
        }
}


class MiscellaneousItemDomainFactory(
    private val item: com.jinproject.data.repository.model.Item
): ItemDomainFactory() {
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
): ItemDomainFactory() {
    override fun create(): Item {
        return Skill(
            name = item.name,
            price = item.price,
            imageName = item.imageName,
            limitedLevel = limitedLevel
        )
    }
}
