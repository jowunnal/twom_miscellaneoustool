package com.jinproject.features.symbol.guildmark

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jinproject.design_compose.component.DefaultAppBar
import com.jinproject.design_compose.component.DefaultLayout
import com.jinproject.design_compose.component.HeadlineText
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.symbol.R
import com.jinproject.features.symbol.guildmark.component.ColorSlider
import com.jinproject.features.symbol.guildmark.component.ImagePixels
import com.jinproject.features.symbol.guildmark.component.UsedColorInPixels

@Composable
fun GuildMarkScreen(
    context: Context = LocalContext.current,
    guildMarkViewModel: GuildMarkViewModel = hiltViewModel(),
    popBackStack: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
) {
    val imageUri by guildMarkViewModel.imageDetailState.collectAsStateWithLifecycle()

    val bitMap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(context.contentResolver, imageUri)
        ) { decoder, info, source ->
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            decoder.isMutableRequired = true
            if (info.size.width > 500 || info.size.height > 500)
                decoder.setTargetSampleSize(2)
        }
    } else
        MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)

    GuildMarkScreen(
        bitMap = bitMap,
        popBackStack = popBackStack,
        showSnackBar = showSnackBar,
    )
}

@Composable
private fun GuildMarkScreen(
    configuration: Configuration = LocalConfiguration.current,
    bitMap: Bitmap,
    popBackStack: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
) {

    var slider by remember {
        mutableFloatStateOf(0f)
    }

    val guildMarkManager = rememberGuildMarkManager(bitMap = bitMap, slider = slider)
    val itemWidth = (configuration.screenWidthDp / 12).dp

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
            DefaultAppBar(
                title = "사진",
                onBackClick = popBackStack
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
            text = "예상되는 이미지"
        )
        VerticalSpacer(height = 20.dp)
        ImagePixels(guildMarkManager = guildMarkManager, modifier = Modifier.size(2.dp))
    }
}

@Preview(heightDp = 1000)
@Composable
private fun PreviewGuildMarkScreen() = MiscellaneousToolTheme {
    val context = LocalContext.current
    val bitMapSample = BitmapFactory.decodeResource(context.resources, R.drawable.test)
    GuildMarkScreen(
        bitMap = bitMapSample,
        popBackStack = {},
        showSnackBar = {}
    )
}