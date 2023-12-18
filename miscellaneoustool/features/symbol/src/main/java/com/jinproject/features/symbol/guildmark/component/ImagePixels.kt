package com.jinproject.features.symbol.guildmark.component

import android.graphics.BitmapFactory
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.features.symbol.R
import com.jinproject.features.symbol.guildmark.GuildMarkManager
import com.jinproject.features.symbol.guildmark.rememberGuildMarkManager

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ImagePixels(
    modifier: Modifier = Modifier,
    guildMarkManager: GuildMarkManager,
) {
    FlowRow(
        maxItemsInEachRow = 12,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        guildMarkManager.filteredCellColors.forEach { argb ->
            val color = Color(argb)
            if (color == Color.White) {
                Spacer(
                    modifier = modifier,
                )
            } else
                Canvas(
                    modifier = modifier,
                ) {
                    drawRect(color)
                }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewImagePixels() = MiscellaneousToolTheme {
    val context = LocalContext.current
    val bitMapSample = BitmapFactory.decodeResource(context.resources, R.drawable.test)
    val guildMarkManager = rememberGuildMarkManager(bitMap = bitMapSample, slider = 0f)
    ImagePixels(
        modifier = Modifier
            .size(12.dp)
            .border(1.dp, MaterialTheme.colorScheme.onBackground),
        guildMarkManager = guildMarkManager,
    )
}