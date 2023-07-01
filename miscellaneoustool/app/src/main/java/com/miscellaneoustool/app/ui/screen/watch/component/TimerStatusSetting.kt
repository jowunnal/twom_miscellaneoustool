package com.miscellaneoustool.app.ui.screen.watch.component

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import com.miscellaneoustool.app.ui.screen.alarm.item.TimerState
import com.miscellaneoustool.app.ui.screen.compose.component.VerticalSpacer
import com.miscellaneoustool.app.ui.screen.compose.theme.Typography
import com.miscellaneoustool.app.ui.screen.watch.item.ButtonStatus
import com.miscellaneoustool.app.ui.service.OverlayService
import com.miscellaneoustool.app.utils.TwomIllustratedBookPreview
import com.miscellaneoustool.app.utils.tu

@Composable
fun TimeStatusSetting(
    timerList: List<TimerState>,
    watchStatus: ButtonStatus,
    fontSize: Int,
    activityContext: Context,
    setWatchStatus: (ButtonStatus) -> Unit
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
            style = Typography.headlineSmall,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.weight(1f)
        )
        BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(35.dp))
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(35.dp))
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
                        color = MaterialTheme.colorScheme.onPrimary,
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
                        color = MaterialTheme.colorScheme.primary,
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
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(100.dp))
                    .align(BiasAlignment(indicatorBias, 0f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = watchStatus.displayName,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.tu,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private fun checkAuthorityDrawOverlays(
    context: Context,
    registerForActivityResult: (Intent) -> Unit
): Boolean { // 다른앱 위에 그리기 체크 : true = 권한있음 , false = 권한없음
    return if (!Settings.canDrawOverlays(context)) {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:" + context.packageName)
        )
        registerForActivityResult(intent)
        false
    } else {
        true
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
            activityContext = object : FragmentActivity() {}
        )
    }
}
