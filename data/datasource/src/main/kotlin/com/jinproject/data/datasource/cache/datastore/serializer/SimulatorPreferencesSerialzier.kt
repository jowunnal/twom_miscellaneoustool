package com.jinproject.data.datasource.cache.datastore.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.jinproject.data.SimulatorPreferences
import java.io.InputStream
import java.io.OutputStream

internal object SimulatorPreferencesSerializer: Serializer<SimulatorPreferences> {
    override val defaultValue: SimulatorPreferences = SimulatorPreferences.getDefaultInstance()
    override suspend fun readFrom(input: InputStream): SimulatorPreferences {
        try {
            return SimulatorPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: SimulatorPreferences, output: OutputStream) = t.writeTo(output)
}