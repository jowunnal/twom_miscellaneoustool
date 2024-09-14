package com.jinproject.features.symbol.guildmark

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jinproject.design_compose.component.BackButtonTitleAppBar
import com.jinproject.design_compose.component.DefaultLayout
import com.jinproject.design_compose.component.DescriptionSmallText
import com.jinproject.design_compose.component.HeadlineText
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.core.utils.checkAuthorityDrawOverlays
import com.jinproject.features.symbol.detail.DetailViewModel
import com.jinproject.features.symbol.getBitmapFromContentUri
import com.jinproject.features.symbol.guildmark.component.ColorSlider
import com.jinproject.features.symbol.guildmark.component.ImagePixels
import com.jinproject.features.symbol.guildmark.component.UsedColorInPixels

@Composable
internal fun GuildMarkScreen(
    guildMarkViewModel: DetailViewModel = hiltViewModel(),
    popBackStack: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
) {
    val imageUri by guildMarkViewModel.imageDetailState.collectAsStateWithLifecycle()

    GuildMarkScreen(
        imageUri = imageUri,
        popBackStack = popBackStack,
        showSnackBar = showSnackBar,
    )
}

@Composable
private fun GuildMarkScreen(
    context: Context = LocalContext.current,
    configuration: Configuration = LocalConfiguration.current,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    imageUri: Uri,
    popBackStack: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
) {

    var slider by remember {
        mutableFloatStateOf(0f)
    }
    val bitMap = getBitmapFromContentUri(
        context = context,
        imageUri = imageUri.toString(),
    )

    val guildMarkManager = rememberGuildMarkManager(bitMap = bitMap, slider = slider)
    val itemWidth = (configuration.screenWidthDp / 12).dp

    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode != Activity.RESULT_OK)
                showSnackBar(
                    SnackBarMessage(
                        headerMessage = context.getString(R.string.symbol_guildMark_permission_headline),
                        contentMessage = context.getString(R.string.symbol_guildMark_permission_content),
                    )
                )
        }

    DisposableEffect(key1 = Unit) {
        val intent = Intent(
            context,
            SymbolOverlayService::class.java
        )

        val observer = LifecycleEventObserver { source, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                if (checkAuthorityDrawOverlays(context = context) { intent ->
                        permissionLauncher.launch(intent)
                    }
                )
                    context.startForegroundService(
                        intent.apply {
                            putExtra(SymbolOverlayService.IMAGE_URI, imageUri.toString())
                            putExtra(SymbolOverlayService.IMAGE_THRESHOLD, slider)
                        }
                    )
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    DefaultLayout(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    guildMarkManager.selectColor(Color.Unspecified)
                }
            ),
        topBar = {
            BackButtonTitleAppBar(
                title = stringResource(id = R.string.image),
                onClick = popBackStack,
            )
        }
    ) {
        ImagePixels(
            guildMarkManager = guildMarkManager, modifier = Modifier
                .size(itemWidth)
                .border(1.dp, MaterialTheme.colorScheme.onBackground)
        )

        VerticalSpacer(height = 50.dp)

        UsedColorInPixels(
            guildMarkManager = guildMarkManager,
            itemWidth = itemWidth
        )

        VerticalSpacer(height = 30.dp)

        ColorSlider(slider = slider, setSlider = { pos -> slider = pos })

        VerticalSpacer(height = 50.dp)

        HeadlineText(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(),
            text = stringResource(id = R.string.symbol_guildMark_expectation_image)
        )
        VerticalSpacer(height = 20.dp)
        ImagePixels(guildMarkManager = guildMarkManager, modifier = Modifier.size(2.dp))
        VerticalSpacer(height = 50.dp)
        DescriptionSmallText(
            text = stringResource(id = R.string.symbol_guildMark_description),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(),
        )
        VerticalSpacer(height = 50.dp)
    }
}

@Preview(heightDp = 1000)
@Composable
private fun PreviewGuildMarkScreen() = MiscellaneousToolTheme {
    GuildMarkScreen(
        imageUri = "".toUri(),
        popBackStack = {},
        showSnackBar = {}
    )
}