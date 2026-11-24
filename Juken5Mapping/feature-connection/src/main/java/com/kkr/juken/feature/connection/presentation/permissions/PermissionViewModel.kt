package com.kkr.juken.feature.connection.presentation.permissions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kkr.juken.core.transport.BluetoothPermissionManager
import com.kkr.juken.domain.model.state.PermissionState
import com.kkr.juken.domain.model.state.SessionStatus
import com.kkr.juken.domain.state.ApplicationStateManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(
    private val appState: ApplicationStateManager,
    private val permissionManager: BluetoothPermissionManager
) : ViewModel() {

    val state = appState.permissions.state
    val bluetoothPermissionState = permissionManager.state

    private val _effects = MutableSharedFlow<PermissionEffect>()
    val effects = _effects.asSharedFlow()

    init {
        permissionManager.checkPermissions()
    }

    fun onPermissionsResult(granted: Map<String, Boolean>) {
        permissionManager.checkPermissions()
        val allGranted = permissionManager.hasPermissions()
        
        appState.permissions.updatePermissions(
            PermissionState(
                bluetoothGranted = allGranted,
                locationGranted = granted[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false,
                nearbyDevicesGranted = allGranted
            )
        )

        if (allGranted) {
            if (permissionManager.isBluetoothEnabled()) {
                viewModelScope.launch {
                    _effects.emit(PermissionEffect.NavigateToScanner)
                }
            } else {
                // Handle Bluetooth disabled
            }
        }
    }

    fun onOpenSettings() {
        permissionManager.openAppSettings()
    }
}

sealed interface PermissionEffect {
    object NavigateToScanner : PermissionEffect
}
