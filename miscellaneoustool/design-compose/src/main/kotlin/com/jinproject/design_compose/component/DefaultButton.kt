package com.jinproject.design_compose.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.PreviewMiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.design_compose.theme.Typography

@Composable
fun DefaultButton(
    content: String,
    modifier: Modifier = Modifier,
    style: TextStyle = Typography.bodyLarge,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentPaddingValues: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .background(backgroundColor)
            .padding(contentPaddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = content,
            style = style,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Stable
enum class ButtonStatus(val displayName: String) {
    ON(displayName = "ON"),
    OFF(displayName = "OFF");

    operator fun not() = when(this) {
        ON -> OFF
        OFF -> ON
    }
}


@Composable
fun SelectionButton(
    buttonStatus: ButtonStatus,
    modifier: Modifier = Modifier,
    textYes: String,
    textNo: String,
    onClickYes: () -> Unit,
    onClickNo: () -> Unit
) {
    val indicatorBias by animateFloatAsState(
        when (buttonStatus) {
            ButtonStatus.ON -> 1f
            ButtonStatus.OFF -> -1f
        }
    )

    BoxWithConstraints(
        modifier = modifier
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(35.dp))
            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(35.dp))
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(100.dp))
                    .clickable(onClick = onClickYes),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = textNo,
                    color = MaterialTheme.colorScheme.primary,
                    style = Typography.bodyLarge
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(100.dp))
                    .clickable(onClick = onClickNo),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = textYes,
                    color = MaterialTheme.colorScheme.primary,
                    style = Typography.bodyLarge
                )
            }
        }
        VerticalSpacer(height = 6.dp)
        Box(
            modifier = Modifier
                .height(maxHeight)
                .width(maxWidth / 2)
                .padding(3.dp)
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(100.dp))
                .align(BiasAlignment(indicatorBias, 0f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = buttonStatus.displayName,
                color = MaterialTheme.colorScheme.onPrimary,
                style = Typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview()
@Composable
private fun PreviewDefaultButton() =
    PreviewMiscellaneousToolTheme {
        DefaultButton(content = "버튼")
    }

@Preview()
@Composable
private fun PreviewSelectionButton() =
    PreviewMiscellaneousToolTheme {
        SelectionButton(
            buttonStatus = ButtonStatus.ON,
            modifier = Modifier
                .width(200.dp)
                .height(26.dp),
            textYes = stringResource(id = R.string.on),
            textNo = stringResource(id = R.string.off),
            onClickYes = {},
            onClickNo = {}
        )
    }