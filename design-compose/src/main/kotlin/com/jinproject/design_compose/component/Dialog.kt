package com.jinproject.design_compose.component

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jinproject.design_compose.utils.PreviewMiscellaneousToolTheme
import com.jinproject.design_compose.component.button.clickableAvoidingDuplication
import com.jinproject.design_compose.component.text.DescriptionLargeText
import com.jinproject.design_compose.component.text.DescriptionSmallText
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.blue
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.red
import com.jinproject.design_compose.theme.Typography

@Composable
fun rememberDialogState(dialogState: DialogState = DialogState.getInitValue()) = remember {
    mutableStateOf(dialogState)
}

@Stable
data class DialogState(
    val header: String,
    val content: String = "",
    val positiveMessage: String = "",
    val negativeMessage: String,
    val onPositiveCallback: (DialogState) -> Unit = {},
    val onNegativeCallback: (DialogState) -> Unit,
) {
    var visibility by mutableStateOf(false)
        private set

    fun changeVisibility(bool: Boolean) {
        visibility = bool
    }

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

fun DialogState.getShownDialogState(
    onPositiveCallback: (() -> Unit)? = null,
    onNegativeCallback: (() -> Unit)? = null,
): DialogState = copy(
    onPositiveCallback = { state ->
        onPositiveCallback?.let { it() } ?: this.onPositiveCallback(state)
        state.changeVisibility(false)
    },
    onNegativeCallback = { state ->
        onNegativeCallback?.let { it() } ?: this.onNegativeCallback(state)
        state.changeVisibility(false)
    },
).apply {
    changeVisibility(true)
}

@Composable
fun TextDialog(
    dialogState: DialogState,
    properties: DialogProperties = DialogProperties(),
    onDismissRequest: () -> Unit
) {
    if (dialogState.visibility) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = properties
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                TextDialogContent(
                    dialogState = dialogState,
                    onPositiveCallback = dialogState.onPositiveCallback,
                    onNegativeCallback = dialogState.onNegativeCallback,
                )
            }
        }
    }
}

@Composable
private fun TextDialogContent(
    dialogState: DialogState,
    onPositiveCallback: (DialogState) -> Unit = {},
    onNegativeCallback: (DialogState) -> Unit,
) {
    DefaultDialogContent(
        dialogState = dialogState,
        content = {
            DescriptionSmallText(text = dialogState.content)
        },
        onPositiveCallback = onPositiveCallback,
        onNegativeCallback = onNegativeCallback,
    )
}

@Composable
fun DefaultDialog(
    dialogState: DialogState,
    properties: DialogProperties = DialogProperties(),
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
) {
    if (dialogState.visibility) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = properties
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                DefaultDialogContent(
                    dialogState = dialogState,
                    onPositiveCallback = dialogState.onPositiveCallback,
                    onNegativeCallback = dialogState.onNegativeCallback,
                    content = content,
                )
            }
        }
    }
}


@Composable
private fun DefaultDialogContent(
    modifier: Modifier = Modifier,
    dialogState: DialogState,
    content: @Composable () -> Unit,
    onPositiveCallback: (DialogState) -> Unit = {},
    onNegativeCallback: (DialogState) -> Unit,
) {
    Column(modifier = modifier) {
        Column(modifier = Modifier.padding(horizontal = 35.dp, vertical = 19.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                DescriptionLargeText(text = dialogState.header)
            }
            VerticalSpacer(height = 12.dp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                content()
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
                    .clickableAvoidingDuplication { onNegativeCallback(dialogState) }
            ) {
                Text(
                    text = dialogState.negativeMessage,
                    style = Typography.bodyLarge,
                    color = blue.color,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            if (dialogState.positiveMessage.isNotBlank()) {
                VerticalDivider()
                Column(
                    modifier = Modifier
                        .padding(
                            horizontal = 21.dp,
                            vertical = 9.dp
                        )
                        .weight(1f)
                        .clickableAvoidingDuplication { onPositiveCallback(dialogState) }
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
        TextDialog(
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
        TextDialog(
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