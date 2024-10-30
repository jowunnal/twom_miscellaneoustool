package com.jinproject.domain.model

/**
 * 월~금 enum class
 */

enum class WeekModel(val displayOnKo: String, val displayOnElse: String) {
    Sun(displayOnKo = "일", displayOnElse = "Sun"),
    Mon(displayOnKo = "월", displayOnElse = "Mon"),
    Tues(displayOnKo = "화", displayOnElse = "Tues"),
    Wed(displayOnKo = "수", displayOnElse = "Wed"),
    Thurs(displayOnKo = "목", displayOnElse = "Thur"),
    Fri(displayOnKo = "금", displayOnElse = "Fri"),
    Sat(displayOnKo = "토", displayOnElse = "Sat");

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

    fun toDayOfWeek() = when (this) {
        Mon -> 1
        Tues -> 2
        Wed -> 3
        Thurs -> 4
        Fri -> 5
        Sat -> 6
        Sun -> 7
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