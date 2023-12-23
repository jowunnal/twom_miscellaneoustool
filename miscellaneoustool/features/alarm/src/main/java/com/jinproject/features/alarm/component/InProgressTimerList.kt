package com.jinproject.features.alarm.component

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
import androidx.compose.ui.unit.dp
import com.jinproject.core.util.doOnLocaleLanguage
import com.jinproject.design_compose.PreviewMiscellaneousToolTheme
import com.jinproject.design_compose.component.DialogState
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.theme.Typography
import com.jinproject.domain.model.WeekModel
import com.jinproject.design_ui.R
import com.jinproject.features.alarm.item.TimeState
import com.jinproject.features.alarm.item.TimerState
import com.jinproject.features.core.utils.appendBoldText

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
                append(" (")
                appendBoldText(
                    text = context.doOnLocaleLanguage(
                        onKo = timerState.timeState.day.displayOnKo,
                        onElse = timerState.timeState.day.displayOnElse
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                append(") ${timerState.timeState.getMeridiem()} ")
                appendBoldText(
                    text = timerState.timeState.getTime12Hour().toString(),
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
                    text = timerState.timeState.minutes.toString(),
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
                    text = timerState.timeState.seconds.toString(),
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
private fun PreviewInProgressTimerItem() {
    PreviewMiscellaneousToolTheme {
        InProgressTimerItem(
            timerState = TimerState(
                id = 1,
                bossName = "보스1",
                timeState = TimeState(
                    day = WeekModel.Mon,
                    hour = 14,
                    minutes = 22,
                    seconds = 25
                )
            ),
            onClearAlarm = { _, _ -> },
            onOpenDialog = {},
            onCloseDialog = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewInProgressTimerList() {
    PreviewMiscellaneousToolTheme {
        LazyColumn {
            items(
                items = listOf(
                    TimerState(
                        id = 1,
                        bossName = "보스1",
                        timeState = TimeState(
                            day = WeekModel.Mon,
                            hour = 14,
                            minutes = 22,
                            seconds = 25
                        )
                    ),
                    TimerState(
                        id = 2,
                        bossName = "보스2",
                        timeState = TimeState(
                            day = WeekModel.Mon,
                            hour = 16,
                            minutes = 18,
                            seconds = 33
                        )
                    ),
                    TimerState(
                        id = 3,
                        bossName = "보스3",
                        timeState = TimeState(
                            day = WeekModel.Mon,
                            hour = 13,
                            minutes = 34,
                            seconds = 49
                        )
                    )
                )
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