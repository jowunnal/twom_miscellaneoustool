package com.jinproject.design_compose.component.lazyList

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun rememberScrollBarState(
    viewHeight: Float,
): ScrollBarState {
    val maxHeight = viewHeight.coerceAtLeast(0f)

    val state = remember {
        ScrollBarState(
            maxHeight = maxHeight,
        )
    }

    SideEffect {
        state.setScrollThreshold(maxHeight)
    }

    return state
}

class ScrollBarState(
    maxHeight: Float,
) {
    var offset by mutableFloatStateOf(0f)
        private set

    var threshold by mutableFloatStateOf(maxHeight)
        private set

    val progress by derivedStateOf { (offset.toDouble() / threshold.toDouble()).toFloat() }

    fun onScroll(delta: Float) {
        offset = (offset - delta).coerceIn(0f..threshold)
    }

    fun setScrollThreshold(threshold: Float) {
        this.threshold = threshold
    }

    fun changeOffset(offset: Float) {
        this.offset = offset
    }
}