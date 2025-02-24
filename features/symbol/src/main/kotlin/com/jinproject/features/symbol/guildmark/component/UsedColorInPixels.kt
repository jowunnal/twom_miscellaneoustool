package com.jinproject.features.symbol.guildmark.component

import android.graphics.BitmapFactory
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.component.HorizontalSpacer
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.component.text.HeadlineText
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.features.symbol.guildmark.GuildMarkManager
import com.jinproject.features.symbol.guildmark.rememberGuildMarkManager

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun UsedColorInPixels(
    modifier: Modifier = Modifier,
    guildMarkManager: GuildMarkManager,
    itemWidth: Dp,
    revertButton: @Composable () -> Unit = {},
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HeadlineText(
                modifier = Modifier
                    .wrapContentWidth(),
                text = stringResource(id = R.string.symbol_guildMark_used_color)
            )
            HorizontalSpacer(width = 12.dp)
            revertButton()
        }
        VerticalSpacer(height = 20.dp)
        FlowRow(
            maxItemsInEachRow = 12,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            val list = guildMarkManager.standardColors

            list.forEach { argb ->
                val color = Color(argb)
                Canvas(
                    modifier = Modifier
                        .size(itemWidth)
                        .border(1.dp, MaterialTheme.colorScheme.onBackground)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                guildMarkManager.selectColor(Color(argb))
                            }
                        ),
                ) {
                    drawRect(color)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewUsedColorInPixels() = MiscellaneousToolTheme {
    val context = LocalContext.current
    val bitMapSample = BitmapFactory.decodeResource(context.resources, R.drawable.test)
    val guildMarkManager = rememberGuildMarkManager(bitMap = bitMapSample, slider = 0f)
    UsedColorInPixels(
        guildMarkManager = guildMarkManager,
        itemWidth = 12.dp
    )
}