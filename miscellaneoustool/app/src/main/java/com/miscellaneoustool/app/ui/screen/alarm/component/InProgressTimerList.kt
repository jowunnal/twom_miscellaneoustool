package com.miscellaneoustool.app.ui.screen.alarm.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.miscellaneoustool.app.ui.screen.alarm.item.TimeState
import com.miscellaneoustool.app.ui.screen.alarm.item.TimerState
import com.miscellaneoustool.app.ui.screen.compose.component.DialogState
import com.miscellaneoustool.app.ui.screen.compose.component.VerticalSpacer
import com.miscellaneoustool.app.ui.screen.compose.theme.Typography
import com.miscellaneoustool.app.utils.TwomIllustratedBookPreview
import com.miscellaneoustool.app.utils.appendBoldText

@Composable
fun InProgressTimerList(
    timerStateList: List<TimerState>,
    onClearAlarm: (Int, String) -> Unit,
    onOpenDialog: (DialogState) -> Unit,
    onCloseDialog: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "현재 진행중인 알람 내역",
                style = Typography.headlineMedium,
                color = MaterialTheme.colorScheme.outline
            )
            VerticalSpacer(height = 20.dp)
        }
        itemsIndexed(
            timerStateList,
            key = { index, item -> item.id }) { index: Int, item: TimerState ->
            Column(
                modifier = Modifier.clickable {
                    onOpenDialog(
                        DialogState(
                            header = "${item.bossName}의 알람을 제거하시겠습니까?",
                            positiveMessage = "예",
                            negativeMessage = "아니오",
                            onPositiveCallback = {
                                onClearAlarm(item.id, item.bossName)
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
                            text = item.bossName,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        append(" (")
                        appendBoldText(
                            text = item.timeState.day.displayName,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        append(") ")
                        appendBoldText(
                            text = item.timeState.hour.toString(),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        append("시 ")
                        appendBoldText(
                            text = item.timeState.minutes.toString(),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        append("분 ")
                        appendBoldText(
                            text = item.timeState.seconds.toString(),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        append("초")
                    },
                    style = Typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.fillMaxWidth()
                )
                VerticalSpacer(height = 16.dp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewInProgressTimerList() {
    TwomIllustratedBookPreview {
        InProgressTimerList(
            timerStateList = listOf(
                TimerState(
                    id = 1,
                    bossName = "보스1",
                    timeState = TimeState(
                        day = com.miscellaneoustool.domain.model.WeekModel.Mon,
                        hour = 14,
                        minutes = 22,
                        seconds = 25
                    )
                ),
                TimerState(
                    id = 2,
                    bossName = "보스2",
                    timeState = TimeState(
                        day = com.miscellaneoustool.domain.model.WeekModel.Mon,
                        hour = 16,
                        minutes = 18,
                        seconds = 33
                    )
                ),
                TimerState(
                    id = 3,
                    bossName = "보스3",
                    timeState = TimeState(
                        day = com.miscellaneoustool.domain.model.WeekModel.Mon,
                        hour = 13,
                        minutes = 34,
                        seconds = 49
                    )
                )
            ),
            onClearAlarm = { _, _ -> },
            onOpenDialog = {},
            onCloseDialog = {}
        )
    }
}