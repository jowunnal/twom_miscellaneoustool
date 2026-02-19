package com.jinproject.design_compose.component.lazyList

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.jinproject.design_ui.R
import com.jinproject.design_compose.component.button.DefaultIconButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ColumnScope.ScrollableLayout(
    viewHeight: Float,
    startFromTop: Boolean = true,
    scrollableState: ScrollableState = rememberLazyListState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    isUpperScrollActive: Boolean,
    timeScheduler: TimeScheduler = rememberTimeScheduler(),
    scrollBarState: ScrollBarState = rememberScrollBarState(
        viewHeight = viewHeight,
        isUpperScrollActive = isUpperScrollActive,
        startFromTop = startFromTop,
    ),
    content: @Composable BoxWithConstraintsScope.() -> Unit,
) {
    require(scrollableState is LazyListState || scrollableState is LazyGridState) {
        "LazyList and LazyGrid only granted"
    }

    val upperScrollAlpha by animateFloatAsState(
        targetValue = if (timeScheduler.isRunning) 1f else 0f,
        label = "Alpha Animate State"
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .addScrollBarNestedScrollConnection(
                scrollBarState = scrollBarState,
                setTime = timeScheduler::setTime,
                cancel = timeScheduler::cancel,
            ),
    ) {

        content()

        UpperScrollButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .graphicsLayer {
                    alpha = upperScrollAlpha
                    translationY = -50f
                }
                .shadow(1.dp, CircleShape),
            scrollableState = scrollableState,
            scrollBarState = scrollBarState,
            coroutineScope = coroutineScope,
            startFromTop = startFromTop,
        )

        if (isUpperScrollActive)
            VerticalScrollBar(
                scrollBarState = scrollBarState,
                timeScheduler = timeScheduler,
                scrollToItem = { amount ->
                    scrollableState.scrollBy(amount)
                },
                upperScrollAlpha = upperScrollAlpha
            )
    }
}

@Composable
fun UpperScrollButton(
    scrollableState: ScrollableState,
    scrollBarState: ScrollBarState,
    modifier: Modifier = Modifier,
    startFromTop: Boolean = true,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) {
    DefaultIconButton(
        icon = if (startFromTop) R.drawable.ic_arrow_up_to_start else R.drawable.ic_arrow_down_to_end,
        onClick = {
            val targetOffset = if(startFromTop) 0f else scrollBarState.threshold

            coroutineScope.launch {
                when (scrollableState) {
                    is LazyListState -> {
                        val targetIndex = if (startFromTop) 0 else scrollableState.layoutInfo.totalItemsCount.minus(1)
                        scrollableState.animateScrollToItem(targetIndex)
                        scrollBarState.changeOffset(targetOffset)
                    }
                    is LazyGridState -> {
                        val targetIndex = if (startFromTop) 0 else scrollableState.layoutInfo.totalItemsCount.minus(1)
                        scrollableState.animateScrollToItem(targetIndex)
                        scrollBarState.changeOffset(targetOffset)
                    }
                }
            }
        },
        iconTint = MaterialTheme.colorScheme.onSurface,
        iconSize = 32.dp,
        modifier = modifier,
    )
}
