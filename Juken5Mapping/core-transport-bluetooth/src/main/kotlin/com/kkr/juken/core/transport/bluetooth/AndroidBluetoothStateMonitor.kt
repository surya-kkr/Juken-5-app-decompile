package com.kkr.juken.core.transport.bluetooth

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.kkr.juken.core.transport.BluetoothPermissionManager
import com.kkr.juken.core.transport.BluetoothStateMonitor
import com.kkr.juken.domain.state.SessionStateManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidBluetoothStateMonitor @Inject constructor(
    private val context: Context,
    private val permissionManager: BluetoothPermissionManager,
    private val sessionStateManager: SessionStateManager
) : BluetoothStateMonitor {

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    permissionManager.checkPermissions()
                    updateSessionStatus()
                }
                Intent.ACTION_AIRPLANE_MODE_CHANGED -> {
                    permissionManager.checkPermissions()
                    updateSessionStatus()
                }
            }
        }
    }

    private fun updateSessionStatus() {
        val status = when {
            !permissionManager.hasAdapter() -> com.kkr.juken.domain.model.state.SessionStatus.NoBluetoothAdapter
            !permissionManager.isBluetoothEnabled() -> com.kkr.juken.domain.model.state.SessionStatus.BluetoothDisabled
            !permissionManager.hasPermissions() -> com.kkr.juken.domain.model.state.SessionStatus.PermissionRequired
            else -> com.kkr.juken.domain.model.state.SessionStatus.Ready
        }
        sessionStateManager.updateStatus(status)
    }

    override fun startMonitoring() {
        val filter = IntentFilter().apply {
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        }
        context.registerReceiver(receiver, filter)
        permissionManager.checkPermissions()
    }

    override fun stopMonitoring() {
        try {
            context.unregisterReceiver(receiver)
        } catch (e: Exception) {
            // Ignore
        }
    }
}
