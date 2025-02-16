package com.jinproject.design_compose.component.lazyList

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.BoxScope
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
    content: @Composable BoxWithConstraintsScope.() -> Unit,
) {
    require(scrollableState is LazyListState || scrollableState is LazyGridState) {
        "LazyList and LazyGrid only granted"
    }

    val timeScheduler = rememberTimeScheduler()

    val scrollBarState = rememberScrollBarState(
        viewHeight = viewHeight,
        timer = timeScheduler,
        isUpperScrollActive = isUpperScrollActive,
        startFromTop = startFromTop,
    )

    val upperScrollAlpha by animateFloatAsState(
        targetValue = if (timeScheduler.isRunning) 1f else 0f,
        label = "Alpha Animate State"
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .addScrollBarNestedScrollConnection(scrollBarState = scrollBarState),
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
            onClick = {
                coroutineScope.launch {
                    if (scrollableState is LazyListState)
                        scrollableState.animateScrollToItem(0)
                    else if (scrollableState is LazyGridState)
                        scrollableState.animateScrollToItem(0)

                    scrollBarState.changeOffset(0f)
                }
            },
        )

        if (isUpperScrollActive)
            VerticalScrollBar(
                scrollBarState = scrollBarState,
                scrollToItem = { amount ->
                    scrollableState.scrollBy(amount)
                },
                upperScrollAlpha = upperScrollAlpha
            )
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