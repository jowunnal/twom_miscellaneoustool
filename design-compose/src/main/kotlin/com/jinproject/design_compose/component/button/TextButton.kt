package com.jinproject.design_compose.component.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.utils.PreviewMiscellaneousToolTheme
import com.jinproject.design_compose.theme.Typography

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

@Preview()
@Composable
private fun PreviewDefaultButton() =
    PreviewMiscellaneousToolTheme {
        TextButton(
            text = "버튼",
            onClick = {}
        )
    }

@Preview()
@Composable
private fun PreviewSelectionButton() =
    PreviewMiscellaneousToolTheme {
        TextCombinedButton(
            text = "버튼",
            onClick = {}
        )
    }