package com.kkr.juken.core.data.db

import androidx.room.TypeConverter
import java.nio.ByteBuffer
import java.nio.ByteOrder

internal class Converters {
    @TypeConverter
    fun fromIntArray(value: IntArray): ByteArray {
        val buffer = ByteBuffer.allocate(value.size * 4).order(ByteOrder.LITTLE_ENDIAN)
        value.forEach { buffer.putInt(it) }
        return buffer.array()
    }

    @TypeConverter
    fun toIntArray(value: ByteArray): IntArray {
        val buffer = ByteBuffer.wrap(value).order(ByteOrder.LITTLE_ENDIAN)
        val result = IntArray(value.size / 4)
        for (i in result.indices) {
            result[i] = buffer.int
        }
        return result
    }
}
