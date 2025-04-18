package com.jinproject.domain.usecase.simulator

import com.jinproject.domain.entity.item.Enchant
import com.jinproject.domain.entity.item.Equipment
import com.jinproject.domain.entity.item.Scroll
import javax.inject.Inject

class EnchantEquipmentUseCase @Inject constructor() {
    /**
     * @return 강화 결과(성공시 강화된 아이템, 실패시 null)
     * @exception EnchantFailedException 올바르지 않은 주문서로 아이템을 강화하려는 경우
     */
    operator fun invoke(item: Equipment, scroll: Scroll): Equipment? {
        return if (item is Enchant) {
            if (item.isAvailableScroll(scroll))
                item.enchant()
            else
                throw EnchantFailedException.NotAllowedScroll("[$scroll] 주문서로 [$item] 을 강화할 수 없습니다.")
        } else
            throw EnchantFailedException.NotAllowedEnchant("[$item] 은 강화 가능한 아이템이 아닙니다.")
    }

    sealed class EnchantFailedException : Exception() {
        data class NotAllowedScroll(override val message: String) : EnchantFailedException()
        data class NotAllowedEnchant(override val message: String) : EnchantFailedException()
    }
}