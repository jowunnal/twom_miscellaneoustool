package com.jinproject.features.simulator.model

import android.content.Context
import com.jinproject.core.util.doOnLocaleLanguage
import com.jinproject.domain.entity.item.ArmorScroll
import com.jinproject.domain.entity.item.EnchantableEquipment
import com.jinproject.domain.entity.item.GradeScroll
import com.jinproject.domain.entity.item.Item
import com.jinproject.domain.entity.item.WeaponScroll
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.math.roundToInt

internal object IntRangeSerializer : KSerializer<IntRange> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("kotlin.ranges.IntRange", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): IntRange {
        val decodedString = decoder.decodeString().split(" ").map { it.toInt() }
        return decodedString[0]..decodedString[1]
    }

    override fun serialize(encoder: Encoder, value: IntRange) {
        encoder.encodeString("${value.first} ${value.last}")
    }
}

@Serializable
@SerialName("Weapon")
internal data class Weapon(
    override val name: String,
    override val imgName: String,
    override val level: Int,
    override val uuid: String,
    override val enchantNumber: Int,
    override val options: List<ItemOption>,
    @Serializable(with = IntRangeSerializer::class) val damage: IntRange,
    val speed: Float,
) : Equipment() {

    internal fun getDamageRangeEnchanted(context: Context): IntRange {
        val isMagicianWeapon =
            options.any { option -> option.name == "STATINT" || option.name == "MP" || option.name == "마나" || option.name == "지능" }

        val addition = if (!isMagicianWeapon) {
            11 * enchantNumber + (11 * (enchantNumber - 6)).coerceAtLeast(0)
        } else {
            when (name) {
                context.doOnLocaleLanguage(
                    onKo = "파괴의홀",
                    onElse = "Hole of Destruction"
                ) -> when (enchantNumber < 7) {
                    true -> 22 * enchantNumber + (if (enchantNumber.mod(2) == 0) 1 else 0)
                    false -> 45 * enchantNumber - 137
                }

                context.doOnLocaleLanguage(
                    onKo = "소울이터",
                    onElse = "Soul Eater"
                ) -> when (enchantNumber < 7) {
                    true -> 23 * enchantNumber
                    false -> 46 * enchantNumber - 137
                }

                else -> throw IllegalStateException("$name cannot be used yet")
            }
        }

        val minDamage = damage.first + damage.first * addition * 0.01
        val maxDamage = damage.last + damage.last * addition * 0.01

        return minDamage.roundToInt()..maxDamage.roundToInt()
    }

    companion object {
        const val SPEED = "speed"
        const val MAXDAMAGE = "MaxDamage"
        const val MINDAMAGE = "MinDamage"
        fun getInitValues(uuid: String) = Weapon(
            name = "버닝 블레이드",
            level = 46,
            options = listOf(
                ItemOption(name = "명중", value = 8.0f),
                ItemOption(name = "크리티컬", value = 2.0f)
            ),
            damage = 10..70,
            speed = 1.2f,
            enchantNumber = 0,
            imgName = "img_blade",
            uuid = uuid,
        )
    }

    override fun toDomainModel(uuid: String?): EnchantableEquipment =
        com.jinproject.domain.entity.item.Weapon(
            stats = options.toMap(),
            limitedLevel = level,
            name = name,
            price = 0,
            type = com.jinproject.domain.entity.item.ItemType.NORMAL,
            speed = speed.toInt(),
            damageRange = damage,
            uuid = uuid ?: this.uuid
        ).apply {
            enchantNumber = this@Weapon.enchantNumber
        }
}

fun Item.transImageName(): String =
    when (this) {
        is com.jinproject.domain.entity.item.Equipment ->
            when (name) {
                "버닝블레이드", "Burning Blade" -> "burning_blade"
                "페일노트", "Fail-Not" -> "fail_not"
                "파괴의홀", "Hole of Destruction" -> "hole_of_destruction"
                "임페리얼보우", "Imperial Bow" -> "imperial_bow"
                "인퀴지션", "Inquisition" -> "inquisition"
                "소울이터", "Soul Eater" -> "soul_eater"
                else -> ""
            }

        is WeaponScroll -> when (grade) {
            GradeScroll.Grade.S -> "weapon_s"
            GradeScroll.Grade.A -> "weapon_a"
            GradeScroll.Grade.B -> "weapon_b"
            GradeScroll.Grade.C -> "weapon_c"
            GradeScroll.Grade.D -> "weapon_d"
        }

        is ArmorScroll -> when (grade) {
            GradeScroll.Grade.S -> "armor_s"
            GradeScroll.Grade.A -> "armor_a"
            GradeScroll.Grade.B -> "armor_b"
            GradeScroll.Grade.C -> "armor_c"
            GradeScroll.Grade.D -> "armor_d"
        }

        else -> ""
    }