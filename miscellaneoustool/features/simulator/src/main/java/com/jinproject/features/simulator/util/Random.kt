package com.jinproject.features.simulator.util

import kotlin.random.Random

/**
 * 30퍼센트 확률로 장비를 강화하는 함수
 * @param now : 현재 강화 수치
 * @param standard : 장비 강화 기준치, 기준치보다 작으면 무조건 성공
 * @return 강화 결과 수치
 */
internal fun enchantEquipmentBy30percent(now: Int, standard: Int): Int {
    val isEnchantSucceed = Random.nextInt(100) < 30

    return if (now < standard)
        now + 1
    else
        if (isEnchantSucceed)
            now + 1
        else
            0
}