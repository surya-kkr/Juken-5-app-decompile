package com.kkr.juken.feature.validation

import com.kkr.juken.core.protocol.ProtocolCodec
import com.kkr.juken.core.protocol.model.EcuCommand
import com.kkr.juken.core.protocol.model.MapType
import com.kkr.juken.core.transport.ConnectionSession
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

/**
 * Runner for repeated protocol operations to verify stability.
 */
class StressTestRunner(
    private val session: ConnectionSession,
    private val codec: ProtocolCodec
) {

    /**
     * Executes repeated handshake cycles.
     */
    suspend fun runHandshakeStress(cycles: Int = 10): List<Long> {
        val latencies = mutableListOf<Long>()
        repeat(cycles) {
            val start = System.currentTimeMillis()
            executeCommand(EcuCommand.Handshake())
            latencies.add(System.currentTimeMillis() - start)
            delay(100)
        }
        return latencies
    }

    /**
     * Executes repeated fuel map block reads.
     */
    suspend fun runFuelReadStress(cycles: Int = 20): List<Long> {
        val latencies = mutableListOf<Long>()
        repeat(cycles) {
            val start = System.currentTimeMillis()
            executeCommand(EcuCommand.ReadMapBlock(type = MapType.FUEL, blockIndex = it % 21))
            latencies.add(System.currentTimeMillis() - start)
            delay(50)
        }
        return latencies
    }

    private suspend fun executeCommand(command: EcuCommand) {
        val payload = codec.serialize(command).toByteArray(Charsets.US_ASCII)
        session.write(payload)
        // Wait for response
        session.incomingPackets.first()
    }
}
