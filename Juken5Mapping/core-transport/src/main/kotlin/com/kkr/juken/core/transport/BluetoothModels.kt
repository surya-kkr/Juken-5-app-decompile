package com.kkr.juken.core.transport

/**
 * Domain representation of a Bluetooth device.
 */
data class BluetoothDevice(
    val name: String?,
    val address: String,
    val bondState: BondState,
    val rssi: Int? = null
) {
    enum class BondState {
        NONE, BONDING, BONDED
    }
}

/**
 * Events related to Bluetooth device discovery.
 */
sealed interface DiscoveryEvent {
    object Started : DiscoveryEvent
    data class DeviceFound(val device: BluetoothDevice) : DiscoveryEvent
    object Finished : DiscoveryEvent
    data class Error(val message: String) : DiscoveryEvent
}
