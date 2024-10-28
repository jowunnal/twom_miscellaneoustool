package com.jinproject.features.collection

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jinproject.design_compose.component.DefaultLayout
import com.jinproject.design_compose.component.bar.BackButtonSearchAppBar
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.features.collection.component.CollectionDetail
import com.jinproject.features.collection.component.CollectionList
import com.jinproject.features.collection.model.CollectionUiState
import com.jinproject.features.collection.model.ItemCollection
import com.jinproject.features.core.base.item.SnackBarMessage

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

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun CollectionScreen(
    collectionUiState: CollectionUiState,
    dispatchEvent: (CollectionEvent) -> Unit,
    onNavigateBack: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
) {

    val navigator = rememberListDetailPaneScaffoldNavigator<ItemCollection>()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    val textFiledState = rememberTextFieldState()
    var isFilterMode by remember {
        mutableStateOf(false)
    }

    ListDetailPaneScaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                DefaultLayout(topBar = {
                    BackButtonSearchAppBar(
                        textFieldState = textFiledState,
                        onBackClick = onNavigateBack,
                    )
                }) {
                    CollectionList(
                        collectionUiState = collectionUiState,
                        searchCharSequence = textFiledState.text,
                        isFilterMode = isFilterMode,
                        triggerFilterMode = { bool -> isFilterMode = bool },
                        navigateToDetail = { item ->
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, item)
                        },
                        dispatchEvent = dispatchEvent,
                    )
                }
            }
        },
        detailPane = {
            AnimatedPane {
                navigator.currentDestination?.content?.let { collection ->
                    CollectionDetail(
                        collection = collection,
                        dispatchEvent = dispatchEvent,
                        onNavigateBack = {
                            navigator.navigateBack()
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