package com.jinproject.data.datasource.cache

import androidx.datastore.core.DataStore
import com.jinproject.data.SimulatorPreferences
import com.jinproject.data.datasource.cache.database.dao.SimulatorDao
import com.jinproject.data.datasource.cache.database.entity.toEquipmentsDataModel
import com.jinproject.data.datasource.cache.mapper.toItemInfo
import com.jinproject.data.datasource.cache.mapper.toItemInfosDataModel
import com.jinproject.data.repository.datasource.CacheSimulatorDataSource
import com.jinproject.data.repository.model.Equipment
import com.jinproject.data.repository.model.ItemDetail
import com.jinproject.data.repository.model.ItemInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class CacheSimulatorDataSourceImpl @Inject constructor(
    private val simulatorDao: SimulatorDao,
    override val prefs: DataStore<SimulatorPreferences>,
) : CacheSimulatorDataSource<SimulatorPreferences> {

    override val data: Flow<SimulatorPreferences> = prefs.data
        .catch { exception ->
            if (exception is IOException) {
                emit(SimulatorPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }

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

    override fun getAvailableItem(): Flow<List<Equipment>> =
        simulatorDao.getAvailableItem().map { it.toEquipmentsDataModel() }

    override fun getOwnedItems(): Flow<List<ItemInfo>> =
        data.map { prefs ->
            prefs.ownedItemsList.toItemInfosDataModel()
        }

    override suspend fun addItemOnOwnedItemList(item: ItemInfo) {
        prefs.updateData { prefs ->
            prefs.toBuilder().addOwnedItems(item.toItemInfo()).build()
        }
    }

    override suspend fun removeItemOnOwnedItemList(uuid: String) {
        prefs.updateData { prefs ->
            val idx = data.first().ownedItemsList.indexOfFirst { owned ->
                owned.uuid!!.contentEquals(uuid)
            }
            prefs.toBuilder().removeOwnedItems(idx).build()
        }
    }

    override suspend fun replaceOwnedItem(item: ItemInfo) {
        prefs.updateData { prefs ->
            val idx = data.first().ownedItemsList.indexOfFirst { owned ->
                owned.uuid!!.contentEquals(item.uuid)
            }
            prefs.toBuilder()
                .setOwnedItems(idx, item.toItemInfo())
                .build()
        }
    }
}