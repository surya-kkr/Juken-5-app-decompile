package com.kkr.juken.core.transport.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class BluetoothPermissionManagerTest {

    private val context = mockk<Context>(relaxed = true)
    private val adapter = mockk<BluetoothAdapter>()
    private lateinit var manager: AndroidBluetoothPermissionManager

    @Before
    fun setup() {
        mockkStatic(ContextCompat::class)
        manager = AndroidBluetoothPermissionManager(context, adapter)
    }

    @Test
    fun `hasPermissions should return true when all permissions granted`() {
        every { ContextCompat.checkSelfPermission(any(), any()) } returns PackageManager.PERMISSION_GRANTED
        assertTrue(manager.hasPermissions())
    }

    @Test
    fun `hasPermissions should return false when any permission denied`() {
        every { ContextCompat.checkSelfPermission(any(), any()) } returns PackageManager.PERMISSION_DENIED
        assertFalse(manager.hasPermissions())
    }

    @Test
    fun `isBluetoothEnabled should reflect adapter state`() {
        every { adapter.isEnabled } returns true
        assertTrue(manager.isBluetoothEnabled())
        
        every { adapter.isEnabled } returns false
        assertFalse(manager.isBluetoothEnabled())
    }

    @Test
    fun `hasAdapter should return true if adapter not null`() {
        assertTrue(manager.hasAdapter())
        
        val nullManager = AndroidBluetoothPermissionManager(context, null)
        assertFalse(nullManager.hasAdapter())
    }
}
