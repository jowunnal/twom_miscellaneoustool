package com.jinproject.features.collection

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jinproject.design_compose.component.bar.BackButtonSearchAppBar
import com.jinproject.design_compose.component.layout.DefaultLayout
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.features.collection.component.CollectionList
import com.jinproject.features.collection.model.CollectionUiState
import com.jinproject.features.core.compose.LocalAnalyticsLoggingEvent
import com.jinproject.features.core.compose.LocalNavigator
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
internal fun CollectionScreen(
    viewModel: CollectionViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navigator = LocalNavigator.current

    CollectionScreen(
        collectionUiState = uiState,
        dispatchEvent = viewModel::dispatchCollectionEvent,
        onNavigateBack = { navigator.goBack() },
        navigateToDetail = { id ->
            navigator.replaceAbove(CollectionRoute.CollectionList, CollectionRoute.CollectionDetail(id))
        },
    )
}

@OptIn(FlowPreview::class)
@Composable
private fun CollectionScreen(
    collectionUiState: CollectionUiState,
    dispatchEvent: (CollectionEvent) -> Unit,
    onNavigateBack: () -> Unit,
    navigateToDetail: (Int) -> Unit,
) {
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
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .focusable(true),
    ) {
        CollectionList(
            collectionUiState = collectionUiState,
            searchCharSequence = textFiledState.text,
            lazyListState = lazyListState,
            isFiltering = isFiltering,
            changeIsFiltering = { bool -> isFiltering = bool },
            navigateToDetail = { item -> navigateToDetail(item.id) },
            dispatchEvent = dispatchEvent,
        )
    }
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
        navigateToDetail = {},
    )
}
