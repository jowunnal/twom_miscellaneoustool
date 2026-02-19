package com.jinproject.features.home

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.jinproject.features.home.model.Equipment
import com.jinproject.features.home.model.ItemCollection
import com.jinproject.features.home.model.MapState
import com.jinproject.features.home.model.MiscellaneousItem
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import java.time.ZonedDateTime

class HomePreviewParameter: PreviewParameterProvider<HomeUiState> {
    override val values: Sequence<HomeUiState>
        get() = sequenceOf(
            HomeUiState(
                maps = persistentListOf(
                    MapState(
                        name = "우디위디숲",
                        imgName = "bulldozerbro"
                    ),
                    MapState(
                        name = "건조한초원",
                        imgName = "bssszsss"
                    ),
                    MapState(
                        name = "빛이들지않는신전",
                        imgName = "ominous_bird"
                    ),
                    MapState(
                        name = "등대던전1층",
                        imgName = "recluse"
                    ),
                    MapState(
                        name = "루나프",
                        imgName = "sephia"
                    ),
                ),
                collections = persistentListOf(
                    ItemCollection(
                        id = 0,
                        stats = persistentMapOf(
                            "pve데미지증가%" to 0.5f,
                            "pvp데미지증가" to 0.5f,
                            "pve데미지감소" to 0.5f,
                        ),
                        items = persistentListOf(
                            MiscellaneousItem(
                                name = "고대코어",
                                count = 180,
                                price = 400000000,
                                imageName = "ancient_core",
                            ),
                            MiscellaneousItem(
                                name = "고대의 봉",
                                count = 25,
                                price = 1000,
                                imageName = "ancient_wand",
                            )
                        ),
                    ),
                    ItemCollection(
                        id = 1,
                        stats = persistentMapOf(
                            "체력" to 3.0f,
                            "마나" to 1.0f,
                        ),
                        items = persistentListOf(
                            MiscellaneousItem(
                                name = "고대코어",
                                count = 300,
                                price = 100,
                                imageName = "ancient_core",
                            ),
                        ),
                    ),
                    ItemCollection(
                        id = 2,
                        stats = persistentMapOf(
                            "pve데미지증가%" to 0.5f,
                            "pvp데미지증가" to 0.5f,
                            "pve데미지감소" to 0.5f,
                        ),
                        items = persistentListOf(
                            MiscellaneousItem(
                                name = "고대코어",
                                count = 900,
                                price = 100,
                                imageName = "ancient_core",
                            ),
                        ),
                    ),
                    ItemCollection(
                        id = 3,
                        stats = persistentMapOf(
                            "pve데미지증가%" to 0.5f,
                            "pvp데미지증가" to 0.5f,
                        ),
                        items = persistentListOf(
                            MiscellaneousItem(
                                name = "고대코어",
                                count = 180,
                                price = 400000000,
                                imageName = "ancient_core",
                            ),
                            Equipment(
                                name = "고대의 봉",
                                count = 1,
                                enchantNumber = 5,
                                price = 10000,
                                imageName = "ancient_wand",
                            )
                        ),
                    ),
                ),
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
