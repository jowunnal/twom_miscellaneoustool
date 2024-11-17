package com.jinproject.features.collection.component

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.component.HorizontalDivider
import com.jinproject.design_compose.component.HorizontalSpacer
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.component.button.TextButton
import com.jinproject.design_compose.component.button.clickableAvoidingDuplication
import com.jinproject.design_compose.component.button.combinedClickableAvoidingDuplication
import com.jinproject.design_compose.component.image.DefaultPainterImage
import com.jinproject.design_compose.component.paddingvalues.MiscellanousToolPaddingValues
import com.jinproject.design_compose.component.text.DescriptionSmallText
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.features.collection.CollectionEvent
import com.jinproject.features.collection.CollectionUiStatePreviewParameter
import com.jinproject.features.collection.model.CollectionUiState
import com.jinproject.features.collection.model.ItemCollection
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.min

@Composable
internal fun CollectionList(
    collectionUiState: CollectionUiState,
    searchCharSequence: CharSequence,
    isFilterMode: Boolean,
    triggerFilterMode: (Boolean) -> Unit,
    configuration: Configuration = LocalConfiguration.current,
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    navigateToDetail: (ItemCollection) -> Unit,
    dispatchEvent: (CollectionEvent) -> Unit,
) {
    val horizontalPadding = 12.dp
    val itemWidth =
        (configuration.screenWidthDp.dp - horizontalPadding * 2 - 24.dp - 48.dp - 25.dp - 20.dp) / 2

    val items by remember(collectionUiState, isFilterMode, searchCharSequence) {
        derivedStateOf {
            collectionUiState.filterBySearchWord(
                s = searchCharSequence.toString(),
                isFilterMode = isFilterMode
            )
        }
    }

    val lazyListState = rememberLazyListState()

    LaunchedEffect(key1 = lazyListState) {
        snapshotFlow { lazyListState.isScrollInProgress }.collectLatest {
            keyboardController?.hide()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.Center),
            contentPadding = MiscellanousToolPaddingValues(
                vertical = 16.dp,
                horizontal = horizontalPadding
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = lazyListState,
        ) {
            items(
                items,
                key = { collection -> collection.id }) { collection ->
                val isSelected =
                    collectionUiState.collectionFilters.find { it.id == collection.id }?.isSelected
                        ?: false

                CollectionItem(
                    modifier = Modifier.animateItem(),
                    collection = collection,
                    isSelected = isSelected,
                    itemWidth = itemWidth,
                    isFilterMode = isFilterMode,
                    triggerFilterMode = triggerFilterMode,
                    navigateToDetail = navigateToDetail,
                    dispatchEvent = dispatchEvent,
                )
            }
        }
        AnimatedVisibility(
            modifier = Modifier
                .align(BottomCenter)
                .padding(bottom = 20.dp),
            visible = isFilterMode,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut(),
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                TextButton(
                    text = stringResource(id = com.jinproject.design_ui.R.string.apply_do),
                    onClick = {
                        dispatchEvent(CollectionEvent.SetFilteredCollection)
                        triggerFilterMode(false)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth()
                        .shadow(4.dp, RoundedCornerShape(100.dp))
                )
            }
        }
    }

}

@Composable
private fun CollectionItem(
    modifier: Modifier = Modifier,
    collection: ItemCollection,
    isSelected: Boolean,
    itemWidth: Dp,
    isFilterMode: Boolean,
    triggerFilterMode: (Boolean) -> Unit,
    navigateToDetail: (ItemCollection) -> Unit,
    dispatchEvent: (CollectionEvent) -> Unit,
) {
    val stat =
        remember(collection.stats) {
            collection.stats.entries.joinToString("\n") { entry ->
                if (entry.key.last() == '%')
                    "${entry.key.dropLast(1)} ${entry.value}%"
                else
                    "${entry.key} ${entry.value}"
            }
        }
    val item = remember(collection.items) {
        collection.items.joinToString("\n") { item -> "${item.name} * ${item.count}" }
    }
    val price = remember(collection.items) {
        collection.items.sumOf { it.price * it.count }.toString()
    }

    val animateState by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = tween(250),
        label = ""
    )
    val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(5.dp, shape = RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20.dp))
            .combinedClickableAvoidingDuplication(
                onClick = {
                    if (!isFilterMode)
                        navigateToDetail(collection)
                    else
                        dispatchEvent(CollectionEvent.AddFilteringCollectionId(collection.id))
                },
                onLongClick = {
                    triggerFilterMode(!isFilterMode)
                },
            )
            .drawWithCache {
                val iconSize = 18.dp
                    .toPx()
                    .toInt()
                val padding = 25f
                val xPos = iconSize.toFloat()
                val yPos = padding + 20f

                val path =
                    PathParser()
                        .parsePathString("M192 416c8.188 0 16.38-3.125 22.62-9.375l256-256C476.9 144.4 480 136.2 480 128c0-18.28-14.95-32-32-32c-8.188 0-16.38 3.125-22.62 9.375L192 338.8 60.62 206.4C54.38 200.2 46.19 196 38 196c-17.05 0-32 14.95-32 32c0 8.188 3.125 16.38 9.375 22.62l128 128C175.6 412.9 183.8 416 192 416z")
                        .toPath()
                        .apply {
                            fillBounds(
                                strokeWidthPx = 1f,
                                maxWidth = iconSize,
                                maxHeight = iconSize,
                            )
                            translate(Offset(x = xPos, y = yPos))

                        }

                onDrawWithContent {
                    drawContent()

                    if (isFilterMode) {
                        drawCircle(
                            color = Color.Black,
                            radius = iconSize / 2f + 18f,
                            center = Offset(
                                x = xPos + iconSize / 2f,
                                y = yPos + iconSize / 2f - 8f
                            ),
                            style = Stroke(1f),

                            )
                        drawCircle(
                            color = surfaceVariantColor.copy(alpha = if (isSelected) 1f else 0.5f),
                            radius = iconSize / 2f + 10f,
                            center = Offset(
                                x = xPos + iconSize / 2f,
                                y = yPos + iconSize / 2f - 8f
                            ),
                            blendMode = BlendMode.Multiply
                        )
                        clipRect(
                            left = xPos,
                            right = (xPos + iconSize) * animateState,
                        ) {
                            drawPath(path, color = Color.White, style = Fill)
                        }
                    }
                }
            }
            .graphicsLayer {
                compositingStrategy = CompositingStrategy.ModulateAlpha
                alpha = if (isSelected && isFilterMode) 0.3f else 1f
            }
            .padding(vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val verticalPadding = 10.dp
            DescriptionSmallText(
                text = item,
                modifier = Modifier.width(itemWidth)
            )
            HorizontalSpacer(width = 5.dp)
            DefaultPainterImage(
                resId = com.jinproject.design_ui.R.drawable.ic_arrow_right_long,
                contentDescription = "Right Long Arrow",
            )
            HorizontalSpacer(width = 20.dp)
            DescriptionSmallText(
                text = stat,
                modifier = Modifier
                    .width(itemWidth)
                    .padding(vertical = verticalPadding)
            )
            HorizontalSpacer(width = 20.dp)
            DefaultPainterImage(
                resId = com.jinproject.design_ui.R.drawable.ic_arrow_right_small,
                contentDescription = "Right Arrow",
                modifier = Modifier
                    .clickableAvoidingDuplication {
                        navigateToDetail(collection)
                    },
            )
        }
        HorizontalDivider()
        VerticalSpacer(height = 10.dp)
        DescriptionSmallText(
            text = price + " " + stringResource(id = com.jinproject.design_ui.R.string.gold),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp, end = 20.dp),
            textAlign = TextAlign.End,
        )
    }
}

fun Path.fillBounds(strokeWidthPx: Float, maxWidth: Int, maxHeight: Int) {
    val pathSize = getBounds()
    val matrix = Matrix()

    val horizontalOffset = pathSize.left - strokeWidthPx / 2
    val verticalOffset = pathSize.top - strokeWidthPx / 2
    val scaleWidth = maxWidth / (pathSize.width + strokeWidthPx)
    val scaleHeight = maxHeight / (pathSize.height + strokeWidthPx)
    val scale = min(scaleHeight, scaleWidth)

    matrix.scale(scale, scale)
    matrix.translate(-horizontalOffset, -verticalOffset)

    transform(matrix)
}

@Composable
@Preview(showBackground = true)
private fun PreviewCollectionList(
    @PreviewParameter(CollectionUiStatePreviewParameter::class)
    collectionUiState: CollectionUiState,
) = MiscellaneousToolTheme {
    CollectionList(
        collectionUiState = collectionUiState,
        searchCharSequence = "",
        isFilterMode = false,
        triggerFilterMode = {},
        navigateToDetail = {},
        dispatchEvent = {},
    )
}

@Composable
@Preview(showBackground = true)
private fun PreviewCollectionListBySearched(
    @PreviewParameter(CollectionUiStatePreviewParameter::class)
    collectionUiState: CollectionUiState,
) = MiscellaneousToolTheme {
    CollectionList(
        collectionUiState = collectionUiState,
        searchCharSequence = "데미지",
        isFilterMode = false,
        triggerFilterMode = {},
        navigateToDetail = {},
        dispatchEvent = {},
    )
}