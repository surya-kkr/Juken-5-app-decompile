package com.kkr.juken.core.transport.bluetooth

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.kkr.juken.core.transport.BluetoothPermissionManager
import com.kkr.juken.core.transport.BluetoothPermissionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidBluetoothPermissionManager @Inject constructor(
    private val context: Context,
    private val adapter: BluetoothAdapter?
) : BluetoothPermissionManager {

    private val _state = MutableStateFlow(BluetoothPermissionState())
    override val state: StateFlow<BluetoothPermissionState> = _state.asStateFlow()

    override fun hasPermissions(): Boolean {
        return getRequiredPermissions().all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun isBluetoothEnabled(): Boolean {
        return adapter?.isEnabled == true
    }

    override fun hasAdapter(): Boolean {
        return adapter != null
    }

    override fun shouldShowRationale(activity: Any): Boolean {
        if (activity !is Activity) return false
        return getRequiredPermissions().any {
            ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
        }
    }

    override fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun checkPermissions() {
        val newState = BluetoothPermissionState(
            isBluetoothEnabled = isBluetoothEnabled(),
            hasAdapter = hasAdapter(),
            bluetoothScanGranted = isGranted(Manifest.permission.BLUETOOTH_SCAN),
            bluetoothConnectGranted = isGranted(Manifest.permission.BLUETOOTH_CONNECT),
            locationGranted = isGranted(Manifest.permission.ACCESS_FINE_LOCATION),
            isPermanentlyDenied = false, // Handled in checkStatus(activity)
            shouldShowRationale = false
        )
        _state.value = newState
    }

    fun updateRationaleStatus(activity: Activity) {
        val newState = _state.value.copy(
            shouldShowRationale = shouldShowRationale(activity),
            isPermanentlyDenied = !hasPermissions() && !shouldShowRationale(activity)
        )
        _state.value = newState
    }

    private fun isGranted(permission: String): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            // Before Android 12, some permissions are auto-granted if in manifest
            if (permission == Manifest.permission.BLUETOOTH_SCAN || 
                permission == Manifest.permission.BLUETOOTH_CONNECT) return true
        }
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun getRequiredPermissions(): List<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            listOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        } else {
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN
            )
        }
    }
}
