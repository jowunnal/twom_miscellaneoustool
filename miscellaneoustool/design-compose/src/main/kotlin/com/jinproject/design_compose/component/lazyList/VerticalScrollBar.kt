package com.jinproject.design_compose.component.lazyList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.PreviewMiscellaneousToolTheme
import com.jinproject.design_compose.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun BoxWithConstraintsScope.VerticalScrollBar(
    scrollBarState: ScrollBarState,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    density: Density = LocalDensity.current,
    upperScrollAlpha: Float,
    scrollToItem: suspend () -> Unit,
) {
    val scrollBarHeight = 24.dp
    val scrollBarWidth = 16.dp
    val interactableScrollBarWidth = scrollBarWidth + 12.dp
    val interactableScrollBarHeight = scrollBarHeight + 4.dp
    val maxHeight = with(density) {
        maxHeight.toPx() - scrollBarHeight.toPx()
    }
    val maxWidth = with(density) {
        maxWidth.roundToPx() - interactableScrollBarWidth.roundToPx()
    }

    Column(
        modifier = Modifier
            .width(interactableScrollBarWidth)
            .height(interactableScrollBarHeight)
            .graphicsLayer {
                alpha = upperScrollAlpha
            }
            .offset {
                IntOffset(
                    x = maxWidth,
                    y = (scrollBarState.progress * maxHeight).toInt()
                )
            }
            .pointerInput(Unit) {
                detectVerticalDragGestures { change, dragAmount ->
                    change.consume()

                    scrollBarState.onScroll(-dragAmount / (maxHeight) * scrollBarState.threshold)

                    coroutineScope.launch {
                        scrollToItem()
                    }
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(
                id = R.drawable.ic_scroll
            ),
            contentDescription = "Scroll Image",
            alpha = 0.5f,
            modifier = Modifier
                .width(scrollBarWidth)
                .height(scrollBarHeight)
                .background(MaterialTheme.colorScheme.surface, CircleShape)
                .padding(vertical = 4.dp)
        )
    }

}

fun Modifier.addScrollBarNestedScrollConnection(
    scrollBarState: ScrollBarState,
) = this.nestedScroll(
    ScrollBarNestedScrollConnection(
        onScroll = scrollBarState::onScroll,
    )
)

class ScrollBarNestedScrollConnection(
    private val onScroll: (Float) -> Unit,
) : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {

        onScroll(available.y)

        return Offset.Zero
    }
}

@Composable
@Preview
private fun PreviewVerticalScrollBar() = PreviewMiscellaneousToolTheme {
    BoxWithConstraints {
        VerticalScrollBar(
            scrollBarState = rememberScrollBarState(
                viewHeight = maxHeight.value,
                timer = TimeScheduler(rememberCoroutineScope()),
                isUpperScrollActive = true,
            ).apply {
                this.changeOffset(maxHeight.value)
            },
            scrollToItem = {},
            upperScrollAlpha = 1f,
        )
    }
}