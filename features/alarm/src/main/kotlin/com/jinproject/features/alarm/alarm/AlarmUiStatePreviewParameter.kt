package com.jinproject.features.alarm.alarm

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.jinproject.features.alarm.alarm.item.MonsterState
import com.jinproject.features.alarm.alarm.item.MonsterType
import com.jinproject.features.alarm.alarm.item.TimerState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.ZonedDateTime

class AlarmUiStatePreviewParameter : PreviewParameterProvider<AlarmUiState> {
    private val timerList: ImmutableList<TimerState> = persistentListOf(
        TimerState(
            id = 1,
            bossName = "불도저",
            dateTime = ZonedDateTime.now()
                .withHour(14)
                .withMinute(22)
                .withSecond(25)
        ),
        TimerState(
            id = 2,
            bossName = "은둔자",
            dateTime = ZonedDateTime.now()
                .withHour(4)
                .withMinute(44)
                .withSecond(44)
        ),
        TimerState(
            id = 3,
            bossName = "바슬라프",
            dateTime = ZonedDateTime.now()
                .withHour(21)
                .withMinute(26)
                .withSecond(55)
        )
    )

    private val monsterList: ImmutableList<MonsterState> = persistentListOf(
        MonsterState(
            name = "불도저",
            type = MonsterType.Named,
            imageName = "bulldozer"
        ),
        MonsterState(
            name = "은둔자",
            type = MonsterType.Boss,
            imageName = "recluse"
        ),
        MonsterState(
            name = "바슬라프",
            type = MonsterType.WorldBoss,
            imageName = "barslaf"
        ),
    )

    private val overlaidBossList: ImmutableList<String> = persistentListOf("불도저", "은둔자", "바슬라프")

    private val frequentlyUsedBossList: ImmutableList<String> =
        persistentListOf("불도저", "은둔자", "바슬라프")

    override val values: Sequence<AlarmUiState> = sequenceOf(
        AlarmUiState(
            timerList = timerList,
            monsterList = monsterList,
            frequentlyUsedBossList = frequentlyUsedBossList,
            overlaidBossList = overlaidBossList,
        )
    )
}
