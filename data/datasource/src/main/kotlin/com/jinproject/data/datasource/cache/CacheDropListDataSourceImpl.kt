package com.jinproject.data.datasource.cache

import com.jinproject.data.datasource.cache.database.dao.DropListDao
import com.jinproject.data.datasource.cache.database.entity.toItemDataModelList
import com.jinproject.data.datasource.cache.database.entity.toMapDataModel
import com.jinproject.data.repository.datasource.CacheDropListDataSource
import com.jinproject.data.repository.model.MapModel
import com.jinproject.data.repository.model.MonsterModel
import com.jinproject.data.repository.model.MonsterWithItemsModel
import com.jinproject.data.repository.model.MonsterWithMapsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CacheDropListDataSourceImpl @Inject constructor(
    private val dropListDao: DropListDao,
) : CacheDropListDataSource {

    override fun getMapList(): Flow<List<MapModel>> = dropListDao.getMapList().map { maps ->
        maps.map { map ->
            map.toMapDataModel()
        }
    }

    override fun getMapListByMonster(monsterName: String): Flow<List<MapModel>> =
        dropListDao.getMapsByMonsterName(monsterName).map { maps ->
            maps.map { map ->
                map.toMapDataModel()
            }
        }

    override fun getMonsterList(): Flow<List<MonsterModel>> =
        dropListDao.getAllMonsterList()
            .map { monsters ->
                monsters.map { monster ->
                    monster.toMonsterModel()
                }
            }

    override fun getMonsterListWithItems(): Flow<List<MonsterWithItemsModel>> =
        dropListDao.getMonsterListWithDropItemList()
            .map { monstersWithItems ->
                monstersWithItems.map { (monster, items) ->
                    MonsterWithItemsModel(
                        monster = monster.toMonsterModel(),
                        items = items.toItemDataModelList()
                    )
                }
            }

    override fun getMonsterListWithMaps(): Flow<List<MonsterWithMapsModel>> =
        dropListDao.getMonsterListWithMaps()
            .map { monstersWithMaps ->
                monstersWithMaps.map { (monster, maps) ->
                    MonsterWithMapsModel(
                        monster = monster.toMonsterModel(),
                        maps = maps.map { it.toMapDataModel() }
                    )
                }
            }

    override fun getMonster(name: String): Flow<MonsterModel> =
        dropListDao.getMonsInfo(name).map { monster ->
            monster.toMonsterModel()
        }

    override fun getMonsterListWithItemsByMap(map: String): Flow<List<MonsterWithItemsModel>> =
        dropListDao.getMonsterListFromMap(map).map { response ->
            response.map { (monster, items) ->
                MonsterWithItemsModel(
                    monster = monster.toMonsterModel(),
                    items = items.toItemDataModelList()
                )
            }
        }

    private fun com.jinproject.data.datasource.cache.database.entity.Monster.toMonsterModel() =
        MonsterModel(
            name = monsName,
            level = monsLevel,
            genTime = monsGtime,
            imgName = monsImgName,
            type = monsType,
        )
}
