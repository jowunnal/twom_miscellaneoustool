package com.jinproject.data.datasource.cache

import android.util.Log
import androidx.datastore.core.DataStore
import com.jinproject.core.util.runIf
import com.jinproject.data.SimulatorPreferences
import com.jinproject.data.datasource.cache.database.dao.SimulatorDao
import com.jinproject.data.datasource.cache.mapper.toEquipment
import com.jinproject.data.datasource.cache.mapper.toEquipments
import com.jinproject.data.datasource.cache.mapper.toItemInfo
import com.jinproject.data.repository.datasource.CacheSimulatorDataSource
import com.jinproject.data.repository.model.Equipment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.io.IOException
import javax.inject.Inject

class CacheSimulatorDataSourceImpl @Inject constructor(
    private val simulatorDao: SimulatorDao,
    private val prefs: DataStore<SimulatorPreferences>,
) : CacheSimulatorDataSource {

    val data: Flow<SimulatorPreferences> = prefs.data
        .catch { exception ->
            if (exception is IOException) {
                emit(SimulatorPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }

    override fun getItemInfo(itemName: String): Flow<Equipment> =
        simulatorDao.getItemInfo(itemName).map { itemInfo ->
            val entry = itemInfo.entries.first()

            entry.toEquipment()
        }

    override fun getItemInfos(): Flow<List<Equipment>> =
        simulatorDao.getItemInfos().map { itemWithInfosList ->
            itemWithInfosList.toEquipments()
        }

    override fun getOwnedItems(): Flow<List<Equipment>> =
        data.onEach { Log.d("test", "new OwnedItems has been published") }.map { prefs ->
            prefs.ownedItemsList.toEquipments()
        }

    override suspend fun addItemOnOwnedItemList(equipment: Equipment) {
        prefs.updateData { prefs ->
            prefs
                .toBuilder()
                .addOwnedItems(equipment.toItemInfo())
                .build()
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

    override suspend fun replaceOwnedItem(equipment: Equipment) {
        prefs.updateData { prefs ->
            val origin = data.first().ownedItemsList
            val idx = origin.indexOfFirst { owned ->
                owned.uuid!!.contentEquals(equipment.uuid)
            }

            prefs.toBuilder().runIf(idx != -1) {
                setOwnedItems(idx, equipment.toItemInfo())
            }.build()
        }
    }

    override suspend fun updateOwnedItems(items: List<Equipment>) {
        prefs.updateData { prefs ->
            prefs.toBuilder()
                .clearOwnedItems()
                .addAllOwnedItems(items.map { it.toItemInfo() })
                .build()
        }
    }
}