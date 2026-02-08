package com.jinproject.data.repository.repo

import com.jinproject.data.repository.datasource.CacheDropListDataSource
import com.jinproject.data.repository.model.toCompleteDomainList
import com.jinproject.data.repository.model.toTwomMapList
import com.jinproject.domain.entity.Monster
import com.jinproject.domain.entity.TwomMap
import com.jinproject.domain.repository.DropListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DropListRepositoryImpl @Inject constructor(
    private val dropListDataSource: CacheDropListDataSource,
) : DropListRepository {

    override fun getMapList(): Flow<List<TwomMap>> =
        dropListDataSource.getMapList().map { maps ->
            maps.toTwomMapList()
        }

    override fun getMonsterList(): Flow<List<Monster>> =
        dropListDataSource.getMonsterList().map { monsters ->
            monsters.map { it.toMonsterDomain() }
        }

    override fun getMonsterListWithItems(): Flow<List<Monster>> =
        dropListDataSource.getMonsterListWithItems().map { monstersWithItems ->
            monstersWithItems.map { it.toMonsterDomain() }
        }

    override fun getMonsterListComplete(): Flow<List<Monster>> =
        dropListDataSource.getMonsterListWithItems()
            .combine(dropListDataSource.getMonsterListWithMaps()) { withItems, withMaps ->
                withItems.toCompleteDomainList(withMaps)
            }

    override fun getMonster(name: String): Flow<Monster> =
        dropListDataSource.getMonster(name).map { monster ->
            monster.toMonsterDomain()
        }
}
