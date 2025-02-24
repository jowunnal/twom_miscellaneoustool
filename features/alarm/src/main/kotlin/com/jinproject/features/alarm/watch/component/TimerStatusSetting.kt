package com.jinproject.features.alarm.watch.component

import android.content.Context
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
import com.jinproject.design_ui.R
import com.jinproject.features.core.utils.checkAuthorityDrawOverlays

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
                val isOverlayGranted = checkAuthorityDrawOverlays(context) { intent ->
                    permissionLauncher.launch(intent)
                }
                if (isOverlayGranted && watchStatus != ButtonStatus.OFF) {
                    startOverlayService(true)
                    setWatchStatus(ButtonStatus.OFF)
                }
            },
            onClickNo = {
                when (checkAuthorityDrawOverlays(context) { intent ->
                    permissionLauncher.launch(intent)
                } && watchStatus != ButtonStatus.ON) {
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
