package com.jinproject.features.symbol.guildmark

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.request.ImageRequest
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.component.bar.BackButtonTitleAppBar
import com.jinproject.design_compose.component.button.clickableAvoidingDuplication
import com.jinproject.design_compose.component.layout.DownloadableLayout
import com.jinproject.design_compose.component.layout.DownloadableUiState
import com.jinproject.design_compose.component.text.DescriptionSmallText
import com.jinproject.design_compose.component.text.HeadlineText
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.core.utils.checkAuthorityDrawOverlays
import com.jinproject.features.symbol.getBitmapFromContentUri
import com.jinproject.features.symbol.guildmark.component.ColorSlider
import com.jinproject.features.symbol.guildmark.component.ImagePixels
import com.jinproject.features.symbol.guildmark.component.UsedColorInPixels

@Composable
internal fun GuildMarkScreen(
    guildMarkViewModel: GuildMarkViewModel = hiltViewModel(),
    context: Context = LocalContext.current,
    popBackStack: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
) {
    val uiState by guildMarkViewModel.uiState.collectAsStateWithLifecycle()

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

    var slider by remember {
        mutableFloatStateOf(0f)
    }

    LifecycleEventEffect(event = Lifecycle.Event.ON_STOP) {
        val intent = Intent(
            context,
            SymbolOverlayService::class.java
        )

        if (checkAuthorityDrawOverlays(context = context) {
                permissionLauncher.launch(it)
            }
        )
            if (uiState is GuildMarkUiState)
                context.startForegroundService(
                    intent.apply {
                        putExtra(SymbolOverlayService.IMAGE_URI, (uiState as GuildMarkUiState).data)
                        putExtra(SymbolOverlayService.IMAGE_THRESHOLD, slider)
                    }
                )
    }

    GuildMarkScreen(
        uiState = uiState,
        slider = slider,
        changeSlider = { pos -> slider = pos },
        popBackStack = popBackStack,
        showSnackBar = showSnackBar,
    )
}

@Composable
private fun GuildMarkScreen(
    configuration: Configuration = LocalConfiguration.current,
    context: Context = LocalContext.current,
    uiState: DownloadableUiState,
    slider: Float,
    changeSlider: (Float) -> Unit,
    popBackStack: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
) {
    val itemWidth = (configuration.screenWidthDp / 12).dp

    DownloadableLayout(
        topBar = {
            BackButtonTitleAppBar(
                title = stringResource(id = R.string.image),
                onBackClick = popBackStack,
            )
        },
        downloadableUiState = uiState,
        verticalScrollable = true,
    ) { state ->
        val uri = (state as GuildMarkUiState).data

        val bitMap by produceState<Bitmap>(
            initialValue = GuildMarkManager.getInitBitmap(),
            key1 = uri,
        ) {
            value = if (uri.startsWith("http")) {
                val requester = ImageRequest.Builder(context).data(uri).build()
                (ImageLoader(context).newBuilder()
                    .allowHardware(false)
                    .build()
                    .execute(requester).drawable as BitmapDrawable).bitmap
            } else
                getBitmapFromContentUri(
                    context = context,
                    imageUri = uri,
                )
        }

        val guildMarkManager = rememberGuildMarkManager(bitMap = bitMap, slider = slider)

        ImagePixels(
            guildMarkManager = guildMarkManager,
            modifier = Modifier
                .size(itemWidth)
                .border(1.dp, MaterialTheme.colorScheme.onBackground)
        )

        VerticalSpacer(height = 50.dp)

        UsedColorInPixels(
            guildMarkManager = guildMarkManager,
            itemWidth = itemWidth,
            revertButton = {
                Image(
                    painter = painterResource(id = R.drawable.ic_recycle),
                    contentDescription = "revert",
                    modifier = Modifier
                        .shadow(5.dp, RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.background, RoundedCornerShape(20.dp))
                        .clickableAvoidingDuplication {
                            guildMarkManager.selectColor(Color.Unspecified)
                        },
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                )
            }
        )

        VerticalSpacer(height = 30.dp)

        ColorSlider(slider = slider, setSlider = changeSlider)

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
private fun PreviewGuildMarkScreen(
    @PreviewParameter(GuildMarkPreviewParameter::class)
    uiState: DownloadableUiState,
) = MiscellaneousToolTheme {
    GuildMarkScreen(
        uiState = uiState,
        slider = 0f,
        changeSlider = {},
        popBackStack = {},
        showSnackBar = {}
    )
}