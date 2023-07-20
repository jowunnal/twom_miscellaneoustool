package com.jinproject.features.alarm.component

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
import com.jinproject.design_compose.PreviewMiscellaneousToolTheme
import com.jinproject.design_compose.component.DefaultButton
import com.jinproject.design_compose.component.HorizontalSpacer
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.tu
import com.jinproject.domain.model.WeekModel
import com.jinproject.features.alarm.R
import com.jinproject.features.alarm.item.TimeState
import com.jinproject.features.core.utils.findActivity
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