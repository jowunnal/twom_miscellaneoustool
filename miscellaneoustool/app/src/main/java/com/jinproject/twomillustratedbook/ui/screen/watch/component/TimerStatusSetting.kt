package com.jinproject.twomillustratedbook.ui.screen.watch.component

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.ui.screen.compose.component.VerticalSpacer
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.Typography
import com.jinproject.twomillustratedbook.ui.screen.watch.item.ButtonStatus
import com.jinproject.twomillustratedbook.utils.TwomIllustratedBookPreview

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

    val indicatorBias by animateFloatAsState(
        when (watchStatus) {
            ButtonStatus.ON -> 1f
            ButtonStatus.OFF -> -1f
        }
    )

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
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.off),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = Typography.bodyLarge
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(100.dp))
                        .clickable {
                            when (checkAuthorityDrawOverlays(context) { intent ->
                                permissionLauncher.launch(intent)
                            }) {
                                true -> {
                                    startOverlayService(false)
                                    setWatchStatus(ButtonStatus.ON)
                                }

                                false -> {}
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.on),
                        color = MaterialTheme.colorScheme.primary,
                        style = Typography.bodyLarge
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
                    style = Typography.bodyLarge,
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
            watchStatus = ButtonStatus.OFF,
            setWatchStatus = {},
            startOverlayService = {}
        )
    }
}
