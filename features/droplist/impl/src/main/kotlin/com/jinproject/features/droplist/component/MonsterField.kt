package com.jinproject.features.droplist.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.request.ImageRequest
import com.jinproject.design_compose.component.CoilBasicImage
import com.jinproject.design_compose.component.SubcomposeAsyncImageWithPreview
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.component.pushRefresh.MTProgressIndicatorRotating
import com.jinproject.design_compose.component.text.DescriptionSmallText
import com.jinproject.design_compose.theme.MiscellaneousToolColor
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.domain.entity.MonsterType
import com.jinproject.features.core.utils.toAssetImageUri
import com.jinproject.features.droplist.DropListUiState
import com.jinproject.features.droplist.DropListUiStatePreviewParameter
import com.jinproject.features.droplist.state.MonsterState

@Composable
internal fun ColumnScope.MonsterField(
    modifier: Modifier = Modifier,
    imgName: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    CoilBasicImage(
        data = toAssetImageUri(prefix = "monster", imgName = imgName),
        modifier = modifier,
    )
    VerticalSpacer(height = 4.dp)
    content()
}

@Composable
internal fun ColumnScope.Monster(
    modifier: Modifier = Modifier,
    monster: MonsterState,
) {
    MonsterField(
        modifier = modifier,
        imgName = monster.imageName,
    ) {
        DescriptionSmallText(
            text = monster.name,
            color = when (monster.type) {
                is MonsterType.Normal -> MaterialTheme.colorScheme.onBackground
                is MonsterType.Named -> MiscellaneousToolColor.blue.color
                is MonsterType.Boss -> MiscellaneousToolColor.orange.color
                is MonsterType.WorldBoss -> MiscellaneousToolColor.deepRed.color
            }
        )
    }
    if (monster.level > 0) {
        DescriptionSmallText(
            text = "Lv.${monster.level}",
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
    if (monster.type !is MonsterType.Normal && monster.genTime != 0)
        DescriptionSmallText(
            text = monster.displayGenTime(),
            modifier = Modifier.padding(vertical = 4.dp)
        )
}

@Composable
@Preview(showBackground = true)
private fun MonsterPreview(
    @PreviewParameter(DropListUiStatePreviewParameter::class)
    dropListUiState: DropListUiState,
) = MiscellaneousToolTheme {
    Column(
        modifier = Modifier
            .width(100.dp)
            .height(300.dp)
            .wrapContentSize()
    ) {
        val monsters = dropListUiState.monstersGroupedByMap.entries.first().value
        Monster(monster = monsters.first())
        Monster(monster = monsters.find { it.type is MonsterType.Boss } ?: monsters.first())
        Monster(monster = monsters.last())
    }
}