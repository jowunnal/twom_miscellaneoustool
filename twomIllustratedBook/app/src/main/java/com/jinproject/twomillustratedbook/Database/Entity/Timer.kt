package com.jinproject.twomillustratedbook.Database.Entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Timer(var timer_name:String, var timer_day:Int, var timer_hour:Int, var timer_min:Int, var timer_sec:Int,
                 @PrimaryKey var timer_id:Int, var timer_statue:Int, var timer_ota:Int) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(timer_name)
        parcel.writeInt(timer_day)
        parcel.writeInt(timer_hour)
        parcel.writeInt(timer_min)
        parcel.writeInt(timer_sec)
        parcel.writeInt(timer_id)
        parcel.writeInt(timer_statue)
        parcel.writeInt(timer_ota)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Timer> {
        override fun createFromParcel(parcel: Parcel): Timer {
            return Timer(parcel)
        }

        override fun newArray(size: Int): Array<Timer?> {
            return arrayOfNulls(size)
        }
    }
}