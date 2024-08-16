package com.jinproject.features.symbol.gallery

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jinproject.design_compose.component.DefaultLayout
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.symbol.gallery.component.GalleryAppBar
import com.jinproject.features.symbol.gallery.component.ImageList
import com.jinproject.features.symbol.guildmark.SymbolOverlayService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun GalleryScreen(
    galleryViewModel: GalleryViewModel = hiltViewModel(),
    navigateToImageDetail: (String) -> Unit,
    popBackStack: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
    navigateToGuildMarkPreview: (String) -> Unit,
    navigateToGuildMark: (String) -> Unit,
) {
    val images by galleryViewModel.images.collectAsStateWithLifecycle()
    val isPaidImage by galleryViewModel.isPaidImage.collectAsStateWithLifecycle()

    GalleryScreen(
        images = images,
        setClickedImage = galleryViewModel::setClickedImage,
        getMoreImages = galleryViewModel::getMoreImages,
        isPaidImage = isPaidImage,
        navigateToImageDetail = navigateToImageDetail,
        popBackStack = popBackStack,
        showSnackBar = showSnackBar,
        navigateToGuildMarkPreview = navigateToGuildMarkPreview,
        navigateToGuildMark = navigateToGuildMark
    )
}

@Composable
private fun GalleryScreen(
    context: Context = LocalContext.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    images: MTImageList,
    setClickedImage: (Long) -> Unit,
    getMoreImages: suspend (Context) -> Unit,
    isPaidImage: Boolean,
    navigateToImageDetail: (String) -> Unit,
    popBackStack: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
    navigateToGuildMarkPreview: (String) -> Unit,
    navigateToGuildMark: (String) -> Unit,
) {
    var isRefreshing by remember {
        mutableStateOf(false)
    }

    DefaultLayout(
        modifier = Modifier,
        topBar = {
            GalleryAppBar(
                title = stringResource(id = com.jinproject.design_ui.R.string.symbol_gallery_topbar),
                onBackClick = popBackStack,
                enabledTailText = images.clickedId != -1L,
                navigateToGuildMarkPreview = {
                    images.images.find { it.id == images.clickedId }?.let { clickedImage ->
                        context.stopService(
                            Intent(
                                context,
                                SymbolOverlayService::class.java
                            )
                        )
                        if (isPaidImage)
                            navigateToGuildMark(clickedImage.uri)
                        else
                            navigateToGuildMarkPreview(clickedImage.uri)
                    }
                }
            )
        }
    ) {
        ImageList(
            list = images,
            isRefreshing = isRefreshing,
            setClickedImage = { id -> setClickedImage(id) },
            getMoreImages = {
                coroutineScope.launch {
                    isRefreshing = true
                    delay(500)
                    getMoreImages(context)
                    isRefreshing = false
                }
            },
            navigateToImageDetail = navigateToImageDetail,
        )
    }
}

@Preview
@Composable
private fun PreviewGalleryScreen(
    @PreviewParameter(GalleryPreviewParameters::class)
    imageList: MTImageList,
) = MiscellaneousToolTheme {
    GalleryScreen(
        images = imageList,
        setClickedImage = {},
        getMoreImages = {},
        isPaidImage = false,
        navigateToImageDetail = {},
        popBackStack = { },
        showSnackBar = {},
        navigateToGuildMarkPreview = {},
        navigateToGuildMark = {}
    )
}