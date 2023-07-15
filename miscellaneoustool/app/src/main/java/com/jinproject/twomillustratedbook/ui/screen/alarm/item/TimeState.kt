package com.jinproject.twomillustratedbook.ui.screen.alarm.item

import android.os.Parcelable
import androidx.compose.runtime.Stable
import com.jinproject.domain.model.WeekModel
import com.jinproject.twomillustratedbook.utils.day
import com.jinproject.twomillustratedbook.utils.hour
import com.jinproject.twomillustratedbook.utils.minute
import com.jinproject.twomillustratedbook.utils.second
import kotlinx.parcelize.Parcelize
import java.util.Calendar

@Stable
@Parcelize
data class TimeState(
    val day: WeekModel,
    val hour: Int,
    val minutes: Int,
    val seconds: Int
) : Parcelable {

    fun getTime12Hour() = if(hour >= 12) hour - 12 else hour

    fun getMeridiem() = if(hour >= 12) "PM" else "AM"

    companion object {
        fun getInitValue(): TimeState = kotlin.run {
            val cal = Calendar.getInstance()
            TimeState(
                day = WeekModel.findByCode(cal.day),
                hour = cal.hour,
                minutes = cal.minute,
                seconds = cal.second
            )
        }
    }
}
