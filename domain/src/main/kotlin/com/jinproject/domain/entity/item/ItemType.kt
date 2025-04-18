package com.jinproject.domain.entity.item

/**
 * 아이템 타입 도메인 엔티티
 *
 * 아이모에 존재하는 아이템 타입 종류
 *
 * 1. 일반
 * 2. 레어
 * 3. 유니크
 * 4. 전설
 */
enum class ItemType {
    NORMAL,
    RARE,
    UNIQUE,
    LEGENDARY,
}