package com.jinproject.domain.usecase.repository

import com.jinproject.domain.entity.item.EnchantableEquipment
import com.jinproject.domain.entity.item.Equipment
import com.jinproject.domain.entity.item.ItemType
import com.jinproject.domain.entity.item.Weapon
import com.jinproject.domain.repository.SimulatorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeSimulatorRepository: SimulatorRepository {
    val items: MutableList<EnchantableEquipment> = mutableListOf(
        Weapon(
            name = "버닝블레이드",
            limitedLevel = 46,
            stats = mapOf("명중" to 7f, "힘" to 12f, "크리티컬" to 4f),
            speed = 10,
            price = 0,
            uuid = "",
            type = ItemType.RARE,
            imageName = "burning_blade",
            damageRange = 40..60
        ),
        Weapon(
            name = "인퀴지션",
            limitedLevel = 48,
            stats = mapOf("명중" to 11f, "힘" to 5f, "크리티컬" to 1f),
            speed = 6,
            price = 0,
            uuid = "",
            type = ItemType.RARE,
            imageName = "burning_blade",
            damageRange = 70..80
        ),
    )

    override fun getEnchantableItems(): Flow<List<Equipment>> = flow {
        emit(
            listOf(
                Weapon(
                    name = "버닝블레이드",
                    limitedLevel = 46,
                    stats = mapOf("명중" to 7f, "힘" to 12f, "크리티컬" to 4f),
                    speed = 10,
                    price = 0,
                    uuid = "",
                    type = ItemType.RARE,
                    imageName = "burning_blade",
                    damageRange = 40..60
                ),
                Weapon(
                    name = "인퀴지션",
                    limitedLevel = 48,
                    stats = mapOf("명중" to 11f, "힘" to 5f, "크리티컬" to 1f),
                    speed = 6,
                    price = 0,
                    uuid = "",
                    type = ItemType.RARE,
                    imageName = "burning_blade",
                    damageRange = 70..80
                ),
            )
        )
    }

    override fun getOwnedItems(): Flow<List<Equipment>> = flow {
        emit(items)
    }

    override suspend fun addItemOnOwnedItemList(equipment: EnchantableEquipment) {
        items.add(equipment)
    }

    override suspend fun removeItemOnOwnedItemList(uuid: String) {
        items.removeIf { it.uuid == uuid }
    }

    override suspend fun replaceOwnedItem(equipment: EnchantableEquipment) {
        val idx = items.indexOfFirst { it.uuid == equipment.uuid }
        items[idx] = equipment
    }

    override suspend fun updateOwnedItem(items: List<EnchantableEquipment>) {
        this.items.apply {
            clear()
            addAll(items)
        }
    }
}