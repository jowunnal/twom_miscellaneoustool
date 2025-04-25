package com.jinproject.features.alarm.setting.component

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.component.ButtonStatus
import com.jinproject.design_compose.component.SelectionButton
import com.jinproject.design_compose.theme.Typography
import com.jinproject.design_compose.utils.PreviewMiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.features.alarm.setting.service.OverlayService
import com.jinproject.features.core.AnalyticsEvent
import com.jinproject.features.core.compose.LocalAnalyticsLoggingEvent
import com.jinproject.features.core.utils.checkAuthorityDrawOverlays

@Composable
fun TimeStatusSetting(
    context: Context = LocalContext.current,
) {
    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {

        }

    var watchStatus by remember {
        mutableStateOf(context.checkOverlayServiceState())
    }
    val localAnalyticsLoggingEvent = LocalAnalyticsLoggingEvent.current

    Row(
        modifier = Modifier
            .height(36.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.watch_setting_title),
            style = Typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.weight(1f)
        )

        SelectionButton(
            buttonStatus = watchStatus,
            modifier = Modifier.weight(1f),
            textYes = stringResource(id = R.string.on),
            textNo = stringResource(id = R.string.off),
            onClickYes = {
                val isOverlayGranted = checkAuthorityDrawOverlays(context) { intent ->
                    permissionLauncher.launch(intent)
                }
                if (isOverlayGranted && watchStatus != ButtonStatus.OFF) {
                    context.startOverlayService(true)
                    watchStatus = ButtonStatus.OFF
                }
            },
            onClickNo = {
                when (checkAuthorityDrawOverlays(context) { intent ->
                    permissionLauncher.launch(intent)
                } && watchStatus != ButtonStatus.ON) {
                    true -> {
                        context.startOverlayService(
                            status = false
                        )
                        localAnalyticsLoggingEvent(AnalyticsEvent.ASCT)
                        watchStatus = ButtonStatus.ON
                    }

                    false -> {}
                }
            }
        )
    }
}

private fun Context.startOverlayService(
    status: Boolean
) = this.startForegroundService(
    Intent(
        this,
        OverlayService::class.java
    ).apply {
        putExtra("status", status)
    }
)

private fun Context.checkOverlayServiceState(): ButtonStatus {
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    return notificationManager.activeNotifications.find { it.id == 999 }?.let {
        ButtonStatus.ON
    } ?: ButtonStatus.OFF
}

@Preview
@Composable
private fun PreviewTimeStatusSetting() {
    PreviewMiscellaneousToolTheme {
        TimeStatusSetting()
    }
}
