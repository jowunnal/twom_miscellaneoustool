package com.jinproject.domain.usecase.droplist

import com.jinproject.domain.entity.Monster
import com.jinproject.domain.entity.TwomMap
import com.jinproject.domain.repository.DropListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMonsterListCompleteByMapUseCase @Inject constructor(
    private val dropListRepository: DropListRepository,
) {
    operator fun invoke(): Flow<Map<TwomMap, List<Monster>>> {
        return dropListRepository.getMonsterListComplete().map { monsters ->
            monsters
                .flatMap { monster -> monster.existedMap.map { map -> map to monster } }
                .groupBy({ it.first }, { it.second })
        }
    }
}