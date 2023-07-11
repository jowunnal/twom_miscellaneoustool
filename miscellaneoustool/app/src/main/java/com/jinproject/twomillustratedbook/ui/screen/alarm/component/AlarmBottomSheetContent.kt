package com.jinproject.twomillustratedbook.ui.screen.alarm.component

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.NumberPicker
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.ui.MainActivity
import com.jinproject.twomillustratedbook.ui.screen.alarm.item.TimeState
import com.jinproject.twomillustratedbook.ui.screen.compose.component.DefaultButton
import com.jinproject.twomillustratedbook.ui.screen.compose.component.HorizontalSpacer
import com.jinproject.twomillustratedbook.ui.screen.compose.component.VerticalSpacer
import com.jinproject.twomillustratedbook.utils.PreviewMiscellaneousToolTheme
import com.jinproject.twomillustratedbook.utils.findActivity
import com.jinproject.twomillustratedbook.utils.tu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AlarmBottomSheetContent(
    timeState: TimeState,
    selectedBossName: String,
    context: Context = LocalContext.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    setHourChanged: (Int) -> Unit,
    setMinutesChanged: (Int) -> Unit,
    setSecondsChanged: (Int) -> Unit,
    onStartAlarm: (String) -> Unit,
    onCloseBottomSheet: () -> Unit,
    showRewardedAd: (() -> Unit) -> Unit
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_handle_bar),
                contentDescription = "HandleBar",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.scrim)
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
                    .size(24.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_x),
                    contentDescription = "ExitIcon",
                    tint = MaterialTheme.colorScheme.scrim
                )
            }
        }
        Text(
            text = selectedBossName,
            fontSize = 18.tu,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.outline,
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

        Row {
            DefaultButton(
                content = stringResource(id = R.string.start_do),
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        val billingModule = (context.findActivity() as MainActivity).billingModule

                        billingModule.queryPurchase { purchaseList ->
                            coroutineScope.launch(Dispatchers.Main) {
                                if (billingModule.checkPurchased(
                                        purchaseList = purchaseList,
                                        productId = "ad_remove"
                                    )
                                ) {
                                    showRewardedAd {
                                        onStartAlarm(selectedBossName)
                                    }
                                } else
                                    onStartAlarm(selectedBossName)
                            }
                        }
                        onCloseBottomSheet()
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
        dividersColor = MaterialTheme.colorScheme.primary,
        value = timeState.hour,
        onValueChange = { hour -> setHourChanged(hour) },
        range = 0..23,
        textStyle = TextStyle(color = MaterialTheme.colorScheme.outline)
    )

    HorizontalSpacer(width = 16.dp)

    NumberPicker(
        dividersColor = MaterialTheme.colorScheme.primary,
        value = timeState.minutes,
        onValueChange = { minutes -> setMinutesChanged(minutes) },
        range = 0..59,
        textStyle = TextStyle(color = MaterialTheme.colorScheme.outline)
    )

    HorizontalSpacer(width = 16.dp)

    NumberPicker(
        dividersColor = MaterialTheme.colorScheme.primary,
        value = timeState.seconds,
        onValueChange = { seconds -> setSecondsChanged(seconds) },
        range = 0..59,
        textStyle = TextStyle(color = MaterialTheme.colorScheme.outline)
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewAlarmBottomSheetContent() =
    PreviewMiscellaneousToolTheme {
        AlarmBottomSheetContent(
            timeState = TimeState(
                day = com.jinproject.domain.model.WeekModel.Mon,
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