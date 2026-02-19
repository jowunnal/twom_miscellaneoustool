package com.jinproject.features.droplist

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.jinproject.domain.entity.MonsterType
import com.jinproject.features.droplist.state.ItemState
import com.jinproject.features.droplist.state.MapState
import com.jinproject.features.droplist.state.MonsterState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

class DropListUiStatePreviewParameter : PreviewParameterProvider<DropListUiState> {
    override val values: Sequence<DropListUiState>
        get() = sequenceOf(
            DropListUiState(
                monstersGroupedByMap = persistentMapOf(
                    MapState(
                        name = "우디위디숲",
                        imgName = "bulldozerbro"
                    ) to monsters
                )
            ).apply {
                updateSearchQuery("멧")
            }
        )

    companion object {
        val maps = persistentListOf(
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
        )

        val items = persistentListOf(
            ItemState("주문서D", "weapon_d"),
            ItemState("무명로브", "frayed_robe"),
            ItemState("네잎클로버", "clover"),
            ItemState("쿠이인형", "kooii_doll"),
            ItemState("쿠이카드10", "kooii_card"),
            ItemState("작은선물상자", ""),
            ItemState("멧돼지꼬리털", ""),
        )

        val monsters = persistentListOf(
            MonsterState(
                name = "쿠이",
                level = 2,
                genTime = 30,
                imageName = "kooii",
                type = MonsterType.Normal("일반"),
                items = items
            ),
            MonsterState(
                name = "나무동그리",
                level = 3,
                genTime = 30,
                imageName = "donguri",
                type = MonsterType.Normal("일반"),
                items = items
            ),
            MonsterState(
                name = "나뭇잎멧돼지",
                level = 4,
                genTime = 30,
                imageName = "pig_leaf",
                type = MonsterType.Normal("일반"),
                items = items
            ),
            MonsterState(
                name = "성난잎멧돼지",
                level = 4,
                genTime = 30,
                imageName = "pig_angry",
                type = MonsterType.Normal("일반"),
                items = items
            ),
            MonsterState(
                name = "불도저주니어",
                level = 5,
                genTime = 300,
                imageName = "bulldozerjr",
                type = MonsterType.Named("네임드"),
                items = items
            ),
            MonsterState(
                name = "불도저",
                level = 7,
                genTime = 1100,
                imageName = "bulldozer",
                type = MonsterType.Boss("보스"),
                items = items
            ),
            MonsterState(
                name = "불도저형님",
                level = 8,
                genTime = 3900,
                imageName = "bulldozerbro",
                type = MonsterType.WorldBoss("대형 보스"),
                items = items
            ),
        )
    }
}