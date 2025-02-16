package com.jinproject.features.symbol.guildmark

import com.jinproject.design_compose.component.layout.DownLoadedUiState
import com.jinproject.design_compose.component.layout.DownloadableUiStatePreviewParameter

internal class GuildMarkPreviewParameter :
    DownloadableUiStatePreviewParameter<String>() {
    override val data: DownLoadedUiState<String>
        get() = GuildMarkUiState("")
}