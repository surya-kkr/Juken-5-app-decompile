package com.kkr.juken.core.protocol.parser

/**
 * Represents the numeric identifier at the start of every ECU packet.
 * For example: 9606, 9602, etc.
 */
@JvmInline
value class ProtocolHeader(val value: Int)
