package com.jinproject.features.symbol.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.request.ImageRequest
import com.jinproject.design_compose.component.SubcomposeAsyncImageWithPreview
import com.jinproject.design_compose.component.bar.BackButtonTitleAppBar
import com.jinproject.design_compose.component.layout.DefaultLayout
import com.jinproject.design_compose.component.pushRefresh.MTProgressIndicatorRotating
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_ui.R
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
    imageUri: String,
    popBackStack: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
) {
    DefaultLayout(topBar = {
        BackButtonTitleAppBar(
            title = stringResource(id = R.string.symbol_detail_appbar),
            onBackClick = popBackStack
        )
    }) {
        SubcomposeAsyncImageWithPreview(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUri)
                .build(),
            contentDescription = "Image",
            loading = {
                MTProgressIndicatorRotating()
            },
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize(),
            placeHolderPreview = R.drawable.ic_x,
        )
    }
}

@Preview
@Composable
private fun PreviewDetailScreen() = MiscellaneousToolTheme {
    DetailScreen(
        imageUri = "",
        popBackStack = {},
        showSnackBar = {},
    )
}