package com.kkr.juken.domain.model.profile

data class BluetoothDeviceProfile(
    val name: String?,
    val address: String,
    val isPaired: Boolean,
    val rssi: Int = 0
)
