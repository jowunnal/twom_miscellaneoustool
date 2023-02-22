package com.jinproject.twomillustratedbook.ui.screen.watch.component

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.jinproject.twomillustratedbook.ui.Service.OverlayService
import com.jinproject.twomillustratedbook.ui.screen.alarm.item.TimerState
import com.jinproject.twomillustratedbook.ui.screen.compose.component.VerticalSpacer
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.deepGray
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.primary
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.white
import com.jinproject.twomillustratedbook.ui.screen.watch.item.ButtonStatus
import com.jinproject.twomillustratedbook.utils.TwomIllustratedBookPreview
import com.jinproject.twomillustratedbook.utils.tu
import java.util.ArrayList

@Composable
fun TimeStatusSetting(
    timerList: List<TimerState>,
    watchStatus: ButtonStatus,
    fontSize: Int,
    activityContext: Context,
    setWatchStatus: (ButtonStatus) -> Unit,
    checkAuthorityDrawOverlays: (Context, (Intent) -> Unit) -> Boolean
) {
    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {

        }

    val indicatorBias by animateFloatAsState(
        when (watchStatus) {
            ButtonStatus.ON -> 1f
            ButtonStatus.OFF -> -1f
        }
    )

    if(watchStatus == ButtonStatus.ON)
        LaunchedEffect(key1 = timerList) {
            when (checkAuthorityDrawOverlays(activityContext) { intent ->
                permissionLauncher.launch(intent)
            }) {
                true -> {
                    activityContext.startForegroundService(
                        Intent(
                            activityContext,
                            OverlayService::class.java
                        ).apply {
                            putExtra("status", false)
                            putExtra("fontSize", fontSize)
                            if (timerList.isNotEmpty())
                                putParcelableArrayListExtra(
                                    "timerList",
                                    timerList as ArrayList
                                )
                        }
                    )
                }
                false ->{}
            }
        }

    Row(
        modifier = Modifier
            .height(36.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "현재 시간 항상 보기",
            fontSize = 16.tu,
            fontWeight = FontWeight.W700,
            color = deepGray,
            modifier = Modifier.weight(1f)
        )
        BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .border(1.dp, primary, RoundedCornerShape(35.dp))
                .background(white, RoundedCornerShape(35.dp))
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(100.dp))
                        .clickable {
                            when (checkAuthorityDrawOverlays(activityContext) { intent ->
                                permissionLauncher.launch(intent)
                            }) {
                                true -> {
                                    activityContext.startForegroundService(
                                        Intent(
                                            activityContext,
                                            OverlayService::class.java
                                        ).apply {
                                            putExtra("status", true)
                                            putExtra("fontSize", fontSize)
                                        }
                                    )
                                    setWatchStatus(ButtonStatus.OFF)
                                }
                                false -> {}
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "OFF",
                        color = primary,
                        fontSize = 16.tu,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(100.dp))
                        .clickable {
                            when (checkAuthorityDrawOverlays(activityContext) { intent ->
                                permissionLauncher.launch(intent)
                            }) {
                                true -> {
                                    activityContext.startForegroundService(
                                        Intent(
                                            activityContext,
                                            OverlayService::class.java
                                        ).apply {
                                            putExtra("status", false)
                                            putExtra("fontSize", fontSize)
                                            if (timerList.isNotEmpty())
                                                putParcelableArrayListExtra(
                                                    "timerList",
                                                    timerList as ArrayList
                                                )
                                        }
                                    )
                                    setWatchStatus(ButtonStatus.ON)
                                }
                                false -> {}
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ON",
                        color = primary,
                        fontSize = 16.tu,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            VerticalSpacer(height = 6.dp)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(maxWidth / 2)
                    .padding(3.dp)
                    .background(primary, RoundedCornerShape(100.dp))
                    .align(BiasAlignment(indicatorBias, 0f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = watchStatus.displayName,
                    color = white,
                    fontSize = 16.tu,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewTimeStatusSetting() {
    TwomIllustratedBookPreview {
        TimeStatusSetting(
            timerList = emptyList(),
            watchStatus = ButtonStatus.OFF,
            fontSize = 14,
            setWatchStatus = {},
            activityContext = object : FragmentActivity() {},
            checkAuthorityDrawOverlays = { _, _ -> false }
        )
    }
}
