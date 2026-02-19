package com.jinproject.features.alarm.alarm.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.NumberPicker
import com.jinproject.design_compose.component.HorizontalSpacer
import com.jinproject.design_compose.component.HorizontalWeightSpacer
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.component.button.TextButton
import com.jinproject.design_compose.component.text.DescriptionLargeText
import com.jinproject.design_compose.utils.PreviewMiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.features.alarm.alarm.AlarmUiState
import com.jinproject.features.alarm.alarm.AlarmUiStatePreviewParameter
import com.jinproject.features.core.AnalyticsEvent
import com.jinproject.features.core.compose.LocalAnalyticsLoggingEvent
import kotlinx.collections.immutable.ImmutableList
import java.time.ZonedDateTime

@Composable
fun AlarmBottomSheetContent(
    selectedBossName: String,
    overlaidBossList: ImmutableList<String>,
    onStartAlarm: (monsterName: String, deadTime: ZonedDateTime) -> Unit,
    onCloseBottomSheet: (Boolean) -> Unit
) {
    var deadTime: ZonedDateTime by remember {
        mutableStateOf(ZonedDateTime.now())
    }

    val event = LocalAnalyticsLoggingEvent.current

    var checkState by remember {
        mutableStateOf(selectedBossName in overlaidBossList)
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HorizontalWeightSpacer(1.5f)
            DescriptionLargeText(
                text = selectedBossName,
                modifier = Modifier
            )
            Checkbox(
                checked = checkState,
                onCheckedChange = { bool ->
                    checkState = bool
                },
            )
            HorizontalWeightSpacer(1f)
        }
        VerticalSpacer(height = 16.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            NumberPickerDefault(
                zonedDateTime = deadTime,
                setHourChanged = { hour ->
                    deadTime = deadTime.withHour(hour)
                },
                setMinutesChanged = { minute ->
                    deadTime = deadTime.withMinute(minute)
                },
                setSecondsChanged = { second ->
                    deadTime = deadTime.withSecond(second)
                }
            )
        }

        VerticalSpacer(height = 16.dp)

        TextButton(
            text = stringResource(id = R.string.start_do),
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onStartAlarm(selectedBossName, deadTime)
                event(
                    AnalyticsEvent.StartAlarm(
                        monsName = selectedBossName,
                        timeStamp = deadTime.toString(),
                    )
                )
                onCloseBottomSheet(checkState)
            }
        )

    }
}

@Composable
private fun NumberPickerDefault(
    zonedDateTime: ZonedDateTime,
    setHourChanged: (Int) -> Unit,
    setMinutesChanged: (Int) -> Unit,
    setSecondsChanged: (Int) -> Unit
) {
    NumberPicker(
        dividersColor = MaterialTheme.colorScheme.primary,
        value = zonedDateTime.hour,
        onValueChange = { hour -> setHourChanged(hour) },
        range = 0..23,
        textStyle = TextStyle(color = MaterialTheme.colorScheme.outline)
    )

    HorizontalSpacer(width = 16.dp)

    NumberPicker(
        dividersColor = MaterialTheme.colorScheme.primary,
        value = zonedDateTime.minute,
        onValueChange = { minutes -> setMinutesChanged(minutes) },
        range = 0..59,
        textStyle = TextStyle(color = MaterialTheme.colorScheme.outline)
    )

    HorizontalSpacer(width = 16.dp)

    NumberPicker(
        dividersColor = MaterialTheme.colorScheme.primary,
        value = zonedDateTime.second,
        onValueChange = { seconds -> setSecondsChanged(seconds) },
        range = 0..59,
        textStyle = TextStyle(color = MaterialTheme.colorScheme.outline)
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewAlarmBottomSheetContent(
    @PreviewParameter(AlarmUiStatePreviewParameter::class)
    alarmUiState: AlarmUiState,
) =
    PreviewMiscellaneousToolTheme {
        AlarmBottomSheetContent(
            selectedBossName = "은둔자",
            overlaidBossList = alarmUiState.overlaidBossList,
            onStartAlarm = { _, _ -> },
            onCloseBottomSheet = {},
        )
    }