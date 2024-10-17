package com.jinproject.features.simulator.component

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.request.ImageRequest
import com.jinproject.design_compose.component.SubcomposeAsyncImageWithPreview
import com.jinproject.design_compose.component.pushRefresh.MTProgressIndicatorRotating
import com.jinproject.design_compose.theme.MiscellaneousToolColor
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_compose.tu
import com.jinproject.features.core.utils.toAssetImageUri
import com.jinproject.features.simulator.EquipmentListPreviewParameters
import com.jinproject.features.simulator.model.Empty
import com.jinproject.features.simulator.model.Equipment
import com.jinproject.features.simulator.model.Item
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun ItemSpace(
    modifier: Modifier = Modifier,
    density: Density = LocalDensity.current,
    size: Dp,
    item: Item,
) {
    val enchantNumberTextSize = with(density) {
        14.tu.toPx()
    }

    val textPaintStroke = remember {
        Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            style = android.graphics.Paint.Style.STROKE
            textSize = enchantNumberTextSize
            color = MiscellaneousToolColor.itemTextColor.color.toArgb()
            strokeWidth = 8f
            strokeMiter = 6f
            strokeJoin = android.graphics.Paint.Join.ROUND
        }
    }

    val textPaint = remember {
        Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            style = android.graphics.Paint.Style.FILL
            textSize = enchantNumberTextSize
            color = Color.WHITE
        }
    }

    SubcomposeAsyncImageWithPreview(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(toAssetImageUri(prefix = "item", imgName = item.imgName))
            .build(),
        contentDescription = "Image",
        loading = {
            MTProgressIndicatorRotating()
        },
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .size(size)
            .shadow(6.dp, RoundedCornerShape(10.dp))
            .background(MiscellaneousToolColor.itemSpaceColor.color, RoundedCornerShape(10.dp))
            .border(1.dp, MaterialTheme.colorScheme.scrim, RoundedCornerShape(10.dp))
            .padding(8.dp)
            .drawWithContent {
                drawContent()

                if (item is Equipment && item !is Empty) {
                    val offset = Offset(x = this.size.center.x, y = enchantNumberTextSize / 2)
                    val text = "+${item.enchantNumber}"

                    drawIntoCanvas {
                        it.nativeCanvas.drawText(
                            text,
                            offset.x,
                            offset.y,
                            textPaintStroke
                        )
                        it.nativeCanvas.drawText(
                            text,
                            offset.x,
                            offset.y,
                            textPaint
                        )
                    }
                }
            }
            .then(modifier),
        placeHolderPreview = com.jinproject.design_ui.R.drawable.test,
    )
}


@Preview(widthDp = 320, heightDp = 320)
@Composable
private fun PreviewItemSpace(
    @PreviewParameter(EquipmentListPreviewParameters::class)
    equipments: ImmutableList<Equipment>,
) = MiscellaneousToolTheme {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
    ) {
        ItemSpace(size = 64.dp, item = equipments.last())
    }
}
