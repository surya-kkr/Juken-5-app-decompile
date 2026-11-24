package com.kkr.juken.core.protocol.parser

import com.kkr.juken.core.protocol.model.EcuResponse
import com.kkr.juken.core.protocol.model.ProtocolError
import com.kkr.juken.core.protocol.model.ProtocolVersion
import com.kkr.juken.core.protocol.parser.internal.*

/**
 * Registry and dispatcher for ECU response parsers.
 */
class ParserRegistry constructor(
    private val factory: ResponseFactory = ResponseFactoryImpl(),
    private val unknownPacketHandler: UnknownPacketHandler = DefaultUnknownPacketHandler()
) {
    private val versionParsers: Map<ProtocolVersion, Map<ProtocolHeader, PacketParser<*>>> = mapOf(
        ProtocolVersion.JUKEN_5 to createJuken5Parsers(),
        ProtocolVersion.JUKEN_5_PRO to createJuken5Parsers()
    )

    fun parse(packet: ParsedPacket, context: ParserContext): ParserResult<EcuResponse> {
        val parsers = versionParsers[context.protocolVersion]
            ?: return ParserResult.Failure(ProtocolError.MalformedResponse(packet.raw, "Unsupported protocol version"))

        val parser = parsers[packet.header]
        if (parser == null) {
            unknownPacketHandler.handle(packet, context)
            return ParserResult.Failure(ProtocolError.MalformedResponse(packet.raw, "Unknown packet header: ${packet.header.value.toString(16).uppercase()}"))
        }

        val nodeResult = parser.parse(packet, context)
        
        return when (nodeResult) {
            is ParserResult.Success -> ParserResult.Success(factory.createResponse(nodeResult.value, context))
            is ParserResult.Failure -> nodeResult
        }
    }

    private fun createJuken5Parsers(): Map<ProtocolHeader, PacketParser<*>> = mapOf(
        ProtocolHeader(0x9606) to GlobalVariablesPacketParser(factory),
        ProtocolHeader(0x9616) to IdentityPacketParser(factory),
        ProtocolHeader(0x9601) to BaseMapPacketParser(factory),
        ProtocolHeader(0x9602) to FuelPacketParser(factory),
        ProtocolHeader(0x9603) to InjectorMapPacketParser(factory),
        ProtocolHeader(0x9605) to IgnitionPacketParser(factory),
        ProtocolHeader(0x9610) to HistoryPacketParser(factory),
        ProtocolHeader(0x9614) to PatternPacketParser(factory),
        ProtocolHeader(0xA601) to CalibrationPacketParser(factory),
        ProtocolHeader(0xA602) to DiagnosticPacketParser(factory),
        ProtocolHeader(0xB800) to MaintenancePacketParser(factory),
        ProtocolHeader(0x1A00) to AckPacketParser(factory)
    )
}
