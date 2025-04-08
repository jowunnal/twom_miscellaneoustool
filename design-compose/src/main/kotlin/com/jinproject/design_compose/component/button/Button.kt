package com.jinproject.design_compose.component.button

import android.os.SystemClock
import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_compose.utils.PreviewMiscellaneousToolTheme
import com.jinproject.design_ui.R

@Composable
fun DefaultIconButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    enabled: Boolean = true,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    iconSize: Dp = 48.dp,
    backgroundTint: Color = MaterialTheme.colorScheme.surface,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val iconColor = when (enabled) {
        true -> iconTint
        false -> iconTint.copy(alpha = 0.3f)
    }

    DefaultButton(
        modifier = Modifier
            .size(iconSize)
            .then(modifier),
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(0.dp),
        backgroundColor = backgroundTint,
        contentPaddingValues = PaddingValues(horizontal = 0.dp, vertical = 0.dp),
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = "Default Icon Button",
            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(iconColor)
        )
    }
}

@Composable
fun DefaultButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: RoundedCornerShape = RoundedCornerShape(100.dp),
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentPaddingValues: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    content: @Composable () -> Unit,
) {
    val color = when (enabled) {
        true -> backgroundColor
        false -> backgroundColor.copy(alpha = 0.3f)
    }

    Column(
        modifier = modifier
            .background(color, shape)
            .clickableAvoidingDuplication(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            )
            .padding(contentPaddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        content()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DefaultCombinedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onDoubleClick: () -> Unit,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentPaddingValues: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(100.dp))
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
                onLongClick = onLongClick,
                onDoubleClick = onDoubleClick,
            )
            .padding(contentPaddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        content()
    }
}

@Composable
fun Modifier.clickableAvoidingDuplication(
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication? = null,
    enabled: Boolean = true,
    onClick: () -> Unit,
): Modifier {
    val avoidDuplicationClickEvent = remember {
        AvoidDuplicationClickEvent(onClick)
    }

    SideEffect {
        avoidDuplicationClickEvent.changeOnClick(onClick)
    }

    return this.clickable(
        interactionSource = interactionSource,
        indication = indication,
        enabled = enabled,
        onClick = avoidDuplicationClickEvent::onClick,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Modifier.combinedClickableAvoidingDuplication(
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication? = null,
    enabled: Boolean = true,
    onClick: () -> Unit,
    onDoubleClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
): Modifier {
    val avoidDuplicationClickEvent = remember {
        AvoidDuplicationClickEvent(onClick)
    }

    SideEffect {
        avoidDuplicationClickEvent.changeOnClick(onClick)
    }

    return this.combinedClickable(
        interactionSource = interactionSource,
        indication = indication,
        enabled = enabled,
        onClick = avoidDuplicationClickEvent::onClick,
        onLongClick = onLongClick,
        onDoubleClick = onDoubleClick,
    )
}

private class AvoidDuplicationClickEvent(
    onClicked: () -> Unit,
) {
    val currentClickTime get() = SystemClock.uptimeMillis()
    var lastClickTime = currentClickTime

    private var _onClicked = onClicked

    fun onClick() {
        val elapsedTime = currentClickTime - lastClickTime
        lastClickTime = currentClickTime

        if (elapsedTime <= MIN_CLICK_INTERVAL) {
            return
        }

        _onClicked()
    }

    fun changeOnClick(lambda: () -> Unit) {
        _onClicked = lambda
    }

    companion object {
        const val MIN_CLICK_INTERVAL = 300L
    }
}

@Preview
@Composable
private fun PreviewDefaultIconButton() = MiscellaneousToolTheme {
    DefaultIconButton(
        icon = R.drawable.ic_arrow_left,
        onClick = {},
        iconTint = MaterialTheme.colorScheme.onSurface,
        iconSize = 16.dp
    )
}

@Preview()
@Composable
private fun PreviewDefaultButton() =
    PreviewMiscellaneousToolTheme {
        DefaultButton(
            onClick = {},
        ) {

        }
    }

