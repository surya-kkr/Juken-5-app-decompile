package com.kkr.juken.core.protocol.serializer.internal

/**
 * Level 1 Serializer: Handles field joining and ASCII formatting.
 */
class PacketWriter {
    /**
     * Joins fields with the standard protocol separator and appends CRLF.
     */
    fun write(header: String, vararg fields: Any): String {
        val payload = if (fields.isEmpty()) header else "$header;${fields.joinToString(";")}"
        return "$payload\r\n"
    }
}
