package com.kkr.juken.core.transport

/**
 * Represents the current state of Bluetooth permissions and hardware.
 */
data class BluetoothPermissionState(
    val isBluetoothEnabled: Boolean = false,
    val hasAdapter: Boolean = false,
    val bluetoothScanGranted: Boolean = false,
    val bluetoothConnectGranted: Boolean = false,
    val locationGranted: Boolean = false,
    val isPermanentlyDenied: Boolean = false,
    val shouldShowRationale: Boolean = false
) {
    /**
     * Returns true if all required permissions for the current Android version are granted.
     */
    val allGranted: Boolean
        get() = (bluetoothScanGranted && bluetoothConnectGranted) || (locationGranted && !isPermanentlyDenied)
        // Note: Actual logic depends on API level, handled by the manager.
}
