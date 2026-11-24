package com.kkr.juken.core.protocol.parser

import com.kkr.juken.core.protocol.model.ProtocolVersion

/**
 * Metadata required by parsers to correctly interpret packet fields.
 *
 * @property protocolVersion The version of the protocol currently in use.
 * @property transportType The physical transport layer used (e.g. "Bluetooth", "WiFi").
 * @property connectionId Unique ID for the current session.
 * @property timestamp Epoch milliseconds when the packet was received.
 */
data class ParserContext(
    val protocolVersion: ProtocolVersion,
    val transportType: String,
    val connectionId: String,
    val timestamp: Long
)
