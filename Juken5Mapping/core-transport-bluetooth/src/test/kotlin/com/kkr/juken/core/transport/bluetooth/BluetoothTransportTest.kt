package com.kkr.juken.core.transport.bluetooth

import com.kkr.juken.core.transport.bluetooth.internal.BluetoothSocketWrapper
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class BluetoothTransportTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private val socket = mockk<BluetoothSocketWrapper>(relaxed = true)
    private val permissionManager = mockk<com.kkr.juken.core.transport.BluetoothPermissionManager>()
    private lateinit var transport: BluetoothTransport

    @Before
    fun setup() {
        every { permissionManager.hasPermissions() } returns true
        every { permissionManager.isBluetoothEnabled() } returns true
        
        transport = BluetoothTransport(
            adapter = null,
            permissionManager = permissionManager,
            scope = testScope,
            dispatcher = testDispatcher,
            socketFactory = { socket }
        )
    }

    @Test
    fun `connect should call socket connect and start reading`() = testScope.runTest {
        coEvery { socket.connect() } just Runs
        
        transport.connect("MOCK_ADDR")
        
        verify { socket.connect() }
        assertTrue(transport.isConnected())
    }

    @Test
    fun `write should send bytes to outputStream`() = testScope.runTest {
        val out = ByteArrayOutputStream()
        every { socket.outputStream } returns out
        transport.connect("MOCK_ADDR")
        
        val data = "HELLO".toByteArray()
        transport.write(data)
        
        assertEquals("HELLO", out.toString())
    }

    @Test
    fun `incomingPackets should emit data from inputStream`() = testScope.runTest {
        val input = ByteArrayInputStream("WORLD".toByteArray())
        every { socket.inputStream } returns input
        
        transport.connect("MOCK_ADDR")
        
        val rx = transport.incomingPackets.first()
        assertEquals("WORLD", String(rx))
    }

    @Test
    fun `disconnect should close socket`() = testScope.runTest {
        transport.connect("MOCK_ADDR")
        transport.disconnect()
        
        verify { socket.close() }
        assertEquals(false, transport.isConnected())
    }
}
