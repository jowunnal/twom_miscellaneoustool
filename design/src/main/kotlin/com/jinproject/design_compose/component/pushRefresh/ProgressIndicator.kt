package com.jinproject.design_compose.component.pushRefresh

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefreshIndicatorTransform
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.utils.PreviewMiscellaneousToolTheme
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun MTProgressIndicatorRotating(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        MTProgressIndicatorInfiniteRotating()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MTPullRefreshIndicator(
    modifier: Modifier = Modifier,
    state: PullRefreshState,
    isRefreshing: Boolean,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .size(40.dp)
            .pullRefreshIndicatorTransform(state)
            .background(
                color = MaterialTheme.colorScheme.surface,
            ),
        contentAlignment = Alignment.Center
    ) {
        Crossfade(
            targetState = isRefreshing,
            animationSpec = tween(100),
            label = "CrossFade Refreshing"
        ) { isRefreshing ->
            if (isRefreshing) {
                MTProgressIndicatorInfiniteRotating()
            } else {
                MTProgressIndicatorRotatingByParam(
                    progress = state.progress,
                )
            }
        }
    }
}

@Composable
fun MTPushRefreshIndicator(
    modifier: Modifier = Modifier,
    state: PushRefreshState,
    isRefreshing: Boolean,
) {
    Box(
        modifier = modifier
            .width(120.dp)
            .height(40.dp)
            .offset {
                IntOffset(
                    x = 0,
                    y = -40.dp.roundToPx()-state.offset.roundToInt()
                )
            }
            .shadow(6.dp * state.progress, shape = RoundedCornerShape(100.dp))
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(100.dp)
            )
            .graphicsLayer {
                alpha = state.progress
            },
        contentAlignment = Alignment.Center
    ) {
        Crossfade(
            targetState = isRefreshing,
            animationSpec = tween(100),
            label = "CrossFade Refreshing"
        ) { isRefreshing ->
            if (isRefreshing) {
                MTProgressIndicatorInfiniteRotating()
            } else {
                MTProgressIndicatorRotatingByParam(state.progress)
            }
        }
    }
}

@Composable
private fun MTProgressIndicatorRotatingByParam(
    progress: Float,
    modifier: Modifier = Modifier,
    counter: Int = 8,
    color: Color = MaterialTheme.colorScheme.onSurface,
) {
    Canvas(modifier = modifier) {

        val stroke = Stroke(
            width = 1.dp.toPx(),
        )

        val offset = MTProgressIndicatorOffset(
            centerOffset = Offset(center.x, center.y),
            radius = 10.dp.toPx()
        )

        val path = offset.getProgressPath(
            counter = counter,
            endInclusive = (progress * counter).toInt()
        )

        drawPath(path, color = color, style = stroke)
    }
}

@Composable
fun MTProgressIndicatorInfiniteRotating(
    modifier: Modifier = Modifier,
    counter: Int = 8,
    color: Color = MaterialTheme.colorScheme.onSurface,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite Transition")
    val rotationAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            tween(8 * 80, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation Animation"
    )

    Canvas(modifier = modifier) {

        val stroke = Stroke(
            width = 1.dp.toPx(),
        )

        val offset = MTProgressIndicatorOffset(
            centerOffset = Offset(center.x, center.y),
            radius = 10.dp.toPx()
        )

        val path = offset.getProgressPath(
            counter = counter,
            endInclusive = counter
        )

        drawPath(path, color = color, style = stroke, alpha = 0.2f)

        val rotatePath = offset.getProgressPath(
            counter = counter,
            endInclusive = 1
        )

        for (i in 1..4) {
            rotate(degrees = (rotationAnimation.toInt() + i) * (360f / counter)) {
                drawPath(
                    rotatePath,
                    color = color,
                    style = stroke,
                    alpha = (0.2f + 0.2f * i).coerceIn(0f, 1f)
                )
            }
        }
    }
}

class MTProgressIndicatorOffset(
    private val centerOffset: Offset,
    private val radius: Float,
) {
    fun getProgressPath(counter: Int, endInclusive: Int) = Path().apply {
        for (i in 1..endInclusive) {
            val angle = 360.0 / counter * i

            moveTo(
                getPoint(ceta = angle).x,
                getPoint(ceta = angle).y
            )
            lineTo(
                getPoint(ceta = angle).x / 2f,
                getPoint(ceta = angle).y / 2f
            )
        }
    }

    private fun getPoint(ceta: Double): Offset {
        return Offset(
            x = centerOffset.x + radius * cos(Math.toRadians(ceta - 90.0).toFloat()),
            y = centerOffset.y + radius * sin(Math.toRadians(ceta - 90.0).toFloat())
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewStepMateProgressIndicatorInfiniteRotating() = PreviewMiscellaneousToolTheme {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp),
        contentAlignment = Alignment.Center
    ) {
        MTProgressIndicatorInfiniteRotating()
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewStepMateProgressIndicatorRotatingByParam() = PreviewMiscellaneousToolTheme {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp),
        contentAlignment = Alignment.Center
    ) {
        MTProgressIndicatorRotatingByParam(
            1f
        )
    }
}