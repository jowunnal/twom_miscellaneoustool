package com.jinproject.twomillustratedbook.ui.screen.alarm.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.AMPMHours
import com.chargemap.compose.numberpicker.Hours
import com.chargemap.compose.numberpicker.HoursNumberPicker
import com.chargemap.compose.numberpicker.NumberPicker
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.domain.model.WeekModel
import com.jinproject.twomillustratedbook.ui.screen.alarm.item.AlarmItem
import com.jinproject.twomillustratedbook.ui.screen.alarm.item.TimeState
import com.jinproject.twomillustratedbook.ui.screen.alarm.item.TimerState
import com.jinproject.twomillustratedbook.ui.screen.compose.component.DefaultButton
import com.jinproject.twomillustratedbook.ui.screen.compose.component.HorizontalSpacer
import com.jinproject.twomillustratedbook.ui.screen.compose.component.VerticalSpacer
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.black
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.deepGray
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.gray
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.primary
import com.jinproject.twomillustratedbook.utils.*
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun AlarmBottomSheetContent(
    timeState: TimeState,
    selectedBossName: String,
    setHourChanged: (Int) -> Unit,
    setMinutesChanged: (Int) -> Unit,
    setSecondsChanged: (Int) -> Unit,
    onStartAlarm: (String) -> Unit,
    onCloseBottomSheet: () -> Unit,
    showRewardedAd: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_handle_bar),
                contentDescription = "HandleBar"
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick =
                {
                    coroutineScope.launch {
                        onCloseBottomSheet()
                    }
                },
                modifier = Modifier
                    .height(24.dp)
                    .width(24.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_x),
                    contentDescription = "ExitIcon",
                    tint = gray
                )
            }
        }
        Text(
            text = selectedBossName,
            fontSize = 18.tu,
            fontWeight = FontWeight.ExtraBold,
            color = deepGray,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        VerticalSpacer(height = 16.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            NumberPickerDefault(
                timeState = timeState,
                setHourChanged = setHourChanged,
                setMinutesChanged = setMinutesChanged,
                setSecondsChanged = setSecondsChanged
            )
        }

        VerticalSpacer(height = 16.dp)

        Row() {
            DefaultButton(content = "시작하기", modifier = Modifier
                .weight(1f)
                .clickable {
                    onStartAlarm(selectedBossName)
                    onCloseBottomSheet()
                    showRewardedAd()
                }
            )
            /*
            HorizontalSpacer(width = 5.dp)
            DefaultButton(content = "서버로 전송", modifier = Modifier
                .weight(1f)
                .clickable { onCloseBottomSheet() })
             */
        }

    }
}

@Composable
private fun NumberPickerDefault(
    timeState: TimeState,
    setHourChanged: (Int) -> Unit,
    setMinutesChanged: (Int) -> Unit,
    setSecondsChanged: (Int) -> Unit
) {
    NumberPicker(
        dividersColor = primary,
        value = timeState.hour,
        onValueChange = { hour -> setHourChanged(hour) },
        range = 0..23,
        textStyle = TextStyle(color = gray)
    )

    HorizontalSpacer(width = 16.dp)

    NumberPicker(
        dividersColor = primary,
        value = timeState.minutes,
        onValueChange = { minutes -> setMinutesChanged(minutes) },
        range = 0..59,
        textStyle = TextStyle(color = gray)
    )

    HorizontalSpacer(width = 16.dp)

    NumberPicker(
        dividersColor = primary,
        value = timeState.seconds,
        onValueChange = { seconds -> setSecondsChanged(seconds) },
        range = 0..59,
        textStyle = TextStyle(color = gray)
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewAlarmBottomSheetContent() =
    TwomIllustratedBookPreview {
        AlarmBottomSheetContent(
            timeState = TimeState(
                day = WeekModel.Mon,
                hour = 13,
                minutes = 20,
                seconds = 34
            ),
            selectedBossName = "은둔자",
            setHourChanged = {},
            setMinutesChanged = {},
            setSecondsChanged = {},
            onStartAlarm = {},
            onCloseBottomSheet = {},
            showRewardedAd = {}
        )
    }