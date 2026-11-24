package com.kkr.juken.core.transport.bluetooth

import android.bluetooth.BluetoothAdapter
import android.content.Context
import com.kkr.juken.core.transport.BluetoothPermissionManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class AndroidBluetoothScannerTest {

    private val context = mockk<Context>(relaxed = true)
    private val adapter = mockk<BluetoothAdapter>(relaxed = true)
    private val permissionManager = mockk<BluetoothPermissionManager>()
    private lateinit var scanner: AndroidBluetoothScanner

    @Before
    fun setup() {
        scanner = AndroidBluetoothScanner(context, adapter, permissionManager)
    }

    @Test
    fun `startScan should check permissions before starting discovery`() {
        every { permissionManager.hasPermissions() } returns true
        every { permissionManager.isBluetoothEnabled() } returns true
        every { adapter.isDiscovering } returns false
        
        scanner.startScan()
        
        verify { permissionManager.hasPermissions() }
        verify { adapter.startDiscovery() }
    }

    @Test
    fun `startScan should not start discovery if permissions missing`() {
        every { permissionManager.hasPermissions() } returns false
        
        scanner.startScan()
        
        verify { permissionManager.hasPermissions() }
        verify(exactly = 0) { adapter.startDiscovery() }
    }
}
