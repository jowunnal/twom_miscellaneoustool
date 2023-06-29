package com.miscellaneoustool.app.ui.screen.alarm.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.NumberPicker
import com.miscellaneoustool.app.R
import com.miscellaneoustool.domain.model.WeekModel
import com.miscellaneoustool.app.ui.screen.alarm.item.TimeState
import com.miscellaneoustool.app.ui.screen.compose.component.DefaultButton
import com.miscellaneoustool.app.ui.screen.compose.component.HorizontalSpacer
import com.miscellaneoustool.app.ui.screen.compose.component.VerticalSpacer
import com.miscellaneoustool.app.ui.screen.compose.theme.deepGray
import com.miscellaneoustool.app.ui.screen.compose.theme.gray
import com.miscellaneoustool.app.ui.screen.compose.theme.primary
import com.miscellaneoustool.app.utils.TwomIllustratedBookPreview
import com.miscellaneoustool.app.utils.tu
import kotlinx.coroutines.launch

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
                day = com.miscellaneoustool.domain.model.WeekModel.Mon,
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