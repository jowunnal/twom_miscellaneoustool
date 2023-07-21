package com.jinproject.features.alarm.item

import android.os.Parcelable
import androidx.compose.runtime.Stable
import com.jinproject.core.util.day
import com.jinproject.core.util.hour
import com.jinproject.core.util.minute
import com.jinproject.core.util.second
import com.jinproject.domain.model.WeekModel
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
