package com.kkr.juken.core.transport.bluetooth.internal

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Utility for detailed inspection and logging of raw byte packets.
 */
internal class PacketInspector(private val tag: String = "PacketInspector") {

    private val timeFormat = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())

    fun inspect(data: ByteArray, direction: Direction, latencyMs: Long? = null) {
        val timestamp = timeFormat.format(Date())
        val hex = data.joinToString(" ") { "%02X".format(it) }
        val ascii = String(data, Charsets.US_ASCII).replace("\r", "\\r").replace("\n", "\\n")
        val size = data.size
        
        val latencyStr = latencyMs?.let { " LATENCY: ${it}ms" } ?: ""
        
        println("[$timestamp] [$tag] [${direction.name}] SIZE: $size$latencyStr")
        println("  HEX:   $hex")
        println("  ASCII: $ascii")
    }

    enum class Direction { TX, RX }
}
