package com.kkr.juken.domain.model.state

data class PermissionState(
    val bluetoothGranted: Boolean = false,
    val locationGranted: Boolean = false,
    val notificationsGranted: Boolean = false,
    val storageGranted: Boolean = false,
    val nearbyDevicesGranted: Boolean = false
)
