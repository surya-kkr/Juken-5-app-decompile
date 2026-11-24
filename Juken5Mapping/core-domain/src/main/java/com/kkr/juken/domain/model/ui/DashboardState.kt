package com.kkr.juken.domain.model.ui

/**
 * Aggregated state for the Dashboard screen.
 */
data class DashboardState(
    val connection: ConnectionUiState = ConnectionUiState.Idle,
    val vehicle: VehicleUiState = VehicleUiState(),
    val live: LiveState = LiveState(),
    val isDeveloperModeEnabled: Boolean = false
)
