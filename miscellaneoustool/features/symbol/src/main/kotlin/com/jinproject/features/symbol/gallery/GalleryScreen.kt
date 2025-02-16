package com.jinproject.features.symbol.gallery

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jinproject.design_compose.component.layout.DownloadableLayout
import com.jinproject.design_compose.component.layout.DownloadableUiState
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.features.core.AnalyticsEvent
import com.jinproject.features.core.AuthManager
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.core.compose.LocalAnalyticsLoggingEvent
import com.jinproject.features.symbol.gallery.component.GalleryAppBar
import com.jinproject.features.symbol.gallery.component.ImageList
import com.jinproject.features.symbol.guildmark.SymbolOverlayService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
internal fun GalleryScreen(
    galleryViewModel: GalleryViewModel = hiltViewModel(),
    navigateToImageDetail: (String) -> Unit,
    popBackStack: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
    navigateToGuildMarkPreview: (String) -> Unit,
    navigateToGuildMark: (String) -> Unit,
    navigateToPurchasedImage: () -> Unit,
    navigateToAuthGraph: () -> Unit,
) {
    val uiState by galleryViewModel.galleryUiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        galleryViewModel.snackBarMessageChannel.receiveAsFlow().collectLatest { snackBarMessage ->
            showSnackBar(snackBarMessage)
        }
    }

    GalleryScreen(
        uiState = uiState,
        getMoreImages = galleryViewModel::getMoreImages,
        navigateToImageDetail = navigateToImageDetail,
        popBackStack = popBackStack,
        showSnackBar = showSnackBar,
        navigateToGuildMarkPreview = navigateToGuildMarkPreview,
        navigateToGuildMark = navigateToGuildMark,
        navigateToPurchasedImage = navigateToPurchasedImage,
        navigateToAuthGraph = navigateToAuthGraph,
    )
}

@Composable
private fun GalleryScreen(
    context: Context = LocalContext.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    uiState: DownloadableUiState,
    getMoreImages: () -> Unit,
    navigateToImageDetail: (String) -> Unit,
    popBackStack: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
    navigateToGuildMarkPreview: (String) -> Unit,
    navigateToGuildMark: (String) -> Unit,
    navigateToPurchasedImage: () -> Unit,
    navigateToAuthGraph: () -> Unit,
) {
    val localAnalyticsLoggingEvent = LocalAnalyticsLoggingEvent.current

    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(key1 = Unit) {
        localAnalyticsLoggingEvent(
            AnalyticsEvent.GalleryScreen
        )
    }

    DownloadableLayout(
        modifier = Modifier.nestedScroll(object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val new = if (available.y < 0f) {
                    getMoreImages()

                    0f
                } else
                    available.y

                return available.copy(y = new)
            }
        }),
        topBar = {
            GalleryAppBar(
                title = stringResource(id = R.string.symbol_gallery_topbar),
                onBackClick = popBackStack,
                onSettingClick = {
                    if (AuthManager.isActive)
                        navigateToPurchasedImage()
                    else {
                        navigateToAuthGraph()
                        showSnackBar(
                            SnackBarMessage(
                                headerMessage = context.getString(R.string.auth_sign_in_required)
                            )
                        )
                    }
                },
            )
        },
        downloadableUiState = uiState,
    ) { state ->
        val downloadedUiState = state as GalleryUiState

        ImageList(
            list = downloadedUiState.data,
            lazyGridState = lazyGridState,
            isRefreshing = false,
            onClickImage = { image ->
                context.stopService(
                    Intent(
                        context,
                        SymbolOverlayService::class.java
                    )
                )

                if (downloadedUiState.isPaidImage(image))
                    navigateToGuildMark(image.uri)
                else
                    navigateToGuildMarkPreview(image.uri)
            },
            getMoreImages = getMoreImages,
            navigateToImageDetail = navigateToImageDetail,
        )
    }
}

@Preview
@Composable
private fun PreviewGalleryScreen(
    @PreviewParameter(GalleryPreviewParameters::class)
    uiState: DownloadableUiState,
) = MiscellaneousToolTheme {
    GalleryScreen(
        uiState = uiState,
        getMoreImages = {},
        navigateToImageDetail = {},
        popBackStack = { },
        showSnackBar = {},
        navigateToGuildMarkPreview = {},
        navigateToGuildMark = {},
        navigateToPurchasedImage = {},
        navigateToAuthGraph = {},
    )
}