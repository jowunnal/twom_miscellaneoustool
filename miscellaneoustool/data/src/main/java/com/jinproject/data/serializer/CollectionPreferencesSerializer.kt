package com.jinproject.data.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.jinproject.data.CollectionPreferences
import java.io.InputStream
import java.io.OutputStream

object CollectionPreferencesSerializer: Serializer<CollectionPreferences> {
    override val defaultValue: CollectionPreferences = CollectionPreferences.getDefaultInstance()
    override suspend fun readFrom(input: InputStream): CollectionPreferences {
        try {
            return CollectionPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: CollectionPreferences, output: OutputStream) = t.writeTo(output)

}