package com.kkr.juken.core.transport

import com.kkr.juken.core.transport.internal.ConnectionSessionImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Manages the lifecycle of a connection to the ECU.
 * Handles state transitions, reconnection, and coordinates metrics/heartbeats.
 */
interface ConnectionSession {
    val id: ConnectionId
    val state: StateFlow<ConnectionState>
    val metrics: StateFlow<ConnectionMetrics>
    val events: SharedFlow<SessionEvent>
    val incomingPackets: Flow<ByteArray>

    suspend fun connect(address: String)
    suspend fun disconnect()
    suspend fun write(data: ByteArray)

    /** Internal use for HeartbeatManager */
    fun notifyHeartbeatLost()
    fun notifyHeartbeatSuccess(latencyMs: Long)

    companion object {
        /**
         * Factory method to create a production connection session.
         */
        fun create(
            transport: Transport,
            scope: CoroutineScope,
            dispatcher: CoroutineDispatcher = Dispatchers.IO
        ): ConnectionSession = ConnectionSessionImpl(
            transport = transport,
            scope = scope,
            dispatcher = dispatcher
        )
    }
}
