package com.kkr.juken.domain.model.state

import java.util.UUID

/**
 * Granular connection status for the Bluetooth lifecycle.
 */
sealed interface SessionStatus {
    object Idle : SessionStatus
    object CheckingPermissions : SessionStatus
    object PermissionRequired : SessionStatus
    object PermissionDenied : SessionStatus
    object PermissionPermanentlyDenied : SessionStatus
    object NoBluetoothAdapter : SessionStatus
    object BluetoothDisabled : SessionStatus
    object Ready : SessionStatus
    object Scanning : SessionStatus
    object Connecting : SessionStatus
    object Connected : SessionStatus
    object Disconnected : SessionStatus
    data class Failed(val reason: String) : SessionStatus
}

data class SessionState(
    val sessionId: String = UUID.randomUUID().toString(),
    val status: SessionStatus = SessionStatus.Idle,
    val connectedDeviceAddress: String? = null,
    val startTimeMs: Long = 0,
    val connectionDurationMs: Long = 0,
    val reconnectCount: Int = 0,
    val isHeartbeatActive: Boolean = false,
    val isAppInForeground: Boolean = true,
    val lastPacketTimestamp: Long = 0,
    val transportType: TransportType? = null
)
