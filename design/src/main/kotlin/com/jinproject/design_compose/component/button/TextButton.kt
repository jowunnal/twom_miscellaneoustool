package com.jinproject.design_compose.component.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.component.HorizontalSpacer
import com.jinproject.design_compose.utils.PreviewMiscellaneousToolTheme
import com.jinproject.design_compose.theme.Typography
import com.jinproject.design_ui.R

@Composable
fun TextButton(
    text: String,
    modifier: Modifier = Modifier,
    contentPaddingValues: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    shape: RoundedCornerShape = RoundedCornerShape(100.dp),
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    DefaultButton(
        modifier = modifier,
        backgroundColor = MaterialTheme.colorScheme.primary,
        contentPaddingValues = contentPaddingValues,
        shape = shape,
        onClick = onClick,
        enabled = enabled,
    ) {
        Text(
            text = text,
            style = Typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun TextIconButton(
    text: String,
    modifier: Modifier = Modifier,
    contentPaddingValues: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    shape: RoundedCornerShape = RoundedCornerShape(100.dp),
    enabled: Boolean = true,
    icon: @Composable RowScope.() -> Unit,
    onClick: () -> Unit,
) {
    DefaultButton(
        modifier = modifier,
        backgroundColor = MaterialTheme.colorScheme.primary,
        contentPaddingValues = contentPaddingValues,
        shape = shape,
        onClick = onClick,
        enabled = enabled,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()
            HorizontalSpacer(4.dp)
            Text(
                text = text,
                style = Typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@Composable
fun TextCombinedButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {},
    onDoubleClick: () -> Unit = {},
) {
    DefaultCombinedButton(
        modifier = modifier,
        backgroundColor = MaterialTheme.colorScheme.primary,
        contentPaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        onClick = onClick,
        onLongClick = onLongClick,
        onDoubleClick = onDoubleClick,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview
@Composable
private fun PreviewDefaultButton() =
    PreviewMiscellaneousToolTheme {
        TextButton(
            text = "버튼",
            onClick = {}
        )
    }

@Preview
@Composable
private fun PreviewTextIconButton() =
    PreviewMiscellaneousToolTheme {
        TextIconButton(
            text = "버튼",
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_play_rounded),
                    contentDescription = "Play Ad",
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            },
            onClick = {}
        )
    }

@Preview
@Composable
private fun PreviewSelectionButton() =
    PreviewMiscellaneousToolTheme {
        TextCombinedButton(
            text = "버튼",
            onClick = {}
        )
    }