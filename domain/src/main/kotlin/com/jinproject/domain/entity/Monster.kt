package com.jinproject.domain.entity

import com.jinproject.domain.entity.item.Item
import com.jinproject.domain.model.MonsterType

/**
 * 몬스터 도메인 엔티티
 *
 * 아이모에 존재하는 몬스터를 구성하기 위한 핵심 속성은 무엇인가?
 *
 * 1. 몬스터 이름
 * 2. 몬스터 레벨
 * 3. 몬스터의 세부 타입(일반, 네임드, 보스, 대형보스)
 * 4. 몬스터의 젠 타임(죽은 뒤 재 생성 까지 걸리는 시간)
 * 4. 몬스터가 출현 가능한 맵 --> 몬스터는 특정 맵에서 출현한다.
 * 5. 몬스터가 드랍하는 아이템 --> 몬스터는 아이템을 드랍한다.
 */
data class Monster(
    val name: String,
    val level: Int,
    val type: MonsterType,
    val genTime: Int,
    val existedMap: List<Map>,
    val dropItems: List<Item>,
)

/**
 *  맵 도메인 엔티티
 *
 *  아이모에 존재하는 맵(필드)의 핵심 속성은 무엇인가?
 *
 *  1. 맵 이름
 *
 */
data class Map(
    val name: String,
)