package com.jinproject.features.symbol.gallery.component

import android.content.Context
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import coil.request.ImageRequest
import com.jinproject.design_compose.component.SubcomposeAsyncImageWithPreview
import com.jinproject.design_compose.component.pushrefresh.GalleryProgressIndicator
import com.jinproject.design_compose.component.pushrefresh.PushRefreshIndicator
import com.jinproject.design_compose.component.pushrefresh.pushRefresh
import com.jinproject.design_compose.component.pushrefresh.rememberPushRefreshState
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.features.symbol.gallery.GalleryPreviewParameters
import com.jinproject.features.symbol.gallery.MTImageList
import kotlin.math.roundToInt

@Composable
internal fun ImageList(
    list: MTImageList,
    setClickedImage: (Long) -> Unit,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
    configuration: Configuration = LocalConfiguration.current,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    context: Context = LocalContext.current,
    getMoreImages: () -> Unit,
    navigateToImageDetail: (String) -> Unit,
) {

    val pushRefreshState = rememberPushRefreshState(
        onRefresh = getMoreImages,
        maxHeight = 200f,
        isRefreshing = isRefreshing,
    )
    val isClickedItem by remember(list) {
        derivedStateOf {
            list.clickedId >= 0L
        }
    }
    val backgroundColor = MaterialTheme.colorScheme.background

    Column(
        modifier = modifier
            .pushRefresh(pushRefreshState = pushRefreshState)
    ) {
        val cellColumnsCount = 4
        val perWidth = configuration.screenWidthDp.dp / cellColumnsCount
        val perHeight = configuration.screenHeightDp.dp / 8

        AnimatedVisibility(visible = isClickedItem) {
            Box(modifier = Modifier) {
                SubcomposeAsyncImageWithPreview(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(list.images.find { it.id == list.clickedId }?.uri)
                        .build(),
                    contentDescription = "Image",
                    loading = {
                        GalleryProgressIndicator()
                    },
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(perWidth)
                        .height(perHeight)
                        .align(Alignment.Center)
                        .padding(10.dp),
                    placeHolderPreview = com.jinproject.design_ui.R.drawable.ic_x,
                )
                Canvas(
                    modifier = Modifier
                        .size(16.dp)
                        .clickable {
                            if (list.clickedId != -1L)
                                setClickedImage(-1L)
                        }
                        .clip(RoundedCornerShape(2.5.dp))
                        .background(MaterialTheme.colorScheme.onSurfaceVariant)
                        .align(Alignment.TopEnd),
                ) {
                    ResourcesCompat.getDrawable(context.resources, com.jinproject.design_ui.R.drawable.ic_x, context.theme)
                        ?.toBitmap(size.width.roundToInt(), size.height.roundToInt())
                        ?.asImageBitmap()?.let { imageBitmap ->
                            drawImage(imageBitmap, colorFilter = ColorFilter.tint(backgroundColor))
                        }
                }
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(cellColumnsCount),
            state = lazyGridState,
            modifier = Modifier,
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(list.images, key = { image -> image.id }) { image ->
                Box(modifier = Modifier.fillMaxSize()) {
                    SubcomposeAsyncImageWithPreview(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(image.uri)
                            .build(),
                        contentDescription = "Image",
                        loading = {
                            GalleryProgressIndicator()
                        },
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(perWidth)
                            .height(perHeight)
                            .align(Alignment.Center)
                            .clickable {
                                if (list.clickedId == -1L)
                                    setClickedImage(image.id)
                                else if (list.clickedId  == image.id)
                                    setClickedImage(-1L)
                            }
                            .graphicsLayer {
                                alpha = if (list.clickedId == image.id) 0.3f else 1f
                            },
                        placeHolderPreview = com.jinproject.design_ui.R.drawable.ic_x,
                    )

                    Canvas(
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                navigateToImageDetail(image.uri)
                            }
                            .padding(4.dp)
                            .clip(RoundedCornerShape(2.5.dp))
                            .background(MaterialTheme.colorScheme.onSurfaceVariant)
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
                }
            }
            item(span = { GridItemSpan(currentLineSpan = cellColumnsCount) }) {
                PushRefreshIndicator(
                    state = pushRefreshState,
                    isRefreshing = isRefreshing,
                    modifier = Modifier
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewFeedList(
    @PreviewParameter(GalleryPreviewParameters::class)
    imageList: MTImageList
) = MiscellaneousToolTheme {
    val state = rememberLazyGridState()
    ImageList(
        list = imageList,
        isRefreshing = false,
        setClickedImage = {},
        modifier = Modifier,
        lazyGridState = state,
        getMoreImages = {},
        navigateToImageDetail = {},
    )
}