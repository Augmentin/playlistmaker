package com.example.playlistmaker.search.data.network

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.util.Date

class CustomDateTypeAdapter : TypeAdapter<Date>() {

    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: Date?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(value.time)
        }
    }

    @Throws(IOException::class)
    override fun read(reader: JsonReader): Date? {
        return when (reader.peek()) {
            JsonToken.NULL -> {
                reader.nextNull()
                null
            }

            JsonToken.NUMBER -> {
                val timestamp = reader.nextLong()
                Date(timestamp)
            }

            JsonToken.STRING -> {
                val str = reader.nextString()
                str.toLongOrNull()?.let { Date(it) }
            }

            else -> {
                reader.skipValue()
                null
            }
        }
    }
}