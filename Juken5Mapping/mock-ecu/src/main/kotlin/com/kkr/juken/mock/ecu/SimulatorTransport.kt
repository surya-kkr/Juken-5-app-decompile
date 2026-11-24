package com.kkr.juken.mock.ecu

import com.kkr.juken.core.transport.Transport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Transport implementation that communicates with [EcuSimulator].
 */
class SimulatorTransport(
    private val simulator: EcuSimulator
) : Transport {

    override val incomingPackets: Flow<ByteArray> = simulator.outgoingPackets

    private val _connected = MutableStateFlow(false)

    override suspend fun write(data: ByteArray) {
        if (!_connected.value) throw Exception("Transport not connected")
        val ascii = String(data, Charsets.US_ASCII)
        simulator.processIncoming(ascii)
    }

    override suspend fun connect(address: String) {
        _connected.value = true
    }

    override suspend fun disconnect() {
        _connected.value = false
    }

    override fun isConnected(): Boolean = _connected.value
}
