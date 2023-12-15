package com.jinproject.features.symbol.guildmark

import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jinproject.design_compose.component.DefaultAppBar
import com.jinproject.design_compose.component.DefaultLayout
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.features.core.base.item.SnackBarMessage

@Composable
fun GuildMarkScreen(
    guildMarkViewModel: GuildMarkViewModel = hiltViewModel(),
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GuildMarkScreen(
    context: Context = LocalContext.current,
    imageUri: Uri,
    popBackStack: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
) {
    val bitMap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(context.contentResolver, imageUri)
        ) { decoder, info, source ->
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            decoder.isMutableRequired = true
        }
    } else
        MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)

    val guildMarkManager = rememberGuildMarkManager(bitMap = bitMap)

    DefaultLayout(
        modifier = Modifier
            .fillMaxWidth()
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
        FlowRow(
            maxItemsInEachRow = 12,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            guildMarkManager.filteredCellColors.forEach { argbs ->
                argbs.forEach { argb ->
                    val color = Color(argb)
                    if (color == Color.White) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .border(1.dp, MaterialTheme.colorScheme.onBackground),
                        )
                    } else
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .drawBehind {
                                    drawRect(color)
                                }
                                .border(1.dp, MaterialTheme.colorScheme.onBackground),
                        )
                    /*
                    color = if ((argb.shr(16) * 0xFF) > 136 || argb.shr(8) * 0xFF > 136 || argb * 0xFF > 136) Color.Black else Color.White,
                     */
                    Log.d("test", "bb")
                }
            }
            Log.d("test", "dd")
        }

        VerticalSpacer(height = 16.dp)

        FlowRow(
            maxItemsInEachRow = 12,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            val list = guildMarkManager.standardColors

            list.forEach { argb ->
                val color = Color(argb)
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .drawBehind {
                            drawRect(color)
                        }
                        .border(1.dp, MaterialTheme.colorScheme.onBackground)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                guildMarkManager.selectColor(Color(argb))
                                Log.d("test", "selected Color : $argb")
                            }
                        ),
                )
                //Log.d("test","cc")
            }

            Log.d("test", "colors: ${list.toString()}, size: ${list.size}")
        }

        VerticalSpacer(height = 16.dp)

        FlowRow(
            maxItemsInEachRow = 12,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            guildMarkManager.filteredCellColors.forEach { argbs ->
                argbs.forEach { argb ->
                    val color = Color(argb)
                    Box(
                        modifier = Modifier
                            .size(2.dp)
                            .drawBehind {
                                drawRect(color)
                            },
                    )
                    Log.d("test", "bb")
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewGuildMarkScreen() = MiscellaneousToolTheme {
    GuildMarkScreen(
        imageUri = Uri.EMPTY,
        popBackStack = {},
        showSnackBar = {}
    )
}