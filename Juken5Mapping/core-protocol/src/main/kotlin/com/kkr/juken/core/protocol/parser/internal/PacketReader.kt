package com.kkr.juken.core.protocol.parser.internal

import com.kkr.juken.core.protocol.model.ProtocolError
import com.kkr.juken.core.protocol.parser.ParsedPacket
import com.kkr.juken.core.protocol.parser.ProtocolHeader

/**
 * Level 1 Parser: Handles raw ASCII manipulation and structural validation.
 */
class PacketReader {

    /**
     * Converts a raw ASCII string from the wire into a [ParsedPacket].
     * Performs structural validation: empty checks, header extraction (Hex), and field splitting.
     *
     * @throws ProtocolError.MalformedResponse if the string structure is invalid.
     */
    fun read(ascii: String): ParsedPacket {
        val trimmed = ascii.trim()
        if (trimmed.isEmpty()) {
            throw ProtocolError.MalformedResponse(ascii, "Received empty packet")
        }

        val fields = trimmed.split(";")
        val headerString = fields.first()
        
        // Protocol headers are hexadecimal strings (e.g., 1607, A601, 1A00)
        val headerValue = headerString.toIntOrNull(16)
            ?: throw ProtocolError.MalformedResponse(ascii, "Invalid hex header: $headerString")

        return ParsedPacket(
            header = ProtocolHeader(headerValue),
            fields = fields.drop(1),
            raw = trimmed
        )
    }
}
