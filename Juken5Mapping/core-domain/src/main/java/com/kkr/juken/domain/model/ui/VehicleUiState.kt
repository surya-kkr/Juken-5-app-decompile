package com.kkr.juken.domain.model.ui

/**
 * Information about the connected vehicle/ECU.
 */
data class VehicleUiState(
    val modelName: String = "Unknown ECU",
    val firmwareVersion: String = "0.0.0",
    val hardwareVersion: String = "0.0.0",
    val serialNumber: String = "N/A",
    val fingerprint: String = "",
    val capabilities: Set<String> = emptySet()
)
