package com.kkr.juken.core.transport

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Interface for discovering Bluetooth devices.
 */
interface BluetoothScanner {
    /**
     * Observable stream of discovery events.
     */
    val discoveryEvents: Flow<DiscoveryEvent>

    /**
     * Set of unique devices found during current or previous scan.
     */
    val foundDevices: StateFlow<Set<BluetoothDevice>>

    /**
     * Set of devices already paired with the system.
     */
    val pairedDevices: StateFlow<Set<BluetoothDevice>>

    /**
     * True if a scan is currently in progress.
     */
    val isScanning: StateFlow<Boolean>

    /**
     * Starts discovering nearby Bluetooth devices.
     */
    fun startScan()

    /**
     * Stops the discovery process.
     */
    fun stopScan()
}

/**
 * Interface for handling Bluetooth-related permissions and hardware state.
 */
interface BluetoothPermissionManager {
    /**
     * Observable stream of current permission and hardware state.
     */
    val state: StateFlow<BluetoothPermissionState>

    /**
     * Returns true if all required Bluetooth permissions are granted.
     */
    fun hasPermissions(): Boolean

    /**
     * Returns true if Bluetooth hardware is enabled.
     */
    fun isBluetoothEnabled(): Boolean

    /**
     * Returns true if a Bluetooth adapter is available on the device.
     */
    fun hasAdapter(): Boolean

    /**
     * Returns true if the app should show a rationale for the requested permissions.
     */
    fun shouldShowRationale(activity: Any): Boolean

    /**
     * Opens the system settings for this application.
     */
    fun openAppSettings()

    /**
     * Checks current permissions and hardware state, updating the [state] flow.
     */
    fun checkPermissions()
}

/**
 * Interface for monitoring Bluetooth hardware state changes.
 */
interface BluetoothStateMonitor {
    /**
     * Starts monitoring for state changes (e.g., Bluetooth ON/OFF, Airplane mode).
     */
    fun startMonitoring()

    /**
     * Stops monitoring and releases resources.
     */
    fun stopMonitoring()
}
