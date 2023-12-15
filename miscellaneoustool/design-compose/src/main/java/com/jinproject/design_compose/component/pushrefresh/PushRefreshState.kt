package com.jinproject.design_compose.component.pushrefresh

import androidx.compose.animation.core.animate
import androidx.compose.foundation.MutatorMutex
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberPushRefreshState(
    maxHeight: Float,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    density: Density = LocalDensity.current,
): PushRefreshState {

    val maxHeightPx = with(density) {
        maxHeight.toDp().toPx()
    }

    val scope = rememberCoroutineScope()

    val state = remember {
        PushRefreshState(
            onRefresh = onRefresh,
            maxHeight = maxHeightPx,
            scope = scope
        )
    }

    SideEffect {
        state.setIsRefreshState(isRefreshing)
    }

    return state
}


class PushRefreshState(
    val onRefresh: () -> Unit,
    val maxHeight: Float,
    private val scope: CoroutineScope,
) {
    private var _offset by mutableFloatStateOf(0f)
    private var isRefreshing by mutableStateOf(false)

    private val offset get() = _offset

    val progress get() = (-offset / maxHeight).coerceAtMost(1f)

    fun onPush(delta: Float): Float {
        if (isRefreshing)
            return 0f

        val newOffset = (_offset + delta).coerceIn(-maxHeight, 0f)
        val consumed = newOffset - _offset
        _offset = newOffset

        return consumed
    }

    fun onRelease(velocity: Float): Float {
        if (isRefreshing)
            return 0f

        if (progress == 1f) {
            onRefresh()
        }

        val consumed = when {
            _offset == 0f -> 0f
            velocity > 0f -> 0f
            else -> velocity
        }

        animateIndicatorTo()

        return consumed
    }

    private val mutatorMutex = MutatorMutex()

    private fun animateIndicatorTo(target: Float = 0f) = scope.launch {
        mutatorMutex.mutate {
            animate(initialValue = offset, targetValue = target) { value, _ ->
                _offset = value
            }
        }
    }

    fun setIsRefreshState(bool: Boolean) {
        if (isRefreshing != bool) {
            isRefreshing = bool

            if (!isRefreshing) {
                animateIndicatorTo()
            }
        }
    }
}