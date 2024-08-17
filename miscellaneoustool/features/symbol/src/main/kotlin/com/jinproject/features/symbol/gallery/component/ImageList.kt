package com.jinproject.features.symbol.gallery.component

import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.content.res.Configuration
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import coil.request.ImageRequest
import com.jinproject.design_compose.component.button.DefaultButton
import com.jinproject.design_compose.component.button.DefaultIconButton
import com.jinproject.design_compose.component.DescriptionLargeText
import com.jinproject.design_compose.component.DescriptionMediumText
import com.jinproject.design_compose.component.SubcomposeAsyncImageWithPreview
import com.jinproject.design_compose.component.lazyList.TimeScheduler
import com.jinproject.design_compose.component.lazyList.VerticalScrollBar
import com.jinproject.design_compose.component.lazyList.addScrollBarNestedScrollConnection
import com.jinproject.design_compose.component.lazyList.rememberScrollBarState
import com.jinproject.design_compose.component.lazyList.rememberTimeScheduler
import com.jinproject.design_compose.component.pushRefresh.MTProgressIndicatorRotating
import com.jinproject.design_compose.component.pushRefresh.MTPushRefreshIndicator
import com.jinproject.design_compose.component.pushRefresh.pushRefresh
import com.jinproject.design_compose.component.pushRefresh.rememberPushRefreshState
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.features.symbol.gallery.GalleryPreviewParameters
import com.jinproject.features.symbol.gallery.MTImageList
import com.jinproject.features.symbol.symbol.MediaStorePermissionSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
internal fun ImageList(
    configuration: Configuration = LocalConfiguration.current,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    context: Context = LocalContext.current,
    timeScheduler: TimeScheduler = rememberTimeScheduler(),
    density: Density = LocalDensity.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    list: MTImageList,
    setClickedImage: (Long) -> Unit,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
    getMoreImages: () -> Unit,
    navigateToImageDetail: (String) -> Unit,
) {
    val pushRefreshState = rememberPushRefreshState(
        onRefresh = getMoreImages,
        isRefreshing = isRefreshing,
    )
    val isClickedItem by remember(list) {
        derivedStateOf {
            list.clickedId >= 0L
        }
    }
    val backgroundColor = MaterialTheme.colorScheme.background
    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { results ->

        }

    val isUpperScrollActive by remember {
        derivedStateOf {
            lazyGridState.firstVisibleItemIndex > 0
        }
    }

    val viewPortSize by remember {
        derivedStateOf {
            lazyGridState.layoutInfo.viewportSize
        }
    }

    val cellColumnsCount = 4
    val perWidth = configuration.screenWidthDp.dp / cellColumnsCount
    val perHeight = configuration.screenHeightDp.dp / 8

    val perItemHeight = perHeight

    val scrollBarState = rememberScrollBarState(
        viewHeight = with(density) {
            val perViewPortHeight = viewPortSize.height

            (perItemHeight * (list.images.size / cellColumnsCount)).toPx() - perViewPortHeight
        }
    )

    Column(
        modifier = modifier
            .pushRefresh(pushRefreshState = pushRefreshState)
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE &&
            ContextCompat.checkSelfPermission(
                context,
                READ_MEDIA_VISUAL_USER_SELECTED
            ) == PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context,
                READ_MEDIA_IMAGES
            ) == PERMISSION_DENIED
        ) Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DescriptionMediumText(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 20.dp),
                text = "선택한 일부 사진에만 접근할 수 있는 권한을 부여한 상태에요.",
                color = MaterialTheme.colorScheme.scrim,
            )
            DefaultButton(onClick = {
                permissionLauncher.launch(MediaStorePermissionSet.toTypedArray())
            }) {
                DescriptionLargeText(text = "관리", color = MaterialTheme.colorScheme.onPrimary)
            }
        }

        AnimatedVisibility(visible = isClickedItem) {
            Box(modifier = Modifier) {
                SubcomposeAsyncImageWithPreview(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(list.images.find { it.id == list.clickedId }?.uri)
                        .build(),
                    contentDescription = "Image",
                    loading = {
                        MTProgressIndicatorRotating()
                    },
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(perWidth)
                        .height(perHeight)
                        .align(Alignment.Center)
                        .padding(10.dp),
                    placeHolderPreview = R.drawable.ic_x,
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
                    ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.ic_x,
                        context.theme
                    )
                        ?.toBitmap(size.width.roundToInt(), size.height.roundToInt())
                        ?.asImageBitmap()?.let { imageBitmap ->
                            drawImage(
                                imageBitmap,
                                colorFilter = ColorFilter.tint(backgroundColor)
                            )
                        }
                }
            }
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .addScrollBarNestedScrollConnection(
                    timer = timeScheduler,
                    isUpperScrollActive = isUpperScrollActive,
                    scrollBarState = scrollBarState,
                ),
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(cellColumnsCount),
                state = lazyGridState,
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.Center),
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
                                MTProgressIndicatorRotating()
                            },
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(perWidth)
                                .height(perHeight)
                                .align(Alignment.Center)
                                .clickable {
                                    if (list.clickedId == -1L)
                                        setClickedImage(image.id)
                                    else if (list.clickedId == image.id)
                                        setClickedImage(-1L)
                                }
                                .graphicsLayer {
                                    alpha = if (list.clickedId == image.id) 0.3f else 1f
                                },
                            placeHolderPreview = R.drawable.ic_x,
                        )

                        Canvas(
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    navigateToImageDetail(image.uri)
                                }
                                .padding(4.dp)
                                .background(MaterialTheme.colorScheme.onSurfaceVariant , RoundedCornerShape(2.5.dp))
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
                    MTPushRefreshIndicator(
                        state = pushRefreshState,
                        isRefreshing = isRefreshing,
                        modifier = Modifier
                    )
                }
            }

            val upperScrollAlpha by animateFloatAsState(
                targetValue = if (timeScheduler.isRunning) 1f else 0f,
                label = "Alpha Animate State"
            )

            UpperScrollButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .graphicsLayer {
                        alpha = upperScrollAlpha
                        translationY = -50f
                    }
                    .shadow(1.dp, CircleShape),
                onClick = {
                    coroutineScope.launch {
                        lazyGridState.animateScrollToItem(0)
                        scrollBarState.changeOffset(0f)
                    }
                },
            )

            if (isUpperScrollActive)
                VerticalScrollBar(
                    scrollBarState = scrollBarState,
                    lazyListState = lazyGridState,
                    headerItemHeight = 0.dp,
                    perItemHeight = perItemHeight,
                )

        }
    }
}

@Composable
fun UpperScrollButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    DefaultIconButton(
        icon = com.jinproject.design_compose.R.drawable.ic_arrow_up_to_start,
        onClick = onClick,
        iconTint = MaterialTheme.colorScheme.onSurface,
        iconSize = 32.dp,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun PreviewFeedList(
    @PreviewParameter(GalleryPreviewParameters::class)
    imageList: MTImageList,
) = MiscellaneousToolTheme {
    val state = rememberLazyGridState()
    ImageList(
        list = imageList,
        isRefreshing = false,
        setClickedImage = {},
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        lazyGridState = state,
        getMoreImages = {},
        navigateToImageDetail = {},
    )
}