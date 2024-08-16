package com.jinproject.design_compose.component.lazyList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.PreviewMiscellaneousToolTheme
import com.jinproject.design_compose.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun BoxWithConstraintsScope.VerticalScrollBar(
    scrollBarState: ScrollBarState,
    lazyListState: LazyGridState,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    density: Density = LocalDensity.current,
    headerItemHeight: Dp,
    perItemHeight: Dp,
) {
    val scrollBarViewHeight = 24.dp
    val maxHeight = with(density) {
        maxHeight.toPx() - scrollBarViewHeight.toPx()
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .align(Alignment.CenterEnd)
            .pointerInput(Unit) {
                detectVerticalDragGestures { change, dragAmount ->
                    change.consume()

                    scrollBarState.onScroll(-dragAmount / (maxHeight) * scrollBarState.threshold)

                    coroutineScope.launch {
                        val index = when {
                            scrollBarState.offset <= headerItemHeight.toPx() -> 0
                            else -> {
                                val newScrollOffset =
                                    (scrollBarState.offset - headerItemHeight.toPx()) / perItemHeight.toPx() + 2

                                (newScrollOffset.coerceAtLeast(0f)).toInt()
                            }
                        }

                        lazyListState.scrollToItem(index)
                    }
                }
            },
    ) {
        Image(
            painter = painterResource(
                id = R.drawable.ic_scroll
            ),
            contentDescription = "Scroll Image",
            modifier = Modifier
                .width(16.dp)
                .height(24.dp)
                .offset {
                    IntOffset(
                        x = 0,
                        y = (scrollBarState.progress * maxHeight).toInt()
                    )
                }
                .background(MaterialTheme.colorScheme.surface, CircleShape)
                .alpha(0.5f)
                .padding(vertical = 4.dp)
        )
    }
}

fun Modifier.addScrollBarNestedScrollConnection(
    timer: TimeScheduler,
    isUpperScrollActive: Boolean,
    scrollBarState: ScrollBarState,
) = this.nestedScroll(
    ScrollBarNestedScrollConnection(
        setTime = timer::setTime,
        cancel = timer::cancel,
        isUpperScrollActive = isUpperScrollActive,
        onScroll = scrollBarState::onScroll,
    )
)

class ScrollBarNestedScrollConnection(
    private val setTime: () -> Unit,
    private val cancel: () -> Unit,
    private val isUpperScrollActive: Boolean,
    private val onScroll: (Float) -> Unit,
) : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        if (isUpperScrollActive)
            setTime()
        else
            cancel()

        onScroll(available.y)

        return Offset.Zero
    }
}

@Composable
@Preview
private fun PreviewVerticalScrollBar() = PreviewMiscellaneousToolTheme {
    BoxWithConstraints {
        VerticalScrollBar(
            scrollBarState = rememberScrollBarState(viewHeight = maxHeight.value).apply {
                this.changeOffset(maxHeight.value)
            },
            lazyListState = rememberLazyGridState(),
            headerItemHeight = 100.dp,
            perItemHeight = 60.dp
        )
    }
}