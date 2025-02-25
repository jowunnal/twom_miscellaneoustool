package com.jinproject.features.simulator.util

import kotlin.random.Random

/**
 * 30퍼센트 확률로 장비를 강화하는 함수
 * @param now : 현재 강화 수치
 * @param standard : 장비 강화 기준치, 기준치보다 작으면 무조건 성공
 * @return 강화 결과
 */
internal fun enchantEquipment(now: Int, standard: Int): Boolean {
    val percent = when {
        now == 6 -> 30
        now == 7 -> 20
        now == 8 -> 10
        now > 9 -> 5
        else -> 100
    }
    val isEnchantSucceed = Random.nextInt(100) < percent

    return if (now < standard)
        true
    else
        if (isEnchantSucceed)
            true
        else
            false
}