package com.jinproject.features.droplist

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jinproject.design_compose.utils.PreviewMiscellaneousToolTheme
import com.jinproject.features.core.AnalyticsEvent
import com.jinproject.features.core.compose.LocalAnalyticsLoggingEvent
import com.jinproject.features.droplist.component.DropListDetail
import com.jinproject.features.droplist.component.MapListPane
import com.jinproject.features.droplist.state.MapState

@Composable
internal fun MapListScreen(
    viewModel: DropListViewModel = hiltViewModel(),
) {
    val dropListUiState by viewModel.dropListUiState.collectAsStateWithLifecycle()

    MapListScreen(
        dropListUiState = dropListUiState,
        selectMap = viewModel::selectMap,
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun MapListScreen(
    dropListUiState: DropListUiState,
    selectMap: (MapState) -> Unit,
) {
    val lazyGridState = rememberLazyGridState()
    val navigator = rememberListDetailPaneScaffoldNavigator<MapState>()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    val localAnalyticsLoggingEvent = LocalAnalyticsLoggingEvent.current

    LaunchedEffect(key1 = dropListUiState.selectedMap) {
        if (dropListUiState.selectedMap != null) {
            dropListUiState.maps.find { it.name == dropListUiState.selectedMap.name }?.let { mapState ->
                navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, mapState)
            }
        }

        localAnalyticsLoggingEvent(AnalyticsEvent.DropListScreen)
    }

    ListDetailPaneScaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                MapListPane(
                    mapListState = dropListUiState.maps,
                    lazyGridState = lazyGridState,
                    onClickItem = { item ->
                        selectMap(item)
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, item)
                    },
                )
            }
        },
        detailPane = {
            AnimatedPane {
                navigator.currentDestination?.content?.let { map ->
                    DropListDetail(
                        mapName = map.name,
                        monsterListState = dropListUiState.monsters,
                        onNavigateBack = {
                            navigator.navigateBack()
                        },
                    )
                }
            }
        },
    )
}

@Composable
@Preview
private fun MapListScreenPreview(
    @PreviewParameter(DropListUiStatePreviewParameter::class)
    dropListUiState: DropListUiState,
) = PreviewMiscellaneousToolTheme {
    MapListScreen(
        dropListUiState = dropListUiState,
        selectMap = {},
    )
}