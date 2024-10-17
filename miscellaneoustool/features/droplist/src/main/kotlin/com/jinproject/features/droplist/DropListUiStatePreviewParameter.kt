package com.jinproject.features.droplist

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.jinproject.domain.model.MonsterType
import com.jinproject.features.droplist.state.MapState
import com.jinproject.features.droplist.state.MonsterState
import kotlinx.collections.immutable.persistentListOf

class DropListUiStatePreviewParameter: PreviewParameterProvider<DropListUiState> {
    override val values: Sequence<DropListUiState>
        get() = sequenceOf(
            DropListUiState(
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
                        name = "돌무더기 요새",
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
                monsters = persistentListOf(
                    MonsterState(
                        name = "쿠이",
                        level = 2,
                        genTime = 30,
                        imgName = "kooii",
                        type = MonsterType.Normal("일반"),
                        items = persistentListOf(
                            "주문서D",
                            "무명로브",
                            "네잎클로버",
                            "쿠이인형",
                            "쿠이카드10",
                            "작은선물상자",
                            "하급회복물약",
                        )
                    ),
                    MonsterState(
                        name = "나무동그리",
                        level = 3,
                        genTime = 30,
                        imgName = "donguri",
                        type = MonsterType.Normal("일반"),
                        items = persistentListOf(
                            "주문서D",
                            "무명로브",
                            "네잎클로버",
                            "쿠이인형",
                            "쿠이카드10",
                            "작은선물상자",
                            "하급회복물약",
                        )
                    ),
                    MonsterState(
                        name = "나뭇잎멧돼지",
                        level = 4,
                        genTime = 30,
                        imgName = "pig_leaf",
                        type = MonsterType.Normal("일반"),
                        items = persistentListOf(
                            "주문서D",
                            "무명로브",
                            "네잎클로버",
                            "쿠이인형",
                            "쿠이카드10",
                            "작은선물상자",
                            "하급회복물약",
                        )
                    ),
                    MonsterState(
                        name = "성난잎멧돼지",
                        level = 4,
                        genTime = 30,
                        imgName = "pig_angry",
                        type = MonsterType.Normal("일반"),
                        items = persistentListOf(
                            "주문서D",
                            "무명로브",
                            "네잎클로버",
                            "쿠이인형",
                            "쿠이카드10",
                            "작은선물상자",
                            "하급회복물약",
                        )
                    ),
                    MonsterState(
                        name = "불도저주니어",
                        level = 5,
                        genTime = 30,
                        imgName = "bulldozerjr",
                        type = MonsterType.Normal("네임드"),
                        items = persistentListOf(
                            "주문서D",
                            "무명로브",
                            "네잎클로버",
                            "쿠이인형",
                            "쿠이카드10",
                            "작은선물상자",
                            "하급회복물약",
                        )
                    ),
                    MonsterState(
                        name = "불도저",
                        level = 7,
                        genTime = 30,
                        imgName = "bulldozer",
                        type = MonsterType.Normal("네임드"),
                        items = persistentListOf(
                            "주문서D",
                            "무명로브",
                            "네잎클로버",
                            "쿠이인형",
                            "쿠이카드10",
                            "작은선물상자",
                            "하급회복물약",
                        )
                    ),
                    MonsterState(
                        name = "불도저형님",
                        level = 8,
                        genTime = 30,
                        imgName = "bulldozerbro",
                        type = MonsterType.Normal("네임드"),
                        items = persistentListOf(
                            "주문서D",
                            "무명로브",
                            "네잎클로버",
                            "쿠이인형",
                            "쿠이카드10",
                            "작은선물상자",
                            "하급회복물약",
                        )
                    ),
                ),
                selectedMap = MapState(
                    name = "우디위디숲",
                    imgName = "bulldozerbro"
                ),
            )
        )
}