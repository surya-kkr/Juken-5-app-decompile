package com.kkr.juken.feature.connection.presentation.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kkr.juken.core.transport.BluetoothDevice
import com.kkr.juken.core.transport.BluetoothScanner
import com.kkr.juken.domain.repository.EcuRepository
import com.kkr.juken.domain.state.ApplicationStateManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val scanner: BluetoothScanner,
    private val appState: ApplicationStateManager,
    private val repository: EcuRepository
) : ViewModel() {

    private val simulatorDevice = BluetoothDevice(
        name = "Simulator ECU",
        address = "00:00:00:00:00:00",
        bondState = BluetoothDevice.BondState.BONDED
    )

    val uiState: StateFlow<ScannerUiState> = combine(
        scanner.pairedDevices,
        scanner.foundDevices,
        scanner.isScanning,
        appState.settings.state
    ) { paired, found, scanning, settings ->
        val pairedList = if (settings.isDeveloperModeEnabled) {
            paired + simulatorDevice
        } else {
            paired
        }
        ScannerUiState(
            pairedDevices = pairedList.toList(),
            availableDevices = found.toList(),
            isScanning = scanning
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ScannerUiState())

    fun startScan() = scanner.startScan()
    fun stopScan() = scanner.stopScan()

    fun startDemoMode() {
        onDeviceSelected(simulatorDevice)
    }

    fun onDeviceSelected(device: BluetoothDevice) {
        viewModelScope.launch {
            // Set transport based on selection
            if (device.address == "00:00:00:00:00:00") {
                appState.settings.setTransport(com.kkr.juken.domain.model.state.TransportType.SIMULATOR)
            } else {
                appState.settings.setTransport(com.kkr.juken.domain.model.state.TransportType.BLUETOOTH)
            }
            // Now that transport is set, connect. connect() will trigger SyncEngine
            repository.connect(device.address)
        }
    }
}

data class ScannerUiState(
    val pairedDevices: List<BluetoothDevice> = emptyList(),
    val availableDevices: List<BluetoothDevice> = emptyList(),
    val isScanning: Boolean = false
)
