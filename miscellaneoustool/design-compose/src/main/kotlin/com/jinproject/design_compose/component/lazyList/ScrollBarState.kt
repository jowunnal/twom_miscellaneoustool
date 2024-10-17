package com.jinproject.design_compose.component.lazyList

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun rememberScrollBarState(
    viewHeight: Float,
    timer: TimeScheduler,
    isUpperScrollActive: Boolean,
): ScrollBarState {
    val maxHeight = viewHeight.coerceAtLeast(0f)

    val state = remember {
        ScrollBarState(
            maxHeight = maxHeight,
            setTime = timer::setTime,
            cancel = timer::cancel,
        )
    }

    SideEffect {
        state.setScrollThreshold(maxHeight)
        state.updateUpperScrollActiveState(isUpperScrollActive)
    }

    return state
}

class ScrollBarState(
    maxHeight: Float,
    private val setTime: () -> Unit,
    private val cancel: () -> Unit,
) {
    var offset by mutableFloatStateOf(0f)
        private set

    var threshold by mutableFloatStateOf(maxHeight)
        private set

    val progress by derivedStateOf { (offset.toDouble() / threshold.toDouble()).toFloat() }

    private var isUpperScrollActive by mutableStateOf(false)

    fun onScroll(delta: Float) {
        if (isUpperScrollActive)
            setTime()
        else
            cancel()

        offset = (offset - delta).coerceIn(0f..threshold)
    }

    fun setScrollThreshold(threshold: Float) {
        this.threshold = threshold
    }

    fun updateUpperScrollActiveState(bool: Boolean) {
        this.isUpperScrollActive = bool
    }

    fun changeOffset(offset: Float) {
        this.offset = offset
    }
}