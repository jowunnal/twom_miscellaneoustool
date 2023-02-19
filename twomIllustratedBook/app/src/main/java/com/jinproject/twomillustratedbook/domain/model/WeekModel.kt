package com.jinproject.twomillustratedbook.domain.model

import androidx.compose.runtime.Stable

/**
 * 월~금 enum class
 */

enum class WeekModel(val displayName: String) {
    Sun(displayName = "일"),
    Mon(displayName = "월"),
    Tues(displayName = "화"),
    Wed(displayName = "수"),
    Thurs(displayName = "목"),
    Fri(displayName = "금"),
    Sat(displayName = "토");

    fun getCodeByWeek() =
        when (this) {
            Sun -> 1
            Mon -> 2
            Tues -> 3
            Wed -> 4
            Thurs -> 5
            Fri -> 6
            Sat -> 7
        }

    companion object {
        fun findByCode(code: Int) =
            when (code) {
                1 -> Sun
                2 -> Mon
                3 -> Tues
                4 -> Wed
                5 -> Thurs
                6 -> Fri
                7 -> Sat
                else -> {
                    throw IllegalStateException("IllegalStateException has occurred : $code")
                }
            }
    }
}