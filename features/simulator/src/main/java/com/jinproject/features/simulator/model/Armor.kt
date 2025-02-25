package com.jinproject.features.simulator.model

import com.jinproject.domain.model.Stat
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Armor")
internal data class Armor(
    override val name: String,
    override val imgName: String,
    override val level: Int,
    override val uuid: String,
    override val enchantNumber: Int,
    override val options: List<ItemOption>,
    val armor: Int,
) : Equipment() {

    internal fun getArmorEnchanted(): Int {
        val addition = if (enchantNumber < 5) {
            (armor * 0.1).coerceAtLeast(1.0)
        } else {
            (armor * 0.2).coerceAtLeast(1.0)
        }

        return (armor + addition * enchantNumber).toInt()
    }

    companion object {
        fun getInitValues(uuid: String) = Armor(
            name = "세계수가지갑옷",
            level = 44,
            options = listOf(
                ItemOption(name = Stat.HP, value = 28.0f),
            ),
            armor = 14,
            enchantNumber = 0,
            imgName = "img_blade",
            uuid = uuid,
        )
    }
}
