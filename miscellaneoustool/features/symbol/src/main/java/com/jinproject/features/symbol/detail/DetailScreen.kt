package com.jinproject.features.symbol.detail

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.request.ImageRequest
import com.jinproject.design_compose.component.DefaultAppBar
import com.jinproject.design_compose.component.DefaultLayout
import com.jinproject.design_compose.component.SubcomposeAsyncImageWithPreview
import com.jinproject.design_compose.component.pushrefresh.GalleryProgressIndicator
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.symbol.R

@Composable
fun DetailScreen(
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
fun DetailScreen(
    imageUri: Uri,
    popBackStack: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
) {
    DefaultLayout(topBar = {
        DefaultAppBar(
            title = "이미지 상세",
            onBackClick = popBackStack
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
            placeHolderPreview = R.drawable.ic_x,
        )
    }

}