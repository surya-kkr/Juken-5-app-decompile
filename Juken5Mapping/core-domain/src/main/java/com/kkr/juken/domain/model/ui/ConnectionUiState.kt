package com.kkr.juken.domain.model.ui

/**
 * High-level connection states for UI consumption.
 */
sealed interface ConnectionUiState {
    object Idle : ConnectionUiState
    object Connecting : ConnectionUiState
    data class Synchronizing(val progress: Int, val step: String) : ConnectionUiState
    object Connected : ConnectionUiState
    object HeartbeatLost : ConnectionUiState
    data class Error(val message: String) : ConnectionUiState
}
