package com.kkr.juken.domain.model.ui

/**
 * High-level state of the real-time telemetry session.
 */
sealed interface LiveSessionUiState {
    object Idle : LiveSessionUiState
    object Connecting : LiveSessionUiState
    object Streaming : LiveSessionUiState
    object Paused : LiveSessionUiState
    data class Error(val message: String) : LiveSessionUiState
}

/**
 * Detailed real-time engine state including grid positioning.
 */
data class RealtimeEngineState(
    val rpm: Int = 0,
    val tps: Double = 0.0,
    val battery: Double = 0.0,
    val temperature: Int = 0,
    val manifoldPressure: Double = 0.0,
    
    // Grid Positioning
    val activeCellIndex: Int? = null,
    val interpolationIndices: List<Int> = emptyList(),
    val movementTrail: List<Int> = emptyList() // Last 20 indices
)
