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
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
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
import com.jinproject.design_compose.component.SubcomposeAsyncImageWithPreview
import com.jinproject.design_compose.component.button.DefaultButton
import com.jinproject.design_compose.component.lazyList.ScrollableLayout
import com.jinproject.design_compose.component.pushRefresh.MTProgressIndicatorRotating
import com.jinproject.design_compose.component.pushRefresh.MTPushRefreshIndicator
import com.jinproject.design_compose.component.pushRefresh.pushRefresh
import com.jinproject.design_compose.component.pushRefresh.rememberPushRefreshState
import com.jinproject.design_compose.component.text.DescriptionLargeText
import com.jinproject.design_compose.component.text.DescriptionMediumText
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.features.symbol.gallery.GalleryPreviewParameters
import com.jinproject.features.symbol.gallery.MTImageList
import com.jinproject.features.symbol.symbol.MediaStorePermissionSet
import kotlin.math.roundToInt

@Composable
internal fun ImageList(
    configuration: Configuration = LocalConfiguration.current,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    context: Context = LocalContext.current,
    density: Density = LocalDensity.current,
    list: MTImageList,
    setClickedImage: (Long) -> Unit,
    isRefreshing: Boolean,
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
            if (results[READ_MEDIA_IMAGES] == true)
                pushRefreshState.onRefresh()
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

    val cellColumnCount = 4
    val perWidth = configuration.screenWidthDp.dp / cellColumnCount
    val cellRowCount = 8
    val perHeight = (configuration.screenHeightDp.dp - 50.dp) / cellRowCount

    Column(
        modifier = Modifier
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

        ScrollableLayout(
            viewHeight = with(density) {
                val perViewPortHeight = viewPortSize.height

                (perHeight * (list.images.size / cellColumnCount)).toPx() - perViewPortHeight
            },
            scrollableState = lazyGridState,
            isUpperScrollActive = isUpperScrollActive,
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(cellColumnCount),
                state = lazyGridState,
                modifier = Modifier
                    .fillMaxHeight(),
            ) {
                items(list.images, key = { image -> image.id }) { image ->
                    GalleryImage(
                        uri = image.uri,
                        modifier = Modifier
                            .width(perWidth)
                            .height(perHeight)
                            .clickable {
                                if (list.clickedId == -1L)
                                    setClickedImage(image.id)
                                else if (list.clickedId == image.id)
                                    setClickedImage(-1L)
                            }
                            .graphicsLayer {
                                alpha = if (list.clickedId == image.id) 0.3f else 1f
                            },
                        placeHolder = R.drawable.ic_x,
                        enlargeImage = { navigateToImageDetail(image.uri) },
                    )
                }
                item(span = { GridItemSpan(currentLineSpan = cellColumnCount) }) {
                    MTPushRefreshIndicator(
                        state = pushRefreshState,
                        isRefreshing = isRefreshing,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewFeedList(
    @PreviewParameter(GalleryPreviewParameters::class)
    imageList: MTImageList,
) = MiscellaneousToolTheme {
    val state = rememberLazyGridState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        ImageList(
            list = imageList,
            isRefreshing = false,
            setClickedImage = {},
            lazyGridState = state,
            getMoreImages = {},
            navigateToImageDetail = {},
        )
    }
}