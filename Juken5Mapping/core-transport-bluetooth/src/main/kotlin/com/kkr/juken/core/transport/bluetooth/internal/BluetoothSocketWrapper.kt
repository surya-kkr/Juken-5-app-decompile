package com.kkr.juken.core.transport.bluetooth.internal

import android.bluetooth.BluetoothSocket
import java.io.InputStream
import java.io.OutputStream

/**
 * Interface wrapping [BluetoothSocket] to allow for easier testing and mocking.
 */
interface BluetoothSocketWrapper {
    val inputStream: InputStream
    val outputStream: OutputStream
    fun connect()
    fun close()
    fun isConnected(): Boolean
}

class RealBluetoothSocketWrapper(private val socket: BluetoothSocket) : BluetoothSocketWrapper {
    override val inputStream: InputStream get() = socket.inputStream
    override val outputStream: OutputStream get() = socket.outputStream
    override fun connect() = socket.connect()
    override fun close() = socket.close()
    override fun isConnected(): Boolean = socket.isConnected
}
