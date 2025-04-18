package com.jinproject.domain.usecase.enchant

import com.jinproject.domain.entity.item.EnchantableEquipment
import com.jinproject.domain.entity.item.GradeScroll
import com.jinproject.domain.entity.item.ItemType
import com.jinproject.domain.entity.item.Weapon
import com.jinproject.domain.entity.item.WeaponScroll
import com.jinproject.domain.usecase.simulator.EnchantEquipmentUseCase
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import java.util.UUID

class EnchantEquipmentUseCaseTest : BehaviorSpec() {
    private val enchantEquipmentUseCase = EnchantEquipmentUseCase()

    init {
        given("5강 버닝블레이드가 있을 때") {
            val item = Weapon(
                stats = mapOf("명중" to 7f, "힘" to 12f, "크리티컬" to 4f),
                limitedLevel = 46,
                name = "버닝블레이드",
                price = 0,
                type = ItemType.RARE,
                speed = 10,
                damageRange = 10..70,
                uuid = UUID.randomUUID().toString()
            ).apply {
                enchantNumber = 5
            }

            `when`("S등급의 무기 주문서로 강화를 한다면") {
                val weaponS = WeaponScroll(grade = GradeScroll.Grade.S)
                val enchantResult = enchantEquipmentUseCase.invoke(item = item, scroll = weaponS)
                then("성공하여 6강이 된다.") {
                    (enchantResult as EnchantableEquipment).enchantNumber shouldBe 6
                }
            }

            `when`("A등급의 무기 주문서로 강화를 한다면") {
                val weaponA = WeaponScroll(grade = GradeScroll.Grade.A)
                val enchantResult =
                    shouldThrow<EnchantEquipmentUseCase.EnchantFailedException.NotAllowedScroll> {
                        enchantEquipmentUseCase.invoke(item = item, scroll = weaponA)
                    }
                then("버닝 블레이드는 A등급의 주문서로 강화를 할 수 없다.") {
                    enchantResult shouldBe EnchantEquipmentUseCase.EnchantFailedException.NotAllowedScroll(
                        "[$weaponA] 주문서로 [$item] 을 강화할 수 없습니다."
                    )
                }
            }
        }
    }
}