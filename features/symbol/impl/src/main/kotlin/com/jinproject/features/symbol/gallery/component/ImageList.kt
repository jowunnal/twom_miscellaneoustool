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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.jinproject.design_compose.component.button.DefaultButton
import com.jinproject.design_compose.component.lazyList.ScrollableLayout
import com.jinproject.design_compose.component.lottie.PreviewLottieAnimation
import com.jinproject.design_compose.component.pushRefresh.MTPushRefreshIndicator
import com.jinproject.design_compose.component.pushRefresh.pushRefresh
import com.jinproject.design_compose.component.pushRefresh.rememberPushRefreshState
import com.jinproject.design_compose.component.text.DescriptionLargeText
import com.jinproject.design_compose.component.text.DescriptionMediumText
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.features.symbol.ImageListPreviewData
import com.jinproject.features.symbol.gallery.MTImage
import com.jinproject.features.symbol.symbol.MediaStorePermissionSet
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun ImageList(
    configuration: Configuration = LocalConfiguration.current,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    context: Context = LocalContext.current,
    density: Density = LocalDensity.current,
    list: ImmutableList<MTImage>,
    onClickImage: (MTImage) -> Unit,
    isRefreshing: Boolean,
    getMoreImages: () -> Unit,
    navigateToImageDetail: (String) -> Unit,
) {
    val pushRefreshState = rememberPushRefreshState(
        onRefresh = getMoreImages,
        isRefreshing = isRefreshing,
    )

    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { results ->
            if (results[READ_MEDIA_IMAGES] == true)
                getMoreImages()
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

    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("lottie/empty_file.json"))
    val lottieProgress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
    )

    if (list.isEmpty())
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            PreviewLottieAnimation(
                composition = composition,
                progress = lottieProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
            )
        }
    else
        Column(
            modifier = Modifier
                .fillMaxHeight()
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

            ScrollableLayout(
                viewHeight = with(density) {
                    val perViewPortHeight = viewPortSize.height

                    (perHeight * (list.size / cellColumnCount)).toPx() - perViewPortHeight
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
                    items(list, key = { image -> image.id }) { image ->
                        GalleryImage(
                            uri = image.uri,
                            modifier = Modifier
                                .width(perWidth)
                                .height(perHeight),
                            onClickImage = { isError ->
                                if (!isError)
                                    onClickImage(image)
                            },
                            placeHolder = R.drawable.ic_x,
                            enlargeImage = { navigateToImageDetail(image.uri) },
                        )
                    }
                }
            }
        }
}

@Preview
@Composable
private fun PreviewFeedList() = MiscellaneousToolTheme {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        ImageList(
            list = ImageListPreviewData.items,
            isRefreshing = false,
            onClickImage = {},
            getMoreImages = {},
            navigateToImageDetail = {},
        )
    }
}