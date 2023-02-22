package com.jinproject.twomillustratedbook.ui.screen.alarm.item

import android.os.Parcelable
import androidx.compose.runtime.Stable
import com.jinproject.twomillustratedbook.domain.model.WeekModel
import com.jinproject.twomillustratedbook.utils.hour
import com.jinproject.twomillustratedbook.utils.minute
import com.jinproject.twomillustratedbook.utils.second
import kotlinx.parcelize.Parcelize
import java.util.*

@Stable
@Parcelize
data class TimeState(
    val day: WeekModel,
    val hour: Int,
    val minutes: Int,
    val seconds: Int
) : Parcelable {
    companion object {
        fun getInitValue(): TimeState {
            val cal = Calendar.getInstance()
            return TimeState(
                day = WeekModel.Mon,
                hour = cal.hour,
                minutes = cal.minute,
                seconds = cal.second
            )
        }
    }
}
