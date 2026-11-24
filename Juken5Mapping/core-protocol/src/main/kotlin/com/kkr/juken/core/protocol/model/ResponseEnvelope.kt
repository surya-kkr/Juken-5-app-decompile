package com.kkr.juken.core.protocol.model

/**
 * A container that wraps an [EcuResponse] with execution and transport metadata.
 * This decouples the protocol payload from the context of its reception.
 *
 * @param T The specific type of the [EcuResponse].
 * @property payload The parsed domain data from the ECU.
 * @property metadata Metadata regarding the reception of this response.
 */
data class ResponseEnvelope<out T : EcuResponse>(
    val payload: T,
    val metadata: ResponseMetadata
)

/**
 * Metadata associated with a received ECU response.
 *
 * @property timestamp The system time when the response was received.
 * @property latencyMs The time elapsed between command transmission and response reception.
 * @property retryCount The number of retries attempted before success.
 * @property transportType The transport used (e.g., "Bluetooth", "WiFi").
 * @property protocolVersion The protocol version detected at the time of reception.
 * @property connectionId Unique identifier for the connection session.
 */
data class ResponseMetadata(
    val timestamp: Long,
    val latencyMs: Long,
    val retryCount: Int,
    val transportType: String,
    val protocolVersion: ProtocolVersion,
    val connectionId: String
)
