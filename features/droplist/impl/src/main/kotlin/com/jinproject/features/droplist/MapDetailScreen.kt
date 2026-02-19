package com.jinproject.features.droplist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jinproject.features.core.compose.LocalNavigator
import com.jinproject.features.droplist.component.DropListDetail

@Composable
internal fun MapDetailScreen(
    mapName: String,
    viewModel: MapDetailViewModel = hiltViewModel<MapDetailViewModel, MapDetailViewModel.Factory>(
        creationCallback = { factory -> factory.create(mapName = mapName) }
    ),
) {
    val monsterList by viewModel.monsterList.collectAsStateWithLifecycle()
    val navigator = LocalNavigator.current

    DropListDetail(
        mapName = mapName,
        monsterListState = monsterList,
        onNavigateBack = { navigator.goBack() },
    )
}
