package com.jinproject.design_compose.component.lazyList

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun rememberScrollBarState(
    viewHeight: Float,
    isUpperScrollActive: Boolean,
    startFromTop: Boolean = true,
): ScrollBarState {
    val maxHeight = viewHeight.coerceAtLeast(0f)

    val state = rememberSaveable(inputs = arrayOf(startFromTop), saver = ScrollBarState.Saver) {
        ScrollBarState(
            maxHeight = maxHeight,
            startFromTop = startFromTop,
        )
    }

    LaunchedEffect(key1 = maxHeight) {
        if (maxHeight > 0f)
            state.setScrollThreshold(maxHeight)
    }

    SideEffect {
        state.updateUpperScrollActiveState(isUpperScrollActive)
    }

    return state
}

class ScrollBarState(
    maxHeight: Float,
    private val startFromTop: Boolean,
) {
    var threshold by mutableFloatStateOf(maxHeight)
        private set

    private var offset by mutableFloatStateOf(0f)

    val progress by derivedStateOf {
        (offset.toDouble() / threshold.toDouble()).toFloat().coerceIn(0f..1f)
    }

    private var isUpperScrollActive by mutableStateOf(false)

    fun onScroll(delta: Float, setTime: () -> Unit, cancel: () -> Unit) {
        if (isUpperScrollActive)
            setTime()
        else
            cancel()

        offset = (offset - delta).coerceIn(0f..threshold)
    }

    fun setScrollThreshold(threshold: Float) {
        if (!startFromTop && threshold > this.threshold) {
            offset = (threshold - this.threshold).coerceAtLeast(0f)
        }
        this.threshold = threshold
    }

    fun updateUpperScrollActiveState(bool: Boolean) {
        this.isUpperScrollActive = bool
    }

    fun changeOffset(offset: Float) {
        this.offset = offset
    }

    companion object {
        val Saver = listSaver(
            save = { listOf(it.threshold, it.startFromTop, it.offset) },
            restore = {
                ScrollBarState(
                    maxHeight = it[0] as Float,
                    startFromTop = it[1] as Boolean,
                ).apply {
                    changeOffset(it[2] as Float)
                }
            }
        )
    }
}