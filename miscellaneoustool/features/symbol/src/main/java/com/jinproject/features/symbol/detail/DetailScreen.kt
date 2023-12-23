package com.jinproject.features.symbol.detail

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.request.ImageRequest
import com.jinproject.design_compose.component.BackButtonTitleAppBar
import com.jinproject.design_compose.component.DefaultLayout
import com.jinproject.design_compose.component.SubcomposeAsyncImageWithPreview
import com.jinproject.design_compose.component.pushrefresh.GalleryProgressIndicator
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.features.core.base.item.SnackBarMessage

@Composable
internal fun DetailScreen(
    detailViewModel: DetailViewModel = hiltViewModel(),
    popBackStack: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
) {
    val imageUri by detailViewModel.imageDetailState.collectAsStateWithLifecycle()

    DetailScreen(
        imageUri = imageUri,
        popBackStack = popBackStack,
        showSnackBar = showSnackBar,
    )
}

@Composable
private fun DetailScreen(
    imageUri: Uri,
    popBackStack: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
) {
    DefaultLayout(topBar = {
        BackButtonTitleAppBar(
            title = stringResource(id = com.jinproject.design_ui.R.string.symbol_detail_appbar),
            onClick = popBackStack
        )
    }) {
        SubcomposeAsyncImageWithPreview(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUri)
                .build(),
            contentDescription = "Image",
            loading = {
                GalleryProgressIndicator()
            },
            contentScale = ContentScale.Fit,
            modifier = androidx.compose.ui.Modifier
                .fillMaxSize(),
            placeHolderPreview = com.jinproject.design_ui.R.drawable.ic_x,
        )
    }
}

@Preview
@Composable
private fun PreviewDetailScreen() = MiscellaneousToolTheme {
    DetailScreen(
        imageUri = Uri.EMPTY,
        popBackStack = {},
        showSnackBar = {},
    )
}