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
    startFromTop: Boolean = true,
): ScrollBarState {
    val maxHeight = viewHeight.coerceAtLeast(0f)

    val state = remember {
        ScrollBarState(
            maxHeight = maxHeight,
            startFromTop = startFromTop,
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
    private val startFromTop: Boolean,
    private val setTime: () -> Unit,
    private val cancel: () -> Unit,
) {
    var threshold by mutableFloatStateOf(maxHeight)
        private set

    private var offset by mutableFloatStateOf(if(maxHeight <= 0f) -1f else 0f)

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
        if(threshold > 0f && offset < 0f)
            offset = if (startFromTop) 0f else threshold
    }

    fun updateUpperScrollActiveState(bool: Boolean) {
        this.isUpperScrollActive = bool
    }

    fun changeOffset(offset: Float) {
        this.offset = offset
    }
}