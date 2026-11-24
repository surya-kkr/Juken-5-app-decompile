package com.kkr.juken.core.protocol.parser.internal

import com.kkr.juken.core.protocol.parser.ParsedPacket
import com.kkr.juken.core.protocol.parser.ParserContext

/**
 * Strategy for handling packets with unrecognized headers.
 */
interface UnknownPacketHandler {
    fun handle(packet: ParsedPacket, context: ParserContext)
}

/**
 * Default implementation of [UnknownPacketHandler].
 */
class DefaultUnknownPacketHandler : UnknownPacketHandler {
    override fun handle(packet: ParsedPacket, context: ParserContext) {
        // Implementation for future logging feature
    }
}
