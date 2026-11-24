package com.kkr.juken.core.transport

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * In-memory implementation of [Transport] for automated testing.
 */
class MockTransport : Transport {
    private val _incoming = MutableSharedFlow<ByteArray>(extraBufferCapacity = 16)
    override val incomingPackets: SharedFlow<ByteArray> = _incoming.asSharedFlow()

    private var connected = false
    var shouldFailConnect = false
    var lastWritten: ByteArray? = null

    override suspend fun connect(address: String) {
        delay(10)
        if (shouldFailConnect) throw Exception("Mock Connection Failed")
        connected = true
    }

    override suspend fun disconnect() {
        connected = false
    }

    override suspend fun write(data: ByteArray) {
        if (!connected) throw Exception("Not connected")
        lastWritten = data
    }

    override fun isConnected(): Boolean = connected

    suspend fun emitIncoming(data: ByteArray) {
        _incoming.emit(data)
    }
}
