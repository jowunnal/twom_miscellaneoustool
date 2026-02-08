package com.jinproject.domain.usecase.droplist

import com.jinproject.domain.entity.Monster
import com.jinproject.domain.entity.MonsterType
import com.jinproject.domain.repository.DropListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 보스 몬스터 목록 조회 UseCase
 * 일반 몬스터를 제외한 보스급 몬스터 목록을 반환
 */
class GetBossMonsterListUseCase @Inject constructor(
    private val dropListRepository: DropListRepository,
) {
    operator fun invoke(): Flow<List<Monster>> =
        dropListRepository.getMonsterList().map { monsters ->
            monsters.filter { it.type !is MonsterType.Normal }
        }
}
