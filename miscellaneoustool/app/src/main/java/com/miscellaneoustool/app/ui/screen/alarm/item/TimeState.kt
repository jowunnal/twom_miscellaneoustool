package com.miscellaneoustool.app.ui.screen.alarm.item

import android.os.Parcelable
import androidx.compose.runtime.Stable
import com.miscellaneoustool.app.domain.model.WeekModel
import com.miscellaneoustool.app.utils.hour
import com.miscellaneoustool.app.utils.minute
import com.miscellaneoustool.app.utils.second
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
