package com.jinproject.domain.model

data class MapModel(
    val name: MapField,
    val imgName: String
)

enum class MapField(val nameInKR: String, val nameInOthers: String) {
    WOODY_WEEDY_FOREST("우디위디숲", "Woody Forest"),
    MUSHROOM_MARSHLAND("버섯늪지", "Mushroom Marshland"),
    MUSHROOM_SPORE_CAVE("머쉬룸스포어", "Mushroom Spore Cave"),
    WINGFRIL_ISLAND_BEACH("윙프릴섬해변", "Wingfril Island Beach"),
    PIRATE_SHIP("해적선", "Pirate Ship"),
    LIGHT_HOUSE_DUNGEON_1F("등대던전1층", "Light House 1F"),
    LIGHT_HOUSE_DUNGEON_2F("등대던전2층", "Light House 2F"),
    LIGHT_HOUSE_DUNGEON_3F("등대던전3층", "Light House 3F"),
    LIGHT_HOUSE_DUNGEON_4F("등대던전4층", "Light House 4F"),
    LIGHT_HOUSE_DUNGEON_5F("등대던전5층", "Light House 5F"),
    TEMPLE_OF_WINGFRIL("빛이들지않는신전", "Temple of Wingfril"),
    FOREST_OF_GRAVE("해지는노을숲", "Forest of Grave"),
    KATARU_MOUNTAINS("카타르산맥", "Kataru Mountains"),
    LANOS_PLAIN("라노스평원", "Lanos Plain"),
    FOREST_WITH_RUINS("폐허가있는숲", "Forest with Ruins"),
    SIRAS_DESERT("모래무덤골짜기", "Desert Valley"),
    LANOS_DESERT("건조한초원", "Arid Grassland"),
    HOT_SAND_PLAINS("뜨거운모래사막", "Hot Sand Plains"),
    POLLUTED_FOREST("오염된 숲", "Polluted Forest"),
    MARSH_OF_DEATH("죽음의 늪", "Marsh of Death"),
    MAZE_FOREST("숲의 미궁", "Maze Forest"),
    SKY_CASTLE_1F("고대의누각", "Ancient Palace"),
    SKY_CASTLE_2F_EAST("하늘성채-동부", "Eastern Sky Castle"),
    SKY_CASTLE_2F_WEST("하늘성채-서부", "Western Sky Castle"),
    SKY_CASTLE_3F_EAST("알수없는 미로", "Unknown Maze"),
    SKY_CASTLE_3F_WEST("돌무더기 요새", "Stone Fortress"),
    FALLEN_TEMPLE("타락한 신전", "Fallen Temple"),
    ISLOT_LAB("이슬롯의 실험실", "Islot\'s Lab"),
    ISLOT_TEMPLE("이슬롯의 신전", "Islot\'s Temple"),
    ANGUISH_ALTAR("비탄의제단", "Anguish Altar"),
    LUNARF("루나프", "Lunarf"),
    MORPHOSIS_ROOT("모르포시즈 뿌리", "Morphosis Root"),
    MORPHOSIS_GARDEN("모르포시즈 정원", "Morphosis Garden");

    companion object {
        fun findByMapName(name: String) = when (name) {
            "우디위디숲" -> WOODY_WEEDY_FOREST
            "버섯늪지" -> MUSHROOM_MARSHLAND
            "머쉬룸스포어" -> MUSHROOM_SPORE_CAVE
            "윙프릴섬해변" -> WINGFRIL_ISLAND_BEACH
            "해적선" -> PIRATE_SHIP
            "등대던전1층" -> LIGHT_HOUSE_DUNGEON_1F
            "등대던전2층" -> LIGHT_HOUSE_DUNGEON_2F
            "등대던전3층" -> LIGHT_HOUSE_DUNGEON_3F
            "등대던전4층" -> LIGHT_HOUSE_DUNGEON_4F
            "등대던전5층" -> LIGHT_HOUSE_DUNGEON_5F
            "빛이들지않는신전" -> TEMPLE_OF_WINGFRIL
            "해지는노을숲" -> FOREST_OF_GRAVE
            "카타르산맥" -> KATARU_MOUNTAINS
            "라노스평원" -> LANOS_PLAIN
            "폐허가있는숲" -> FOREST_WITH_RUINS
            "모래무덤골짜기" -> SIRAS_DESERT
            "건조한초원" -> LANOS_DESERT
            "뜨거운모래사막" -> HOT_SAND_PLAINS
            "오염된 숲" -> POLLUTED_FOREST
            "죽음의 늪" -> MARSH_OF_DEATH
            "숲의 미궁" -> MAZE_FOREST
            "고대의누각" -> SKY_CASTLE_1F
            "하늘성채-동부" -> SKY_CASTLE_2F_EAST
            "하늘성채-서부" -> SKY_CASTLE_2F_WEST
            "알수없는 미로" -> SKY_CASTLE_3F_EAST
            "돌무더기 요새" -> SKY_CASTLE_3F_WEST
            "타락한 신전" -> FALLEN_TEMPLE
            "이슬롯의 실험실" -> ISLOT_LAB
            "이슬롯의 신전" -> ISLOT_TEMPLE
            "비탄의제단" -> ANGUISH_ALTAR
            "루나프" -> LUNARF
            "모르포시즈 뿌리" -> MORPHOSIS_ROOT
            "모르포시즈 정원" -> MORPHOSIS_GARDEN
            else -> throw IllegalArgumentException("잘못된 맵이름: $name 입니다.")
        }
    }

}
