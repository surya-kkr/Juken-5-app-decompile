package com.kkr.juken.core.transport.internal

import com.kkr.juken.core.protocol.ProtocolCodec
import com.kkr.juken.core.protocol.model.EcuCommand
import com.kkr.juken.core.transport.ConnectionSession
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

/**
 * Manages protocol-level keep-alive by periodically sending heartbeats (1B00).
 * Detects ECU timeouts if the heartbeat is not acknowledged.
 */
internal class HeartbeatManager(
    private val codec: ProtocolCodec,
    private val intervalMs: Long = 5000L,
    private val timeoutMs: Long = 2000L
) {
    private var heartbeatJob: Job? = null

    fun start(session: ConnectionSession, scope: CoroutineScope) {
        heartbeatJob?.cancel()
        heartbeatJob = scope.launch {
            while (isActive) {
                delay(intervalMs)
                try {
                    sendHeartbeat(session)
                } catch (e: Exception) {
                    if (e is CancellationException && e !is TimeoutCancellationException) throw e
                    session.notifyHeartbeatLost()
                }
            }
        }
    }

    fun stop() {
        heartbeatJob?.cancel()
        heartbeatJob = null
    }

    private suspend fun sendHeartbeat(session: ConnectionSession) {
        val heartbeatCmd = EcuCommand.Heartbeat()
        val payload = codec.serialize(heartbeatCmd).toByteArray(Charsets.US_ASCII)
        
        val startTime = System.currentTimeMillis()
        withTimeout(timeoutMs) {
            session.write(payload)
            // Wait for any packet as proof of life
            session.incomingPackets.first()
        }
        val latency = System.currentTimeMillis() - startTime
        session.notifyHeartbeatSuccess(latency)
    }
}
