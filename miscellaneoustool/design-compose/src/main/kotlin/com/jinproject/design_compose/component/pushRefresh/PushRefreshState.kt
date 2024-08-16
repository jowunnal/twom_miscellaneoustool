package com.jinproject.design_compose.component.pushRefresh

import androidx.compose.animation.core.animate
import androidx.compose.foundation.MutatorMutex
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberPushRefreshState(
    onRefresh: () -> Unit,
    isRefreshing: Boolean,
    threshold: Dp = PushRefreshState.DEFAULT_THRESHOLD,
    density: Density = LocalDensity.current,
): PushRefreshState {

    val thresholdPx = with(density) {
        threshold.toPx()
    }

    val scope = rememberCoroutineScope()

    val state = remember {
        PushRefreshState(
            onRefresh = onRefresh,
            threshold = thresholdPx,
            scope = scope
        )
    }

    SideEffect {
        state.setRefresh(isRefreshing)
    }

    return state
}


class PushRefreshState(
    val onRefresh: () -> Unit,
    private val threshold: Float,
    private val scope: CoroutineScope,
) {
    private var offset by mutableFloatStateOf(0f)
    private var isRefreshing by mutableStateOf(false)
    private val adjustedPushedOffset by derivedStateOf {
        offset * 1f
    }

    val progress get() = offset / threshold

    fun onPush(delta: Float): Float {
        if (isRefreshing)
            return 0f

        val newOffset = (offset - delta).coerceIn(0f .. threshold)
        val consumed = -(newOffset - offset)
        offset = newOffset

        return consumed
    }

    fun onRelease(velocity: Float): Float {
        if (isRefreshing)
            return 0f

        if (progress == 1f) {
            onRefresh()
        }

        val consumed = when {
            offset == 0f -> 0f
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
                offset = value
            }
        }
    }

    fun setRefresh(bool: Boolean) {
        if (isRefreshing != bool) {
            isRefreshing = bool

            if (!isRefreshing) {
                animateIndicatorTo()
            }
        }
    }

    companion object {
        val DEFAULT_THRESHOLD = 80.dp
    }
}