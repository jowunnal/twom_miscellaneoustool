package com.jinproject.twomillustratedbook.ui.screen.watch.component

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.PreviewMiscellaneousToolTheme
import com.jinproject.design_compose.component.ButtonStatus
import com.jinproject.design_compose.component.SelectionButton
import com.jinproject.design_compose.theme.Typography
import com.jinproject.twomillustratedbook.R

@Composable
fun TimeStatusSetting(
    watchStatus: ButtonStatus,
    context: Context = LocalContext.current,
    setWatchStatus: (ButtonStatus) -> Unit,
    startOverlayService: (Boolean) -> Unit
) {
    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {

        }

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
                when (checkAuthorityDrawOverlays(context) { intent ->
                    permissionLauncher.launch(intent)
                }) {
                    true -> {
                        startOverlayService(true)
                        setWatchStatus(ButtonStatus.OFF)
                    }

                    false -> {}
                }
            },
            onClickNo = {
                when (checkAuthorityDrawOverlays(context) { intent ->
                    permissionLauncher.launch(intent)
                }) {
                    true -> {
                        startOverlayService(false)
                        setWatchStatus(ButtonStatus.ON)
                    }

                    false -> {}
                }
            }
        )
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
    PreviewMiscellaneousToolTheme {
        TimeStatusSetting(
            watchStatus = ButtonStatus.OFF,
            setWatchStatus = {},
            startOverlayService = {}
        )
    }
}
