package com.jinproject.features.symbol.symbol

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.component.FooterText
import com.jinproject.design_compose.component.TextButton
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.core.utils.checkAuthorityDrawOverlays
import com.jinproject.features.core.utils.checkPermissions
import com.jinproject.features.symbol.guildmark.component.ImagePixels
import com.jinproject.features.symbol.guildmark.rememberGuildMarkManager
import com.jinproject.features.symbol.symbol.component.SymbolLayout

private val MediaStorePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
    Manifest.permission.READ_MEDIA_IMAGES
else
    Manifest.permission.WRITE_EXTERNAL_STORAGE

@Composable
internal fun SymbolScreen(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    configuration: Configuration = LocalConfiguration.current,
    navigateToGallery: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
) {
    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { results ->
            val deniedPermissions = results.filter { !it.value }
            deniedPermissions.forEach { result ->
                if (result.key == MediaStorePermission)
                    showSnackBar(
                        SnackBarMessage(
                            headerMessage = context.getString(R.string.symbol_permission_headline),
                            contentMessage = context.getString(R.string.symbol_permission_content)
                        )
                    )
            }
        }

    val activityForResultLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode != Activity.RESULT_OK)
                showSnackBar(
                    SnackBarMessage(
                        headerMessage = context.getString(R.string.symbol_guildMark_permission_headline),
                        contentMessage = context.getString(R.string.symbol_guildMark_permission_content),
                    )
                )
            else
                navigateToGallery()
        }

    val imageSize = configuration.screenWidthDp / 2.5

    SymbolLayout(
        modifier = Modifier.fillMaxSize(),
        headline = stringResource(id = R.string.symbol_headline),
        headLineContent = {
            Image(
                painter = painterResource(id = R.drawable.test),
                contentDescription = "Test Image",
                modifier = Modifier
                    .size(imageSize.dp)
                    .align(Alignment.CenterHorizontally)
            )
            VerticalSpacer(height = 8.dp)
            FooterText(
                text = stringResource(id = R.string.symbol_headline_content),
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
        },
        desc = stringResource(id = R.string.symbol_description),
        descriptionContent = {
            ImagePixels(
                modifier = Modifier
                    .size((imageSize / 12).dp)
                    .border(1.dp, color = MaterialTheme.colorScheme.onBackground),
                guildMarkManager = rememberGuildMarkManager(
                    bitMap = BitmapFactory.decodeResource(context.resources, R.drawable.test),
                    slider = 0f
                )
            )
            VerticalSpacer(height = 8.dp)
            FooterText(
                text = stringResource(id = R.string.symbol_description_content),
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
        },
        footer = stringResource(id = R.string.symbol_footer),
        footerContent = {
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.symbol_button_getImage),
                onClick = {
                    checkPermissions(
                        context = context,
                        permissions = setOf(
                            MediaStorePermission,
                        ),
                        onGranted = {
                            if (checkAuthorityDrawOverlays(
                                    context = context
                                ) { intent ->
                                    activityForResultLauncher.launch(intent)
                                }
                            )
                                navigateToGallery()
                        },
                        permissionLauncher = permissionLauncher,
                    )
                },
            )
        }
    )
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
private fun PreviewSymbolScreen() = MiscellaneousToolTheme {
    SymbolScreen(
        navigateToGallery = {},
        showSnackBar = {},
    )
}