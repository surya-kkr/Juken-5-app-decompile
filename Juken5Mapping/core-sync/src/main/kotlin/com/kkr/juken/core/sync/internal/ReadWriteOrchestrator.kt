package com.kkr.juken.core.sync.internal

import com.kkr.juken.core.protocol.ProtocolCodec
import com.kkr.juken.core.protocol.model.*
import com.kkr.juken.core.protocol.parser.ParserContext
import com.kkr.juken.core.protocol.parser.ParserResult
import com.kkr.juken.core.transport.ConnectionSession
import com.kkr.juken.domain.model.tuning.MapType
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * Orchestrates granular Read/Write operations for ECU maps.
 */
class ReadWriteOrchestrator(
    private val session: ConnectionSession,
    private val codec: ProtocolCodec,
    private val scope: CoroutineScope
) {
    private val dispatcher = Dispatchers.IO

    suspend fun readBlock(type: MapType, index: Int): List<Int> = withContext(dispatcher) {
        val protoType = mapTypeToProtocol(type)
        val command = EcuCommand.ReadMapBlock(type = protoType, blockIndex = index)
        val response = executeCommand<EcuResponse>(command)
        
        when (response) {
            is EcuResponse.FuelMapResponse -> response.values
            is EcuResponse.BaseMapResponse -> response.values
            is EcuResponse.InjectorMapResponse -> response.values
            is EcuResponse.IgnitionMapResponse -> response.values
            else -> throw Exception("Unexpected response type: ${response::class.simpleName}")
        }
    }

    suspend fun writeBlock(type: MapType, index: Int, data: List<Int>): Unit = withContext(dispatcher) {
        val protoType = mapTypeToProtocol(type)
        val command = EcuCommand.WriteMapBlock(type = protoType, blockIndex = index, data = data)
        executeCommand<EcuResponse.AckResponse>(command)
    }

    private suspend fun <T : EcuResponse> executeCommand(command: EcuCommand): T {
        val ascii = codec.serialize(command)
        session.write(ascii.toByteArray(Charsets.US_ASCII))
        
        return withTimeout(1500) {
            session.incomingPackets
                .map { raw ->
                    val context = ParserContext(
                        protocolVersion = ProtocolVersion.JUKEN_5,
                        transportType = "Transfer",
                        connectionId = session.id.value,
                        timestamp = System.currentTimeMillis()
                    )
                    codec.decode(String(raw, Charsets.US_ASCII), context)
                }
                .filterIsInstance<ParserResult.Success<T>>()
                .map { it.value }
                .first()
        }
    }

    private fun mapTypeToProtocol(type: MapType): com.kkr.juken.core.protocol.model.MapType = when (type) {
        MapType.FUEL -> com.kkr.juken.core.protocol.model.MapType.FUEL
        MapType.BASE_MAP -> com.kkr.juken.core.protocol.model.MapType.BASE_MAP
        MapType.INJECTOR -> com.kkr.juken.core.protocol.model.MapType.INJECTOR
        MapType.IGNITION -> com.kkr.juken.core.protocol.model.MapType.IGNITION
    }
}
