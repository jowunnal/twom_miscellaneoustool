package com.jinproject.design_compose.component

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImagePainter
import coil.compose.LocalImageLoader
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageScope

@Composable
fun SubcomposeAsyncImageWithPreview(
    @DrawableRes placeHolderPreview: Int = 0,
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    loading: @Composable (SubcomposeAsyncImageScope.(AsyncImagePainter.State.Loading) -> Unit)? = null,
    success: @Composable (SubcomposeAsyncImageScope.(AsyncImagePainter.State.Success) -> Unit)? = null,
    error: @Composable (SubcomposeAsyncImageScope.(AsyncImagePainter.State.Error) -> Unit)? = null,
    onLoading: ((AsyncImagePainter.State.Loading) -> Unit)? = null,
    onSuccess: ((AsyncImagePainter.State.Success) -> Unit)? = null,
    onError: ((AsyncImagePainter.State.Error) -> Unit)? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
) {
    if(LocalInspectionMode.current && placeHolderPreview != 0) {
        Image(
            painter = painterResource(id = placeHolderPreview),
            contentDescription = contentDescription,
            modifier = modifier,
            colorFilter = colorFilter,
            contentScale = contentScale,
            alignment = alignment,
            alpha = alpha
        )

        return
    }

    SubcomposeAsyncImage(
        model = model,
        contentDescription = contentDescription,
        imageLoader = LocalImageLoader.current,
        modifier = modifier,
        loading = loading,
        success = success,
        error = error,
        onLoading = onLoading,
        onSuccess = onSuccess,
        onError = onError,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
        filterQuality = filterQuality
    )
}

@Composable
fun SubcomposeAsyncImageWithPreview(
    placeHolderPreview: Bitmap? = null,
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    loading: @Composable (SubcomposeAsyncImageScope.(AsyncImagePainter.State.Loading) -> Unit)? = null,
    success: @Composable (SubcomposeAsyncImageScope.(AsyncImagePainter.State.Success) -> Unit)? = null,
    error: @Composable (SubcomposeAsyncImageScope.(AsyncImagePainter.State.Error) -> Unit)? = null,
    onLoading: ((AsyncImagePainter.State.Loading) -> Unit)? = null,
    onSuccess: ((AsyncImagePainter.State.Success) -> Unit)? = null,
    onError: ((AsyncImagePainter.State.Error) -> Unit)? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
) {
    if(LocalInspectionMode.current && placeHolderPreview != null) {
        Image(
            bitmap = placeHolderPreview.asImageBitmap(),
            contentDescription = contentDescription,
            modifier = modifier,
            colorFilter = colorFilter,
            contentScale = contentScale,
            alignment = alignment,
            alpha = alpha
        )

        return
    }

    SubcomposeAsyncImage(
        model = model,
        contentDescription = contentDescription,
        imageLoader = LocalImageLoader.current,
        modifier = modifier,
        loading = loading,
        success = success,
        error = error,
        onLoading = onLoading,
        onSuccess = onSuccess,
        onError = onError,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
        filterQuality = filterQuality
    )
}