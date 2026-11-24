package com.kkr.juken.core.transport.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import com.kkr.juken.core.transport.Transport
import com.kkr.juken.core.transport.bluetooth.internal.BluetoothSocketWrapper
import com.kkr.juken.core.transport.bluetooth.internal.PacketInspector
import com.kkr.juken.core.transport.bluetooth.internal.RealBluetoothSocketWrapper
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.io.IOException
import java.util.UUID

/**
 * Implementation of [Transport] using Bluetooth Classic RFCOMM (SPP).
 */
@SuppressLint("MissingPermission")
class BluetoothTransport(
    private val adapter: BluetoothAdapter?,
    private val permissionManager: com.kkr.juken.core.transport.BluetoothPermissionManager,
    private val sessionStateManager: com.kkr.juken.domain.state.SessionStateManager,
    private val scope: CoroutineScope,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val socketFactory: (String) -> BluetoothSocketWrapper = { address ->
        val device = adapter?.getRemoteDevice(address) ?: throw IOException("Device not found")
        val sppUuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        RealBluetoothSocketWrapper(device.createRfcommSocketToServiceRecord(sppUuid))
    }
) : Transport {

    private var socket: BluetoothSocketWrapper? = null
    private val inspector = PacketInspector("BluetoothTransport")

    private val _incomingPackets = MutableSharedFlow<ByteArray>(extraBufferCapacity = 128)
    override val incomingPackets: SharedFlow<ByteArray> = _incomingPackets.asSharedFlow()

    private var connected = false
    private var readJob: Job? = null

    override suspend fun write(data: ByteArray) = withContext(dispatcher) {
        val currentSocket = socket ?: throw IOException("Socket not connected")
        try {
            currentSocket.outputStream.write(data)
            currentSocket.outputStream.flush()
            inspector.inspect(data, PacketInspector.Direction.TX)
        } catch (e: IOException) {
            disconnect()
            throw e
        }
    }

    override suspend fun connect(address: String) = withContext(dispatcher) {
        if (connected) return@withContext
        
        if (!permissionManager.hasPermissions()) {
            throw IOException("Missing Bluetooth permissions")
        }

        if (!permissionManager.isBluetoothEnabled()) {
            throw IOException("Bluetooth is disabled")
        }

        if (adapter?.isDiscovering == true) {
            adapter.cancelDiscovery()
        }

        sessionStateManager.updateStatus(com.kkr.juken.domain.model.state.SessionStatus.Connecting)

        try {
            val newSocket = socketFactory(address)
            newSocket.connect()
            socket = newSocket
            connected = true
            sessionStateManager.onConnected(address, com.kkr.juken.domain.model.state.TransportType.BLUETOOTH)
            startReading()
        } catch (e: IOException) {
            socket?.close()
            socket = null
            sessionStateManager.updateStatus(com.kkr.juken.domain.model.state.SessionStatus.Failed(e.message ?: "Connection failed"))
            throw e
        }
    }

    override suspend fun disconnect() = withContext(dispatcher) {
        readJob?.cancel()
        readJob = null
        connected = false
        try {
            socket?.close()
        } catch (e: IOException) {
            // Ignore
        }
        socket = null
    }

    override fun isConnected(): Boolean = connected

    private fun startReading() {
        readJob?.cancel()
        readJob = scope.launch(dispatcher) {
            val buffer = ByteArray(1024)
            val currentSocket = socket ?: return@launch
            val inputStream = currentSocket.inputStream

            while (isActive && connected) {
                try {
                    val bytesRead = inputStream.read(buffer)
                    if (bytesRead > 0) {
                        val data = buffer.copyOfRange(0, bytesRead)
                        inspector.inspect(data, PacketInspector.Direction.RX)
                        _incomingPackets.emit(data)
                    } else if (bytesRead == -1) {
                        disconnect()
                        break
                    }
                } catch (e: IOException) {
                    if (isActive) disconnect()
                    break
                }
            }
        }
    }
}
