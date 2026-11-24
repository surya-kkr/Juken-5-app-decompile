package com.kkr.juken.core.protocol.parser.internal

import com.kkr.juken.core.protocol.model.ProtocolError
import com.kkr.juken.core.protocol.parser.*

/**
 * Verification Status: VERIFIED
 * Parses Global Variables packet (9606).
 * Format: 9606;VAR1=VAL1;VAR2=VAR2...
 */
internal class GlobalVariablesPacketParser(
    private val factory: ResponseFactory
) : PacketParser<ProtocolNode.GlobalVariablesNode> {
    override fun parse(packet: ParsedPacket, context: ParserContext): ParserResult<ProtocolNode.GlobalVariablesNode> {
        val vars = packet.fields.associate { field ->
            val parts = field.split("=")
            parts.first() to (parts.getOrNull(1) ?: "")
        }
        val node = ProtocolNode.GlobalVariablesNode(vars)
        return ParserResult.Success(node)
    }
}

/**
 * Verification Status: VERIFIED
 * Parses ECU Identity packet (9616).
 * Format: 9616;ModelName;HardwareVer;FirmwareVer;[SerialNumber]
 */
internal class IdentityPacketParser(
    private val factory: ResponseFactory
) : PacketParser<ProtocolNode.IdentityNode> {
    override fun parse(packet: ParsedPacket, context: ParserContext): ParserResult<ProtocolNode.IdentityNode> {
        if (packet.fields.size < 3) return ParserResult.Failure(ProtocolError.MalformedResponse(packet.raw, "Identity fields missing"))
        
        val node = ProtocolNode.IdentityNode(
            modelName = packet.fields[0],
            hardwareVer = packet.fields[1],
            firmwareVer = packet.fields[2],
            serialNumber = packet.fields.getOrNull(3)
        )
        return ParserResult.Success(node)
    }
}

/**
 * Base for Map Block parsers (9601, 9602, 9603, 9605).
 */
internal abstract class AbstractMapPacketParser<N : ProtocolNode>(
    protected val factory: ResponseFactory
) : PacketParser<N> {
    protected fun extractMapData(packet: ParsedPacket): Pair<Int, List<Int>>? {
        val blockIndex = packet.fields.firstOrNull()?.toIntOrNull() ?: return null
        val values = packet.fields.drop(1).mapNotNull { it.toIntOrNull() }
        return blockIndex to values
    }
}

internal class BaseMapPacketParser(f: ResponseFactory) : AbstractMapPacketParser<ProtocolNode.BaseMapNode>(f) {
    override fun parse(packet: ParsedPacket, context: ParserContext): ParserResult<ProtocolNode.BaseMapNode> {
        val data = extractMapData(packet) ?: return ParserResult.Failure(ProtocolError.MalformedResponse(packet.raw, "Invalid map fields"))
        return ParserResult.Success(ProtocolNode.BaseMapNode(data.first, data.second))
    }
}

internal class FuelPacketParser(f: ResponseFactory) : AbstractMapPacketParser<ProtocolNode.FuelMapNode>(f) {
    override fun parse(packet: ParsedPacket, context: ParserContext): ParserResult<ProtocolNode.FuelMapNode> {
        val data = extractMapData(packet) ?: return ParserResult.Failure(ProtocolError.MalformedResponse(packet.raw, "Invalid map fields"))
        return ParserResult.Success(ProtocolNode.FuelMapNode(data.first, data.second))
    }
}

internal class InjectorMapPacketParser(f: ResponseFactory) : AbstractMapPacketParser<ProtocolNode.InjectorMapNode>(f) {
    override fun parse(packet: ParsedPacket, context: ParserContext): ParserResult<ProtocolNode.InjectorMapNode> {
        val data = extractMapData(packet) ?: return ParserResult.Failure(ProtocolError.MalformedResponse(packet.raw, "Invalid map fields"))
        return ParserResult.Success(ProtocolNode.InjectorMapNode(data.first, data.second))
    }
}

internal class IgnitionPacketParser(f: ResponseFactory) : AbstractMapPacketParser<ProtocolNode.IgnitionMapNode>(f) {
    override fun parse(packet: ParsedPacket, context: ParserContext): ParserResult<ProtocolNode.IgnitionMapNode> {
        val data = extractMapData(packet) ?: return ParserResult.Failure(ProtocolError.MalformedResponse(packet.raw, "Invalid map fields"))
        return ParserResult.Success(ProtocolNode.IgnitionMapNode(data.first, data.second))
    }
}

/**
 * Verification Status: VERIFIED
 * Parses History packet (9610).
 * Format: 9610;BlockIndex;Entry1;Entry2... where Entry is R,T,A
 */
internal class HistoryPacketParser(
    private val factory: ResponseFactory
) : PacketParser<ProtocolNode.HistoryNode> {
    override fun parse(packet: ParsedPacket, context: ParserContext): ParserResult<ProtocolNode.HistoryNode> {
        val blockIndex = packet.fields.firstOrNull()?.toIntOrNull() ?: return ParserResult.Failure(ProtocolError.MalformedResponse(packet.raw, "Invalid history index"))
        val node = ProtocolNode.HistoryNode(blockIndex, packet.fields.drop(1))
        return ParserResult.Success(node)
    }
}

/**
 * Verification Status: VERIFIED
 * Parses Auto Mapping Pattern packet (9614).
 */
internal class PatternPacketParser(
    private val factory: ResponseFactory
) : PacketParser<ProtocolNode.PatternNode> {
    override fun parse(packet: ParsedPacket, context: ParserContext): ParserResult<ProtocolNode.PatternNode> {
        val blockIndex = packet.fields.firstOrNull()?.toIntOrNull() ?: return ParserResult.Failure(ProtocolError.MalformedResponse(packet.raw, "Invalid pattern index"))
        val values = packet.fields.drop(1).mapNotNull { it.toIntOrNull() }
        return ParserResult.Success(ProtocolNode.PatternNode(blockIndex, values))
    }
}

internal class CalibrationPacketParser(f: ResponseFactory) : PacketParser<ProtocolNode.CalibrationNode> {
    override fun parse(packet: ParsedPacket, context: ParserContext): ParserResult<ProtocolNode.CalibrationNode> {
        return ParserResult.Success(ProtocolNode.CalibrationNode(packet.fields.firstOrNull() ?: ""))
    }
}

internal class DiagnosticPacketParser(f: ResponseFactory) : PacketParser<ProtocolNode.DiagnosticNode> {
    override fun parse(packet: ParsedPacket, context: ParserContext): ParserResult<ProtocolNode.DiagnosticNode> {
        if (packet.fields.size < 2) return ParserResult.Failure(ProtocolError.MalformedResponse(packet.raw, "Diagnostic fields missing"))
        val node = ProtocolNode.DiagnosticNode(
            statusFlags = packet.fields[0],
            dtcCount = packet.fields[1].toIntOrNull() ?: 0,
            rawCodes = packet.fields.drop(2)
        )
        return ParserResult.Success(node)
    }
}

internal class MaintenancePacketParser(f: ResponseFactory) : PacketParser<ProtocolNode.MaintenanceNode> {
    override fun parse(packet: ParsedPacket, context: ParserContext): ParserResult<ProtocolNode.MaintenanceNode> {
        return ParserResult.Success(ProtocolNode.MaintenanceNode(packet.fields.firstOrNull() ?: ""))
    }
}

internal class AckPacketParser(f: ResponseFactory) : PacketParser<ProtocolNode.AckNode> {
    override fun parse(packet: ParsedPacket, context: ParserContext): ParserResult<ProtocolNode.AckNode> {
        return ParserResult.Success(ProtocolNode.AckNode)
    }
}
