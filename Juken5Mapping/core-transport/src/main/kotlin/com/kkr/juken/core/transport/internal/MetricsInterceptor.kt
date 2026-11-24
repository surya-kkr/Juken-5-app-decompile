package com.kkr.juken.core.transport.internal

import com.kkr.juken.core.transport.ConnectionId
import com.kkr.juken.core.transport.ConnectionMetrics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Internal component for collecting and calculating connection performance metrics.
 */
internal class MetricsInterceptor(initialId: ConnectionId) {

    private val startTime = System.currentTimeMillis()
    private val _metrics = MutableStateFlow(ConnectionMetrics(connectionId = initialId))
    val metrics: StateFlow<ConnectionMetrics> = _metrics.asStateFlow()

    private var totalLatency: Long = 0
    private var latencyCount: Long = 0

    fun recordSent(bytes: Int) {
        _metrics.update { current ->
            current.copy(
                bytesSent = current.bytesSent + bytes,
                packetsSent = current.packetsSent + 1,
                lastPacketTime = System.currentTimeMillis(),
                uptimeMs = System.currentTimeMillis() - startTime
            )
        }
    }

    fun recordReceived(bytes: Int) {
        _metrics.update { current ->
            current.copy(
                bytesReceived = current.bytesReceived + bytes,
                packetsReceived = current.packetsReceived + 1,
                lastPacketTime = System.currentTimeMillis(),
                uptimeMs = System.currentTimeMillis() - startTime
            )
        }
    }

    fun recordHeartbeat(latencyMs: Long) {
        totalLatency += latencyMs
        latencyCount++
        
        _metrics.update { current ->
            current.copy(
                heartbeatLatencyMs = latencyMs,
                lastHeartbeatTime = System.currentTimeMillis(),
                averageLatencyMs = totalLatency / latencyCount,
                maxLatencyMs = maxOf(current.maxLatencyMs, latencyMs),
                uptimeMs = System.currentTimeMillis() - startTime
            )
        }
    }

    fun recordReconnect() {
        _metrics.update { it.copy(reconnectCount = it.reconnectCount + 1) }
    }

    fun recordRetry() {
        _metrics.update { it.copy(retryCount = it.retryCount + 1) }
    }
}
