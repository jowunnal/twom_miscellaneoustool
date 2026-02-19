package com.jinproject.features.droplist.state

import com.jinproject.domain.entity.MonsterType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.util.Locale

data class MonsterState(
    override val name: String,
    val level: Int,
    val genTime: Int,
    override val imageName: String,
    val type: MonsterType,
    val items: ImmutableList<ItemState>
) : Searchable {
    override val imagePrefix: String = "monster"
    companion object {
        fun getInitValue() = MonsterState(
            name = "",
            level = 0,
            genTime = 0,
            imageName = "",
            type = MonsterType.Normal("일반"),
            items = persistentListOf()
        )
    }

    fun displayGenTime(): String {
        val days = genTime / (86400)
        val hours = (genTime % 86400) / 3600
        val minutes = (genTime % 3600) / 60
        val seconds = genTime % 60

        val dayPostFix = if (Locale.getDefault().language == "ko") "일" else "days"
        val hourPostFix = if (Locale.getDefault().language == "ko") "시간" else "hours"
        val minutePostFix = if (Locale.getDefault().language == "ko") "분" else "minutes"
        val secondPostFix = if (Locale.getDefault().language == "ko") "초" else "seconds"
        val isValid =
            { value: Int, postfix: String -> if (value > 0) "$value$postfix" + (if (postfix != secondPostFix) " " else "") else "" }

        return "${isValid(days, dayPostFix)}${isValid(hours, hourPostFix)}${
            isValid(
                minutes,
                minutePostFix
            )
        }${isValid(seconds, secondPostFix)}"
    }
}
