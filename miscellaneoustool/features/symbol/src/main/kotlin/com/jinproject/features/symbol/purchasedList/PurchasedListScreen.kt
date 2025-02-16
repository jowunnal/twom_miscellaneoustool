package com.jinproject.features.symbol.purchasedList

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jinproject.design_compose.component.bar.BackButtonTitleAppBar
import com.jinproject.design_compose.component.layout.DownloadableLayout
import com.jinproject.design_compose.component.layout.DownloadableUiState
import com.jinproject.design_compose.theme.MiscellaneousToolTheme

@Composable
internal fun PurchasedListScreen(
    viewModel: PurchasedListViewModel = hiltViewModel(),
    navigateToItemDetail: (String) -> Unit,
    navigateToGuildMark: (String) -> Unit,
    navigatePopBackStack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PurchasedListScreen(
        uiState = uiState,
        getNextPage = viewModel::getNextPage,
        navigateToItemDetail = navigateToItemDetail,
        navigateToGuildMark = navigateToGuildMark,
        navigatePopBackStack = navigatePopBackStack,
    )
}

@Composable
private fun PurchasedListScreen(
    uiState: DownloadableUiState,
    getNextPage: () -> Unit,
    navigateToItemDetail: (String) -> Unit,
    navigateToGuildMark: (String) -> Unit,
    navigatePopBackStack: () -> Unit,
) {
    DownloadableLayout(
        topBar = {
            BackButtonTitleAppBar(
                title = stringResource(id = com.jinproject.design_ui.R.string.symbol_purchased_list),
                onBackClick = navigatePopBackStack,
            )
        },
        downloadableUiState = uiState
    ) { state ->
        PurchasedImageList(
            imageList = (state as PurchasedListUiState).data,
            getNextPage = getNextPage,
            navigateToItemDetail = navigateToItemDetail,
            navigateToGuildMark = navigateToGuildMark,
        )
    }
}

@Composable
@Preview
private fun PreviewPurchasedListScreen(
    @PreviewParameter(PurchasedListPreviewParameter::class)
    uiState: DownloadableUiState,
) = MiscellaneousToolTheme {
    PurchasedListScreen(
        uiState = uiState,
        getNextPage = {},
        navigateToItemDetail = {},
        navigateToGuildMark = {},
        navigatePopBackStack = {},
    )
}