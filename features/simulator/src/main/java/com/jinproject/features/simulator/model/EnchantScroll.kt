package com.jinproject.features.simulator.model

import com.jinproject.domain.entity.item.GradeScroll
import com.jinproject.domain.entity.item.Scroll
import com.jinproject.domain.entity.item.WeaponScroll
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed class EnchantScroll : Item(), EnchantScrollDomainFactory {
    @Serializable
    @SerialName("WeaponS")
    internal data class WeaponS(
        override val name: String = "무기S",
        override val imgName: String = "weapon_s",
    ) : EnchantScroll() {
        override fun toDomainModel(): Scroll = WeaponScroll(
            grade = GradeScroll.Grade.S
        )
    }

    @Serializable
    internal data class WeaponA(
        override val name: String = "무기A",
        override val imgName: String = "weapon_a",
    ) : EnchantScroll() {
        override fun toDomainModel(): Scroll = WeaponScroll(
            grade = GradeScroll.Grade.A
        )
    }

    @Serializable
    internal data class WeaponB(
        override val name: String = "무기B",
        override val imgName: String = "weapon_b",
    ) : EnchantScroll() {
        override fun toDomainModel(): Scroll = WeaponScroll(
            grade = GradeScroll.Grade.B
        )
    }

    @Serializable
    internal data class WeaponC(
        override val name: String = "무기C",
        override val imgName: String = "weapon_c",
    ) : EnchantScroll() {
        override fun toDomainModel(): Scroll = WeaponScroll(
            grade = GradeScroll.Grade.C
        )
    }

    @Serializable
    internal data class WeaponD(
        override val name: String = "무기D",
        override val imgName: String = "weapon_d",
    ) : EnchantScroll() {
        override fun toDomainModel(): Scroll = WeaponScroll(
            grade = GradeScroll.Grade.D
        )
    }
}

internal fun Int.findEnchantScroll() = when (this) {
    in 40..49 -> EnchantScroll.WeaponS()
    in 30..39 -> EnchantScroll.WeaponA()
    in 20..29 -> EnchantScroll.WeaponB()
    in 10..19 -> EnchantScroll.WeaponC()
    in 0..9 -> EnchantScroll.WeaponD()
    else -> throw IllegalStateException("$this is not allowed level range")
}

internal interface EnchantScrollDomainFactory {
    fun toDomainModel(): Scroll
}


/*
@Serializable
data object ArmorS : EnchantScroll(
    name = "방어구S",
    imgName = "armor_s"
)

@Serializable
data object ArmorA : EnchantScroll(
    name = "방어구A",
    imgName = "armor_a"
)

@Serializable
data object ArmorB : EnchantScroll(
    name = "방어구B",
    imgName = "armor_b"
)

@Serializable
data object ArmorC : EnchantScroll(
    name = "방어구C",
    imgName = "armor_c"
)

@Serializable
data object ArmorD : EnchantScroll(
    name = "방어구D",
    imgName = "armor_d"
)*/