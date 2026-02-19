package com.jinproject.features.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.component.CoilBasicImage
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.component.text.DescriptionSmallText
import com.jinproject.features.core.utils.toAssetImageUri
import com.jinproject.features.home.model.MapState

@Composable
internal fun HomeMapCard(
    mapState: MapState,
    itemWidth: Dp,
    modifier: Modifier = Modifier,
    onClickItem: (MapState) -> Unit,
) {
    Column(
        modifier = modifier
            .shadow(6.dp, RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(20.dp))
            .clickable {
                onClickItem(mapState)
            }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CoilBasicImage(
            data = toAssetImageUri(prefix = "monster", imgName = mapState.imgName),
            modifier = Modifier
                .height(65.dp)
                .width(itemWidth),
        )
        VerticalSpacer(height = 4.dp)
        DescriptionSmallText(text = mapState.name)
    }
}
