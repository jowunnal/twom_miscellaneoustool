package com.jinproject.features.home

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.jinproject.features.collection.CollectionUiStatePreviewParameter
import com.jinproject.features.droplist.DropListUiStatePreviewParameter
import kotlinx.collections.immutable.persistentListOf
import java.time.ZonedDateTime

class HomePreviewParameter: PreviewParameterProvider<HomeUiState> {
    override val values: Sequence<HomeUiState>
        get() = sequenceOf(
            HomeUiState(
                maps = DropListUiStatePreviewParameter.maps,
                collections = CollectionUiStatePreviewParameter.items,
                bossTimer = persistentListOf(
                    BossTimer(
                        name = "불도저",
                        time = ZonedDateTime.now(),
                    ),
                    BossTimer(
                        name = "딜린",
                        time = ZonedDateTime.now(),
                    ),
                    BossTimer(
                        name = "우크파나",
                        time = ZonedDateTime.now(),
                    ),
                )
            )
        )
}