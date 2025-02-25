package com.jinproject.design_compose.component.pushRefresh

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Velocity

@Composable
fun Modifier.pushRefresh(
    pushRefreshState: PushRefreshState,
    enabled: Boolean = true,
) = this.pushRefresh(
    onPush = pushRefreshState::onPush,
    onRelease = pushRefreshState::onRelease,
    enabled = enabled,
)


private fun Modifier.pushRefresh(
    onPush: (pushDelta: Float) -> Float,
    onRelease: suspend (flingVelocity: Float) -> Float,
    enabled: Boolean = true,
) = this.nestedScroll(
    PushRefreshNestedScrollConnection(
        onPush,
        onRelease,
        enabled,
    )
)

/**
 * 아래로 내리면, delta < 0
 * 위로 올리면, delta > 0
 */
private class PushRefreshNestedScrollConnection(
    private val onPush: (pullDelta: Float) -> Float,
    private val onRelease: suspend (flingVelocity: Float) -> Float,
    private val enabled: Boolean,
) : NestedScrollConnection {

    override fun onPreScroll(
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        return when {
            !enabled -> Offset.Zero
            source == NestedScrollSource.Drag && available.y > 0f -> Offset(0f, onPush(available.y))
            else -> Offset.Zero
        }
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        return when {
            !enabled -> Offset.Zero
            source == NestedScrollSource.Drag && available.y < 0f -> Offset(0f, onPush(available.y))
            else -> Offset.Zero
        }
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        return Velocity(0f, onRelease(available.y))
    }
}