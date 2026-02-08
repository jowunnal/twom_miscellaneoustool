package com.jinproject.features.droplist

import androidx.activity.compose.BackHandler
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.utils.PreviewMiscellaneousToolTheme
import com.jinproject.features.core.AnalyticsEvent
import com.jinproject.features.core.compose.LocalAnalyticsLoggingEvent
import com.jinproject.features.droplist.component.DropListDetail
import com.jinproject.features.droplist.component.MapListPane
import com.jinproject.features.droplist.component.SearchMapContent
import com.jinproject.features.droplist.state.MapState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun MapListScreen(
    viewModel: DropListViewModel = hiltViewModel(),
) {
    val dropListUiState by viewModel.dropListUiState.collectAsStateWithLifecycle()
    val navigator = rememberListDetailPaneScaffoldNavigator<MapState>()

    val localAnalyticsLoggingEvent = LocalAnalyticsLoggingEvent.current

    LaunchedEffect(dropListUiState.selectedMap) {
        if (dropListUiState.selectedMap != MapState.getInitValue()) {
            navigator.navigateTo(
                ListDetailPaneScaffoldRole.Detail,
                dropListUiState.selectedMap
            )
        }

        localAnalyticsLoggingEvent(AnalyticsEvent.DropListScreen)
    }

    MapListScreen(
        dropListUiState = dropListUiState,
        navigator = navigator,
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun MapListScreen(
    dropListUiState: DropListUiState,
    navigator: ThreePaneScaffoldNavigator<MapState> = rememberListDetailPaneScaffoldNavigator<MapState>(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) {
    val lazyGridState = rememberLazyGridState()


    BackHandler(navigator.canNavigateBack()) {
        coroutineScope.launch { navigator.navigateBack() }
    }

    var transitionState by remember {
        mutableStateOf(false)
    }

    val maps by remember(
        dropListUiState.monstersGroupedByMap,
        dropListUiState.mapListWhereSelectedMonsterLive,
        dropListUiState.searchQuery
    ) {
        derivedStateOf {
            if (dropListUiState.searchQuery.isBlank())
                dropListUiState.mapStateList
            else
                dropListUiState.mapListWhereSelectedMonsterLive.maps.ifEmpty { dropListUiState.mapStateList }
        }
    }

    ListDetailPaneScaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                Column(
                    modifier = Modifier
                        .focusable(true)
                        .padding(horizontal = 12.dp),
                ) {
                    VerticalSpacer(16.dp)
                    SharedTransitionLayout {
                        SearchMapContent(
                            dropListUiState = dropListUiState,
                            transitionState = transitionState,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            setTransitionState = { bool ->
                                transitionState = bool
                            },
                        )
                    }
                    MapListPane(
                        mapListState = maps,
                        lazyGridState = lazyGridState,
                        onClickItem = { item ->
                            dropListUiState.updateSelectedMap(item)
                        },
                    )
                }
            }
        },
        detailPane = {
            AnimatedPane {
                navigator.currentDestination?.contentKey?.let { map ->
                    DropListDetail(
                        mapName = map.name,
                        monsterListState = dropListUiState.monsterListExistInSelectedMap.monsters,
                        onNavigateBack = {
                            coroutineScope.launch { navigator.navigateBack() }
                        },
                    )
                }
            }
        },
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
@Preview
private fun MapListScreenPreview(
    @PreviewParameter(DropListUiStatePreviewParameter::class)
    dropListUiState: DropListUiState,
) = PreviewMiscellaneousToolTheme {
    MapListScreen(
        dropListUiState = dropListUiState,
    )
}