package com.jinproject.features.simulator

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.jinproject.domain.model.Stat
import com.jinproject.features.simulator.model.Equipment
import com.jinproject.features.simulator.model.ItemOption
import com.jinproject.features.simulator.model.SimulatorState
import com.jinproject.features.simulator.model.Weapon
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal class EquipmentListPreviewParameters : PreviewParameterProvider<ImmutableList<Equipment>> {
    val data = persistentListOf(
        Weapon(
            name = "버닝블레이드",
            level = 46,
            options = listOf(
                ItemOption(name = Stat.HR, value = 8.0f),
                ItemOption(name = Stat.CRI, value = 2.0f),
                ItemOption(name = Stat.STATSTR, value = 4.0f),
            ),
            damage = 10..70,
            speed = 1.2f,
            enchantNumber = 0,
            imgName = "img_blade",
            uuid = "0L",
        ),
        Weapon(
            name = "임페리얼보우",
            level = 46,
            options = listOf(
                ItemOption(name = Stat.STATDEX, value = 3.0f),
                ItemOption(name = Stat.CRI, value = 3.0f)
            ),
            damage = 6..54,
            speed = 1.2f,
            enchantNumber = 3,
            imgName = "imperial_bow",
            uuid = "1L",
        ),
        Weapon(
            name = "소울이터",
            level = 48,
            options = listOf(
                ItemOption(name = Stat.STATINT, value = 14.0f),
                ItemOption(name = Stat.MP, value = 20.0f)
            ),
            damage = 6..72,
            speed = 1.2f,
            enchantNumber = 6,
            imgName = "soul_eater",
            uuid = "2L",
        )
    )
    override val values: Sequence<ImmutableList<Equipment>> = sequenceOf(data)
}

internal class SimulatorStatePreviewParameters: PreviewParameterProvider<SimulatorState> {
    private val data = EquipmentListPreviewParameters().data

    override val values: Sequence<SimulatorState>
        get() = sequenceOf(
            SimulatorState(
                ownedItems = data,
                availableItems = data,
            )
        )
}