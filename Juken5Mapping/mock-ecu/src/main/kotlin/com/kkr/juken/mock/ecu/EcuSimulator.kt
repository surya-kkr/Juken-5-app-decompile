package com.kkr.juken.mock.ecu

import com.kkr.juken.mock.ecu.internal.FaultInjector
import com.kkr.juken.mock.ecu.internal.SimulatorCodec
import com.kkr.juken.mock.ecu.internal.SimulatorRequest
import com.kkr.juken.mock.ecu.model.EngineState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Stateful software simulation of a Juken 5 ECU.
 */
class EcuSimulator(
    val engineState: EngineState = EngineState()
) {
    internal val faultInjector: FaultInjector = FaultInjector()
    private val codec = SimulatorCodec()

    // Using replay = 1 to ensure that any response emitted during processIncoming is captured 
    // by the transport even if it's subscribed slightly after.
    private val _outgoingPackets = MutableSharedFlow<ByteArray>(replay = 1, extraBufferCapacity = 64)
    val outgoingPackets: SharedFlow<ByteArray> = _outgoingPackets.asSharedFlow()

    /**
     * Entry point for incoming raw ASCII commands from the transport.
     */
    suspend fun processIncoming(raw: String) {
        val request = codec.decodeCommand(raw)
        val responseBytes = when (request) {
            is SimulatorRequest.Handshake -> handleHandshake()
            is SimulatorRequest.FetchGlobalVariables -> handleGlobalVars()
            is SimulatorRequest.ReadMap -> handleReadMap(request)
            is SimulatorRequest.ReadHistory -> handleReadHistory(request)
            is SimulatorRequest.ReadPattern -> handleReadPattern(request)
            SimulatorRequest.Heartbeat -> handleHeartbeat()
            is SimulatorRequest.Unknown -> null
        }

        responseBytes?.let { data ->
            val faulted = faultInjector.inject(data)
            faulted?.forEach { _outgoingPackets.emit(it) }
        }
    }

    private fun handleHandshake(): ByteArray {
        return codec.encodeResponse(
            "9616",
            engineState.modelName,
            engineState.hardwareVer,
            engineState.firmwareVer,
            engineState.serialNumber
        )
    }

    private fun handleGlobalVars(): ByteArray {
        val vars = engineState.variables.entries.joinToString(";") { "${it.key}=${it.value}" }
        return codec.encodeResponse("9606", vars)
    }

    private fun handleReadMap(req: SimulatorRequest.ReadMap): ByteArray {
        val map = when (req.type) {
            "FUEL" -> engineState.fuelMap
            "BASE" -> engineState.baseMap
            "INJECTOR" -> engineState.injectorMap
            "IGNITION" -> engineState.ignitionMap
            else -> return codec.encodeResponse("NACK", "Unknown Map Type")
        }
        val header = when (req.type) {
            "FUEL" -> "9602"
            "BASE" -> "9601"
            "INJECTOR" -> "9603"
            "IGNITION" -> "9605"
            else -> "9600"
        }
        val blockData = map.getBlock(req.block)
        return codec.encodeResponse(header, req.block, *blockData.toTypedArray())
    }

    private fun handleReadHistory(req: SimulatorRequest.ReadHistory): ByteArray {
        val blockSize = 10 
        val start = req.block * blockSize
        val end = minOf(start + blockSize, engineState.historyBuffer.size)
        val entries = if (start < engineState.historyBuffer.size) {
            engineState.historyBuffer.subList(start, end)
        } else emptyList<String>()
        
        return codec.encodeResponse("9610", req.block, *entries.toTypedArray())
    }

    private fun handleReadPattern(req: SimulatorRequest.ReadPattern): ByteArray {
        return codec.encodeResponse("9614", req.blockIndex, 0, 0, 0)
    }

    private fun handleHeartbeat(): ByteArray {
        return codec.encodeResponse("1A00")
    }
    
    fun setFaultProfile(loss: Double = 0.0, corruption: Double = 0.0, latency: Long = 0) {
        faultInjector.packetLossChance = loss
        faultInjector.corruptionChance = corruption
        faultInjector.minLatencyMs = latency
        faultInjector.maxLatencyMs = latency
    }
}
