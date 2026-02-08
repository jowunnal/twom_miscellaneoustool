package com.jinproject.data.repository.model

import com.jinproject.domain.entity.Monster
import com.jinproject.domain.entity.MonsterType
import com.jinproject.domain.entity.TwomMap

data class MonsterModel(
    val name: String,
    val level: Int,
    val genTime: Int,
    val imgName: String,
    val type: String,
) {
    /** 기본 정보만으로 Domain 변환 (아이템, 맵 없음) */
    fun toMonsterDomain() = Monster(
        name = name,
        level = level,
        hp = 0,
        type = MonsterType.findByBossTypeName(type),
        genTime = genTime,
        existedMap = emptyList(),
        dropItems = emptyList(),
        imageName = imgName,
    )
}

data class MonsterWithItemsModel(
    val monster: MonsterModel,
    val items: List<GetItemDomainFactory>
) {
    /** 아이템 포함, 맵 없음 */
    fun toMonsterDomain() = Monster(
        name = monster.name,
        level = monster.level,
        hp = 0,
        type = MonsterType.findByBossTypeName(monster.type),
        genTime = monster.genTime,
        existedMap = emptyList(),
        dropItems = items.map { it.getDomainFactory().create() },
        imageName = monster.imgName,
    )

    /** 아이템 포함, 맵 지정 */
    fun toMonsterDomain(existedMap: List<TwomMap>) = Monster(
        name = monster.name,
        level = monster.level,
        hp = 0,
        type = MonsterType.findByBossTypeName(monster.type),
        genTime = monster.genTime,
        existedMap = existedMap,
        dropItems = items.map { it.getDomainFactory().create() },
        imageName = monster.imgName,
    )
}

data class MonsterWithMapsModel(
    val monster: MonsterModel,
    val maps: List<MapModel>
) {
    /** 맵 포함, 아이템 없음 */
    fun toMonsterDomain() = Monster(
        name = monster.name,
        level = monster.level,
        hp = 0,
        type = MonsterType.findByBossTypeName(monster.type),
        genTime = monster.genTime,
        existedMap = maps.toTwomMapList(),
        dropItems = emptyList(),
        imageName = monster.imgName,
    )
}

data class MonsterWithItemsAndMapsModel(
    val monster: MonsterModel,
    val items: List<GetItemDomainFactory>,
    val maps: List<MapModel>
) {
    /** 아이템 + 맵 모두 포함 */
    fun toMonsterDomain() = Monster(
        name = monster.name,
        level = monster.level,
        hp = 0,
        type = MonsterType.findByBossTypeName(monster.type),
        genTime = monster.genTime,
        existedMap = maps.toTwomMapList(),
        dropItems = items.map { it.getDomainFactory().create() },
        imageName = monster.imgName,
    )
}

/** MonsterWithItemsModel 리스트와 MonsterWithMapsModel 리스트를 결합하여 Complete 변환 */
fun List<MonsterWithItemsModel>.toCompleteDomainList(mapsData: List<MonsterWithMapsModel>): List<Monster> =
    map { monsWithItems ->
        val maps = mapsData.find { it.monster.name == monsWithItems.monster.name }
            ?.maps?.toTwomMapList() ?: emptyList()
        monsWithItems.toMonsterDomain(maps)
    }
