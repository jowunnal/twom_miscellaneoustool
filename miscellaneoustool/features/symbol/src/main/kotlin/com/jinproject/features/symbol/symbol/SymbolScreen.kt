package com.jinproject.features.symbol.symbol

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.component.button.TextButton
import com.jinproject.design_compose.component.text.FooterText
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.features.core.AuthManager
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.core.utils.checkAuthorityDrawOverlays
import com.jinproject.features.core.utils.checkPermissions
import com.jinproject.features.symbol.SymbolRoute
import com.jinproject.features.symbol.guildmark.component.ImagePixels
import com.jinproject.features.symbol.guildmark.rememberGuildMarkManager
import com.jinproject.features.symbol.symbol.component.SymbolLayout

internal val MediaStorePermissionSet =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        setOf(READ_MEDIA_IMAGES, READ_MEDIA_VISUAL_USER_SELECTED)
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        setOf(READ_MEDIA_IMAGES)
    else
        setOf(READ_EXTERNAL_STORAGE)

@Composable
internal fun SymbolScreen(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    configuration: Configuration = LocalConfiguration.current,
    navigateToGallery: () -> Unit,
    navigateToGenerateImage: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
    navigateToAuthGraph: () -> Unit,
) {
    var bool: SymbolRoute = remember {
        SymbolRoute.Symbol
    }

    val navigateNextScreen = {
        if (bool is SymbolRoute.Gallery)
            navigateToGallery()
        else if (bool is SymbolRoute.GenerateImage) {
            if (AuthManager.isActive)
                navigateToGenerateImage()
            else {
                navigateToAuthGraph()
                showSnackBar(
                    SnackBarMessage(
                        headerMessage = context.getString(R.string.auth_sign_in_required),
                        contentMessage = context.getString(R.string.auth_sign_in_required_detail)
                    )
                )
            }
        }
    }

    val activityForResultLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            if (checkAuthorityDrawOverlays(
                    context = context
                ) {
                    showSnackBar(
                        SnackBarMessage(
                            headerMessage = context.getString(R.string.symbol_guildMark_permission_headline),
                            contentMessage = context.getString(R.string.symbol_guildMark_permission_content),
                        )
                    )
                }
            )
                navigateNextScreen()
        }

    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { results ->
            val deniedPermissions = results.filter { !it.value }

            if (deniedPermissions.isEmpty()) {
                if (checkAuthorityDrawOverlays(
                        context = context
                    ) { intent ->
                        activityForResultLauncher.launch(intent)
                    }
                )
                    navigateNextScreen()
            } else {
                deniedPermissions.forEach { result ->
                    if (result.key in MediaStorePermissionSet)
                        showSnackBar(
                            SnackBarMessage(
                                headerMessage = context.getString(R.string.symbol_permission_headline),
                                contentMessage = context.getString(R.string.symbol_permission_content)
                            )
                        )
                }
            }
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
                    bool = SymbolRoute.Gallery
                    checkPermissions(
                        context = context,
                        permissions = MediaStorePermissionSet,
                        onGranted = {
                            if (checkAuthorityDrawOverlays(
                                    context = context,
                                    registerForActivityResult = { intent ->
                                        activityForResultLauncher.launch(intent)
                                    })
                            )
                                navigateNextScreen()

                        },
                        permissionLauncher = permissionLauncher,
                    )
                },
            )

            VerticalSpacer(height = 10.dp)

            TextButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.symbol_button_generateImage),
                onClick = {
                    bool = SymbolRoute.GenerateImage
                    checkPermissions(
                        context = context,
                        permissions = MediaStorePermissionSet,
                        onGranted = {
                            if (checkAuthorityDrawOverlays(
                                    context = context,
                                    registerForActivityResult = { intent ->
                                        activityForResultLauncher.launch(intent)
                                    })
                            )
                                navigateNextScreen()

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
        navigateToGenerateImage = {},
        navigateToAuthGraph = {},
    )
}