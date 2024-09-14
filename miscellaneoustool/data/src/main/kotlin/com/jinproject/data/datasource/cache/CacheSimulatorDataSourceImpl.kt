package com.jinproject.data.datasource.cache

import androidx.datastore.core.DataStore
import com.jinproject.data.ItemInfo
import com.jinproject.data.SimulatorPreferences
import com.jinproject.data.datasource.cache.database.dao.SimulatorDao
import com.jinproject.data.datasource.cache.database.entity.Equipment
import com.jinproject.data.model.ItemDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class CacheSimulatorDataSourceImpl @Inject constructor(
    private val simulatorDao: SimulatorDao,
    private val simulatorPreferences: DataStore<SimulatorPreferences>,
) : CacheSimulatorDataSource {
    override fun getItemInfo(itemName: String): Flow<ItemDetail> =
        simulatorDao.getItemInfo(itemName).map { item ->
            val equip = item.keys.first()

            ItemDetail(
                name = equip.name,
                imgName = equip.img_name,
                level = equip.level,
                stat = mutableMapOf<String, Float>().apply {
                    runCatching {
                        item[equip]?.forEach { info ->
                            put(info.type, info.value.toFloat())
                        }
                    }.getOrDefault(emptyMap<String, Float>())
                },
            )
        }

    override fun getAvailableItem(): Flow<List<Equipment>> = simulatorDao.getAvailableItem()

    private val simulatorOwnedData: Flow<SimulatorPreferences> = simulatorPreferences.data
        .catch { exception ->
            if (exception is IOException) {
                emit(SimulatorPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }

    override fun getOwnedItems(): Flow<List<ItemInfo>> =
        simulatorOwnedData.map { prefs ->
            prefs.ownedItemsList
        }

    override suspend fun addItemOnOwnedItemList(item: ItemInfo) {
        simulatorPreferences.updateData { prefs ->
            prefs.toBuilder().addOwnedItems(item).build()
        }
    }

    override suspend fun removeItemOnOwnedItemList(uuid: String) {
        simulatorPreferences.updateData { prefs ->
            val idx = simulatorOwnedData.first().ownedItemsList.indexOfFirst { owned ->
                owned.uuid!!.contentEquals(uuid)
            }
            prefs.toBuilder().removeOwnedItems(idx).build()
        }
    }

    override suspend fun replaceOwnedItem(item: ItemInfo) {
        simulatorPreferences.updateData { prefs ->
            val idx = simulatorOwnedData.first().ownedItemsList.indexOfFirst { owned ->
                owned.uuid!!.contentEquals(item.uuid)
            }
            prefs.toBuilder()
                .setOwnedItems(idx, item)
                .build()
        }
    }
}