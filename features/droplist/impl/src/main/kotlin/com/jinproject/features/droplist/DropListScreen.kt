package com.jinproject.features.droplist

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.utils.PreviewMiscellaneousToolTheme
import com.jinproject.features.core.AnalyticsEvent
import com.jinproject.features.core.compose.LocalAnalyticsLoggingEvent
import com.jinproject.features.core.compose.LocalNavigator
import com.jinproject.features.droplist.component.MapListPane
import com.jinproject.features.droplist.component.SearchMapContent
import com.jinproject.features.droplist.state.MapState

@Composable
internal fun MapListScreen(
    viewModel: DropListViewModel = hiltViewModel(),
) {
    val dropListUiState by viewModel.dropListUiState.collectAsStateWithLifecycle()
    val navigator = LocalNavigator.current
    val localAnalyticsLoggingEvent = LocalAnalyticsLoggingEvent.current

    LaunchedEffect(Unit) {
        localAnalyticsLoggingEvent(AnalyticsEvent.DropListScreen)
    }

    MapListScreen(
        dropListUiState = dropListUiState,
        navigateToDetail = { mapState ->
            navigator.replaceAbove(DropListRoute.MapList, DropListRoute.MapDetail(mapState.name))
        },
    )
}

@Composable
private fun MapListScreen(
    dropListUiState: DropListUiState,
    navigateToDetail: (MapState) -> Unit = {},
) {
    val lazyGridState = rememberLazyGridState()

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
                navigateToDetail(item)
            },
        )
    }
}

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
