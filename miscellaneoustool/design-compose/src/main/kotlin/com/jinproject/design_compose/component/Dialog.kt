package com.jinproject.design_compose.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jinproject.design_compose.PreviewMiscellaneousToolTheme
import com.jinproject.design_compose.component.text.DescriptionLargeText
import com.jinproject.design_compose.component.text.DescriptionSmallText
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.blue
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.red
import com.jinproject.design_compose.theme.Typography

@Stable
data class DialogState(
    val header: String,
    val content: String = "",
    val positiveMessage: String,
    val negativeMessage: String,
    val onPositiveCallback: () -> Unit = {},
    val onNegativeCallback: () -> Unit,
) {
    companion object {
        fun getInitValue() = DialogState(
            header = "",
            content = "",
            positiveMessage = "",
            negativeMessage = "",
            onNegativeCallback = {}
        )
    }
}

@Composable
fun DialogCustom(
    dialogState: DialogState,
    properties: DialogProperties = DialogProperties(),
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            DialogCustomContent(
                dialogState = dialogState,
                onPositiveCallback = dialogState.onPositiveCallback,
                onNegativeCallback = dialogState.onNegativeCallback
            )
        }
    }
}

@Composable
private fun DialogCustomContent(
    dialogState: DialogState,
    onPositiveCallback: () -> Unit = {},
    onNegativeCallback: () -> Unit,
) {
    Column() {
        Column(modifier = Modifier.padding(horizontal = 35.dp, vertical = 19.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                DescriptionLargeText(text = dialogState.header)
            }
            VerticalSpacer(height = 12.dp)
            if(dialogState.content.isNotBlank())
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    DescriptionSmallText(text = dialogState.content)
                }
        }
        HorizontalDivider()
        Row(
            modifier = Modifier.height(44.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = 21.dp,
                        vertical = 9.dp
                    )
                    .weight(1f)
                    .clickable { onNegativeCallback() }
            ) {
                Text(
                    text = dialogState.negativeMessage,
                    style = Typography.bodyLarge,
                    color = blue.color,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            if(dialogState.positiveMessage.isNotBlank()) {
                VerticalDivider()
                Column(
                    modifier = Modifier
                        .padding(
                            horizontal = 21.dp,
                            vertical = 9.dp
                        )
                        .weight(1f)
                        .clickable { onPositiveCallback() }
                ) {
                    Text(
                        text = dialogState.positiveMessage,
                        style = Typography.bodyLarge,
                        color = red.color,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun PreviewDialogCustomWithPositiveMessage() {
    PreviewMiscellaneousToolTheme {
        DialogCustom(
            dialogState = DialogState(
                header = "헤더메세지는 이렇게 나옵니다.",
                content = "컨텐트메세지는 이렇게 나옵니다.",
                positiveMessage = "네",
                negativeMessage = "아뇨",
                onNegativeCallback = {}
            ),
            onDismissRequest = {}
        )
    }
}

@Composable
@Preview
private fun PreviewDialogCustomConfirm() {
    PreviewMiscellaneousToolTheme {
        DialogCustom(
            dialogState = DialogState(
                header = "헤더메세지는 이렇게 나옵니다.",
                content = "컨텐트메세지는 이렇게 나옵니다.",
                positiveMessage = "",
                negativeMessage = "확인",
                onNegativeCallback = {}
            ),
            onDismissRequest = {}
        )
    }
}