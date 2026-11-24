package com.kkr.juken.domain.model.ui

/**
 * Technical metrics for protocol and transport analysis in the UI.
 */
data class DeveloperUiState(
    val transportType: String = "None",
    val protocolVersion: String = "Unknown",
    val packetRateRx: Float = 0f,
    val packetRateTx: Float = 0f,
    val byteRateRx: Float = 0f,
    val byteRateTx: Float = 0f,
    val lastLatencyMs: Long = 0,
    val avgLatencyMs: Long = 0,
    val maxLatencyMs: Long = 0,
    val totalPacketsRx: Long = 0,
    val totalPacketsTx: Long = 0,
    val reconnectCount: Int = 0,
    val retryCount: Int = 0
)
