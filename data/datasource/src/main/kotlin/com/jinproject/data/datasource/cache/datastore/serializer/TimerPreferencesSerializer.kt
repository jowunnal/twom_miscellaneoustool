package com.jinproject.data.datasource.cache.datastore.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.jinproject.core.TimerPreferences
import java.io.InputStream
import java.io.OutputStream

internal object TimerPreferencesSerializer: Serializer<TimerPreferences> {
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