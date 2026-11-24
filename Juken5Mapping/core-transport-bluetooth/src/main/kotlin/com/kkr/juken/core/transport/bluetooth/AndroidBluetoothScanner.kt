package com.kkr.juken.core.transport.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice as NativeDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.kkr.juken.core.transport.BluetoothDevice
import com.kkr.juken.core.transport.BluetoothScanner
import com.kkr.juken.core.transport.DiscoveryEvent
import kotlinx.coroutines.flow.*

/**
 * Android implementation of [BluetoothScanner] using BluetoothAdapter discovery.
 */
@SuppressLint("MissingPermission")
class AndroidBluetoothScanner(
    private val context: Context,
    private val adapter: BluetoothAdapter?,
    private val permissionManager: com.kkr.juken.core.transport.BluetoothPermissionManager,
    private val sessionStateManager: com.kkr.juken.domain.state.SessionStateManager
) : BluetoothScanner {

    private val _discoveryEvents = MutableSharedFlow<DiscoveryEvent>(extraBufferCapacity = 16)
    override val discoveryEvents: Flow<DiscoveryEvent> = _discoveryEvents.asSharedFlow()

    private val _foundDevices = MutableStateFlow<Set<BluetoothDevice>>(emptySet())
    override val foundDevices: StateFlow<Set<BluetoothDevice>> = _foundDevices.asStateFlow()

    private val _pairedDevices = MutableStateFlow<Set<BluetoothDevice>>(emptySet())
    override val pairedDevices: StateFlow<Set<BluetoothDevice>> = _pairedDevices.asStateFlow()

    private val _isScanning = MutableStateFlow(false)
    override val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                NativeDevice.ACTION_FOUND -> {
                    val device: NativeDevice? = intent.getParcelableExtra(NativeDevice.EXTRA_DEVICE)
                    val rssi: Int = intent.getShortExtra(NativeDevice.EXTRA_RSSI, Short.MIN_VALUE).toInt()
                    device?.let { native ->
                        // Only add if NOT already in paired devices
                        if (native.bondState != NativeDevice.BOND_BONDED) {
                            val mapped = native.toDomain(rssi = if (rssi == Short.MIN_VALUE.toInt()) null else rssi)
                            _foundDevices.update { set -> set + mapped }
                            _discoveryEvents.tryEmit(DiscoveryEvent.DeviceFound(mapped))
                        }
                    }
                }
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    _isScanning.value = true
                    _discoveryEvents.tryEmit(DiscoveryEvent.Started)
                    sessionStateManager.updateStatus(com.kkr.juken.domain.model.state.SessionStatus.Scanning)
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    _isScanning.value = false
                    _discoveryEvents.tryEmit(DiscoveryEvent.Finished)
                    sessionStateManager.updateStatus(com.kkr.juken.domain.model.state.SessionStatus.Ready)
                }
            }
        }
    }

    override fun startScan() {
        if (adapter == null) {
            _discoveryEvents.tryEmit(DiscoveryEvent.Error("Bluetooth adapter not available"))
            return
        }

        if (!permissionManager.hasPermissions()) {
            _discoveryEvents.tryEmit(DiscoveryEvent.Error("Missing required Bluetooth permissions"))
            return
        }

        if (!permissionManager.isBluetoothEnabled()) {
            _discoveryEvents.tryEmit(DiscoveryEvent.Error("Bluetooth is disabled"))
            return
        }

        // Update paired devices
        _pairedDevices.value = adapter.bondedDevices.map { it.toDomain() }.toSet()
        
        _foundDevices.value = emptySet()
        val filter = IntentFilter().apply {
            addAction(NativeDevice.ACTION_FOUND)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        }
        
        if (adapter.isDiscovering) {
            adapter.cancelDiscovery()
        }
        
        context.registerReceiver(receiver, filter)
        val success = adapter.startDiscovery()
        if (!success) {
            _discoveryEvents.tryEmit(DiscoveryEvent.Error("Failed to start discovery"))
        }
    }

    override fun stopScan() {
        adapter?.cancelDiscovery()
        try {
            context.unregisterReceiver(receiver)
        } catch (e: Exception) {
            // Ignore
        }
        _isScanning.value = false
    }

    private fun NativeDevice.toDomain(rssi: Int? = null): BluetoothDevice = BluetoothDevice(
        name = name,
        address = address,
        bondState = mapBondState(bondState),
        rssi = rssi
    )

    private fun mapBondState(state: Int): BluetoothDevice.BondState = when (state) {
        NativeDevice.BOND_BONDED -> BluetoothDevice.BondState.BONDED
        NativeDevice.BOND_BONDING -> BluetoothDevice.BondState.BONDING
        else -> BluetoothDevice.BondState.NONE
    }
}
