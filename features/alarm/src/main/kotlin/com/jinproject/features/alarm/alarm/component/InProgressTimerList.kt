package com.jinproject.features.alarm.alarm.component

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.jinproject.core.util.doOnLocaleLanguage
import com.jinproject.core.util.to12Hour
import com.jinproject.core.util.toMeridiem
import com.jinproject.design_compose.component.DialogState
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.theme.Typography
import com.jinproject.design_compose.utils.PreviewMiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.features.alarm.alarm.AlarmUiState
import com.jinproject.features.alarm.alarm.AlarmUiStatePreviewParameter
import com.jinproject.features.alarm.alarm.item.TimerState
import com.jinproject.features.core.utils.appendBoldText
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun InProgressTimerItem(
    timerState: TimerState,
    context: Context = LocalContext.current,
    onClearAlarm: (Int, String) -> Unit,
    onOpenDialog: (DialogState) -> Unit,
    onCloseDialog: () -> Unit
) {
    Column(
        modifier = Modifier.clickable {
            onOpenDialog(
                DialogState(
                    header = "${timerState.bossName} ${context.getString(R.string.delete_something)}",
                    positiveMessage = context.getString(R.string.yes),
                    negativeMessage = context.getString(R.string.no),
                    onPositiveCallback = {
                        onClearAlarm(timerState.id, timerState.bossName)
                        onCloseDialog()
                    },
                    onNegativeCallback = { onCloseDialog() }
                )
            )
        }
    ) {
        Text(
            text = buildAnnotatedString {
                appendBoldText(
                    text = timerState.bossName,
                    color = MaterialTheme.colorScheme.onBackground
                )
                append(" ${timerState.dateTime.format(DateTimeFormatter.ofPattern("MM/dd"))} (")
                appendBoldText(
                    text = timerState.dateTime.dayOfWeek.getDisplayName(
                        TextStyle.SHORT,
                        Locale.getDefault()
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                append(") ${timerState.dateTime.toMeridiem()} ")
                appendBoldText(
                    text = timerState.dateTime.to12Hour().toString(),
                    color = MaterialTheme.colorScheme.onBackground
                )
                append(
                    "${
                        context.doOnLocaleLanguage(
                            onKo = stringResource(id = R.string.hour),
                            onElse = " :"
                        )
                    } "
                )
                appendBoldText(
                    text = timerState.dateTime.minute.toString(),
                    color = MaterialTheme.colorScheme.onBackground
                )
                append(
                    "${
                        context.doOnLocaleLanguage(
                            onKo = stringResource(id = R.string.minute),
                            onElse = " :"
                        )
                    } "
                )
                appendBoldText(
                    text = timerState.dateTime.second.toString(),
                    color = MaterialTheme.colorScheme.onBackground
                )
                append(stringResource(id = R.string.second))
            },
            style = Typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.fillMaxWidth()
        )
        VerticalSpacer(height = 16.dp)
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewInProgressTimerItem(
    @PreviewParameter(AlarmUiStatePreviewParameter::class)
    alarmUiState: AlarmUiState,
) {
    PreviewMiscellaneousToolTheme {
        InProgressTimerItem(
            timerState = alarmUiState.timerList.first(),
            onClearAlarm = { _, _ -> },
            onOpenDialog = {},
            onCloseDialog = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewInProgressTimerList(
    @PreviewParameter(AlarmUiStatePreviewParameter::class)
    alarmUiState: AlarmUiState
) {
    PreviewMiscellaneousToolTheme {
        LazyColumn {
            items(
                items = alarmUiState.timerList
            ) { timerState ->
                InProgressTimerItem(
                    timerState = timerState,
                    onClearAlarm = { _, _ -> },
                    onOpenDialog = {},
                    onCloseDialog = {}
                )
            }
        }
    }
}