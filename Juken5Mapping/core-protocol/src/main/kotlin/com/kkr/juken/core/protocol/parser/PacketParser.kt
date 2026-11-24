package com.kkr.juken.core.protocol.parser

/**
 * Interface for individual response parsers that extract raw protocol fields into [ProtocolNode]s.
 */
interface PacketParser<out T : ProtocolNode> {
    /**
     * Parses a [ParsedPacket] into a [ProtocolNode].
     * Performs protocol-level validation (Level 2).
     */
    fun parse(packet: ParsedPacket, context: ParserContext): ParserResult<T>
}
