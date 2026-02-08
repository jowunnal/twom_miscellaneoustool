package com.jinproject.features.collection

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jinproject.design_compose.component.bar.BackButtonSearchAppBar
import com.jinproject.design_compose.component.layout.DefaultLayout
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.features.collection.component.CollectionDetail
import com.jinproject.features.collection.component.CollectionList
import com.jinproject.features.collection.model.CollectionUiState
import com.jinproject.features.collection.model.ItemCollection
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.core.compose.LocalAnalyticsLoggingEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Composable
internal fun CollectionScreen(
    viewModel: CollectionViewModel = hiltViewModel(),
    showSnackBar: (SnackBarMessage) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CollectionScreen(
        collectionUiState = uiState,
        dispatchEvent = viewModel::dispatchCollectionEvent,
        onNavigateBack = onNavigateBack,
        showSnackBar = showSnackBar,
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class, FlowPreview::class)
@Composable
private fun CollectionScreen(
    collectionUiState: CollectionUiState,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    dispatchEvent: (CollectionEvent) -> Unit,
    onNavigateBack: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<ItemCollection>()

    BackHandler(navigator.canNavigateBack()) {
        coroutineScope.launch {
            navigator.navigateBack()
        }
    }

    LaunchedEffect(key1 = collectionUiState.selectedCollectionId) {
        collectionUiState.selectedCollectionId?.let {
            collectionUiState.itemCollections.find { it.id == collectionUiState.selectedCollectionId }
                ?.let { item ->
                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, item)
                }
        }
    }

    val textFiledState = rememberTextFieldState()
    var isFiltering by remember {
        mutableStateOf(true)
    }
    val localAnalyticsLoggingEvent = LocalAnalyticsLoggingEvent.current

    LaunchedEffect(key1 = Unit) {
        snapshotFlow {
            textFiledState.text
        }.debounce(300).distinctUntilChanged().collectLatest {
            localAnalyticsLoggingEvent(
                com.jinproject.features.core.AnalyticsEvent.CollectionSearchWord(
                    word = it.toString()
                )
            )
        }
    }

    val lazyListState = rememberLazyListState()

    ListDetailPaneScaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                DefaultLayout(
                    topBar = {
                        BackButtonSearchAppBar(
                            modifier = Modifier.padding(end = 8.dp),
                            textFieldState = textFiledState,
                            onBackClick = {
                                if (!isFiltering)
                                    isFiltering = true
                                else
                                    onNavigateBack()
                            },
                        )
                    },
                    modifier = Modifier.focusable(true),
                ) {
                    CollectionList(
                        collectionUiState = collectionUiState,
                        searchCharSequence = textFiledState.text,
                        lazyListState = lazyListState,
                        isFiltering = isFiltering,
                        changeIsFiltering = { bool -> isFiltering = bool },
                        navigateToDetail = { item ->
                            coroutineScope.launch {
                                navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, item)
                            }
                        },
                        dispatchEvent = dispatchEvent,
                    )
                }
            }
        },
        detailPane = {
            AnimatedPane {
                navigator.currentDestination?.contentKey?.let { collection ->
                    CollectionDetail(
                        collection = collection,
                        dispatchEvent = dispatchEvent,
                        onNavigateBack = {
                            coroutineScope.launch {
                                navigator.navigateBack()
                            }
                        },
                        showSnackBar = showSnackBar,
                    )
                }
            }
        },
    )
}

@Composable
@Preview
private fun PreviewCollectionScreen(
    @PreviewParameter(CollectionUiStatePreviewParameter::class)
    collectionUiState: CollectionUiState,
) = MiscellaneousToolTheme {
    CollectionScreen(
        collectionUiState = collectionUiState,
        dispatchEvent = {},
        onNavigateBack = {},
        showSnackBar = {},
    )
}