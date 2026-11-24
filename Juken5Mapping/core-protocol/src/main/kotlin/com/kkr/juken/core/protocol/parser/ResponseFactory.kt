package com.kkr.juken.core.protocol.parser

import com.kkr.juken.core.protocol.model.EcuResponse

/**
 * Interface for creating domain-level [EcuResponse] from internal [ProtocolNode].
 */
interface ResponseFactory {
    fun createResponse(node: ProtocolNode, context: ParserContext): EcuResponse
}

/**
 * Default implementation of the response factory.
 */
class ResponseFactoryImpl : ResponseFactory {
    override fun createResponse(node: ProtocolNode, context: ParserContext): EcuResponse {
        return when (node) {
            is ProtocolNode.IdentityNode -> EcuResponse.EcuIdentityResponse(
                com.kkr.juken.core.protocol.model.EcuIdentity(
                    protocolVersion = context.protocolVersion,
                    ecuName = node.modelName,
                    hardwareVersion = com.kkr.juken.core.protocol.model.HardwareVersion(0, 0, node.hardwareVer),
                    firmwareVersion = com.kkr.juken.core.protocol.model.FirmwareVersion(0, 0, 0, node.firmwareVer),
                    serialNumber = node.serialNumber
                )
            )
            is ProtocolNode.GlobalVariablesNode -> EcuResponse.GlobalVariablesResponse(node.variables)
            is ProtocolNode.FuelMapNode -> EcuResponse.FuelMapResponse(node.blockIndex, node.values)
            is ProtocolNode.BaseMapNode -> EcuResponse.BaseMapResponse(node.blockIndex, node.values)
            is ProtocolNode.InjectorMapNode -> EcuResponse.InjectorMapResponse(node.blockIndex, node.values)
            is ProtocolNode.IgnitionMapNode -> EcuResponse.IgnitionMapResponse(node.blockIndex, node.values)
            is ProtocolNode.HistoryNode -> EcuResponse.HistoryResponse(
                node.blockIndex, 
                node.rawEntries.map { raw -> 
                    val parts = raw.split(",")
                    com.kkr.juken.core.protocol.model.HistoryEntry(
                        timestamp = System.currentTimeMillis(),
                        row = parts.getOrNull(0)?.toIntOrNull() ?: 0,
                        col = parts.getOrNull(1)?.toIntOrNull() ?: 0,
                        afr = parts.getOrNull(2)?.toDoubleOrNull() ?: 0.0
                    )
                }
            )
            is ProtocolNode.PatternNode -> EcuResponse.PatternResponse(node.blockIndex, node.values)
            is ProtocolNode.DiagnosticNode -> EcuResponse.DiagnosticResponse(
                node.statusFlags.toIntOrNull(16) ?: 0,
                node.dtcCount,
                node.rawCodes
            )
            ProtocolNode.AckNode -> EcuResponse.AckResponse
            else -> EcuResponse.NackResponse("Unsupported protocol node")
        }
    }
}
