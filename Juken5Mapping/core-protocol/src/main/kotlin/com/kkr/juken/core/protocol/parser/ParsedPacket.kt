package com.kkr.juken.core.protocol.parser

/**
 * Intermediate representation of a validated and segmented ECU packet.
 *
 * @property header The extracted [ProtocolHeader].
 * @property fields The list of payload segments (strings) between delimiters.
 * @property raw Original ASCII string (excluding CRLF) for debugging/logging.
 */
data class ParsedPacket(
    val header: ProtocolHeader,
    val fields: List<String>,
    val raw: String
)
