package com.miscellaneoustool.app.data.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.jinproject.twomillustratedbook.TimerPreferences
import java.io.InputStream
import java.io.OutputStream

object TimerPreferencesSerializer: Serializer<TimerPreferences> {
    override val defaultValue: TimerPreferences = TimerPreferences.getDefaultInstance()
    override suspend fun readFrom(input: InputStream): TimerPreferences {
        try {
            return TimerPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: TimerPreferences, output: OutputStream) = t.writeTo(output)
}