package com.jinproject.domain.entity.item

import kotlin.random.Random

/**
 * 강화 시스템 인터페이스
 *
 * @param enchantProbability : 강화 확률
 * @param enchantNumber : 현재 강화 수치
 */
interface Enchant {
    val enchantProbability: Int
    var enchantNumber: Int

    fun increaseEnchantNumber() {
        enchantNumber += 1
    }

    /**
     * 아이템을 주문서로 강화할 수 있는지 여부를 반환
     * @param scroll : 강화 주문서
     * @return 강화 가능 여부
     */
    fun isAvailableScroll(scroll: Scroll): Boolean

    /**
     * 강화 실행 핵심 로직
     * @return 강화 성공 여부
     */
    fun process(): Boolean {
        val isEnchantSucceed = Random.nextInt(100) < enchantProbability

        return isEnchantSucceed
    }

    /**
     * 강화 실행에 따른 결과를 반환
     * @return 강화 결과(성공시 강화된 장비, 실패시 null)
     */
    fun enchant(): Equipment?

}