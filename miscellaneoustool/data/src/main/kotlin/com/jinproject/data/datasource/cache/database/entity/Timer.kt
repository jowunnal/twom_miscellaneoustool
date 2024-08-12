package com.jinproject.data.datasource.cache.database.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Monster::class,
        parentColumns = arrayOf("monsName"),
        childColumns = arrayOf("timerMonsName")
    )], indices = [Index("timerMonsName")]
)
data class Timer(
    @PrimaryKey(autoGenerate = true) var timerId: Int,
    var day: Int,
    var hour: Int,
    var min: Int,
    var sec: Int,
    var ota: Int,
    var timerMonsName: String,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(timerMonsName)
        parcel.writeInt(day)
        parcel.writeInt(hour)
        parcel.writeInt(min)
        parcel.writeInt(sec)
        parcel.writeInt(timerId)
        parcel.writeInt(ota)
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