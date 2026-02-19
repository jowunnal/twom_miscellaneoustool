package com.jinproject.features.symbol.gallery.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.request.ImageRequest
import com.jinproject.design_compose.component.SubcomposeAsyncImageWithPreview
import com.jinproject.design_compose.component.button.clickableAvoidingDuplication
import com.jinproject.design_compose.component.pushRefresh.MTProgressIndicatorRotating
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.features.symbol.ai.DownloadState

@Composable
internal fun GalleryImage(
    modifier: Modifier = Modifier,
    uri: String,
    downloadState: DownloadState = DownloadState.Downloaded,
    @DrawableRes placeHolder: Int = com.jinproject.design_ui.R.drawable.test,
    enlargeImage: () -> Unit,
    onClickImage: (Boolean) -> Unit = {},
    content: @Composable BoxScope.(Boolean, DownloadState) -> Unit = { _, _ -> },
) {
    val backgroundColor = MaterialTheme.colorScheme.background
    var isError by remember {
        mutableStateOf(false)
    }

    Box(modifier = Modifier) {
        SubcomposeAsyncImageWithPreview(
            model = ImageRequest.Builder(LocalContext.current)
                .data(uri)
                .build(),
            contentDescription = "Image",
            loading = {
                MTProgressIndicatorRotating()
            },
            error = {
                if (downloadState !is DownloadState.Downloading)
                    Image(
                        painter = painterResource(id = com.jinproject.design_ui.R.drawable.ic_not_download),
                        contentDescription = "Expired Image"
                    )
            },
            onError = {
                isError = true
            },
            onSuccess = {
                isError = false
            },
            contentScale = ContentScale.Fit,
            modifier = modifier
                .align(Alignment.Center)
                .clickableAvoidingDuplication {
                    onClickImage(isError)
                },
            placeHolderPreview = placeHolder,
        )

        if (downloadState !is DownloadState.Downloading && !isError)
            Canvas(
                modifier = Modifier
                    .size(24.dp)
                    .clickableAvoidingDuplication {
                        enlargeImage()
                    }
                    .padding(4.dp)
                    .background(
                        MaterialTheme.colorScheme.onSurfaceVariant,
                        RoundedCornerShape(2.5.dp)
                    )
                    .padding(4.dp)
                    .align(Alignment.BottomStart),
            ) {
                val width = 1.5.dp.toPx()
                val stroke = Stroke(
                    width = width,
                )

                val rect = Rect(Offset.Zero, size)
                val path = Path().apply {
                    moveTo(0f, rect.center.y)
                    lineTo(0f, rect.bottom)
                    lineTo(rect.center.x, rect.bottom)

                    moveTo(rect.center.x, 0f)
                    lineTo(rect.right, 0f)
                    lineTo(rect.right, rect.center.y)
                }

                drawPath(path, color = backgroundColor, style = stroke)
            }

        content(isError, downloadState)
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewGalleryImage() = MiscellaneousToolTheme {
    GalleryImage(
        uri = "",
        enlargeImage = {},
        modifier = Modifier.size(200.dp),
    )
}