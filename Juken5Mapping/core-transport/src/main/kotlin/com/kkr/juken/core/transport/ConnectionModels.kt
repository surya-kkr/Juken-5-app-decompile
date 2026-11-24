package com.kkr.juken.core.transport

import java.util.UUID

/**
 * Unique identifier for a specific connection session.
 */
@JvmInline
value class ConnectionId(val value: String = UUID.randomUUID().toString())

/**
 * Operational states of a [ConnectionSession].
 */
enum class ConnectionState {
    IDLE,
    CONNECTING,
    CONNECTED,
    HEARTBEAT_LOST,
    RECONNECTING,
    DISCONNECTING,
    DISCONNECTED,
    FAILED
}

/**
 * Real-time metrics for monitoring connection health and performance.
 */
data class ConnectionMetrics(
    val connectionId: ConnectionId,
    val uptimeMs: Long = 0,
    val lastPacketTime: Long = 0,
    val lastHeartbeatTime: Long = 0,
    val heartbeatLatencyMs: Long = 0,
    val averageLatencyMs: Long = 0,
    val maxLatencyMs: Long = 0,
    val packetsSent: Long = 0,
    val packetsReceived: Long = 0,
    val bytesSent: Long = 0,
    val bytesReceived: Long = 0,
    val retryCount: Int = 0,
    val reconnectCount: Int = 0
)

/**
 * Observable events emitted by a [ConnectionSession].
 */
sealed interface SessionEvent {
    data class Connected(val id: ConnectionId) : SessionEvent
    data class Disconnected(val id: ConnectionId) : SessionEvent
    data class ReconnectStarted(val attempt: Int) : SessionEvent
    object ReconnectSucceeded : SessionEvent
    data class ReconnectFailed(val error: Throwable) : SessionEvent
    object HeartbeatTimeout : SessionEvent
    data class TransportError(val error: Throwable) : SessionEvent
    data class PacketSent(val bytes: Int) : SessionEvent
    data class PacketReceived(val bytes: Int) : SessionEvent
}
