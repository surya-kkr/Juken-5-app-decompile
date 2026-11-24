package com.kkr.juken.core.transport

import kotlinx.coroutines.flow.Flow

/**
 * Abstract interface for physical data exchange with the ECU.
 * Implementations handle the specifics of Bluetooth, WiFi, or Mocked hardware.
 * Handles only raw bytes; no knowledge of ASCII or protocol logic.
 */
interface Transport {
    /**
     * Observable stream of raw byte packets received from the ECU.
     */
    val incomingPackets: Flow<ByteArray>

    /**
     * Sends raw bytes to the ECU hardware.
     */
    suspend fun write(data: ByteArray)

    /**
     * Establishes a physical connection to the ECU hardware.
     *
     * @param address Hardware-specific address (e.g., MAC or IP).
     * @throws Exception if connection fails.
     */
    suspend fun connect(address: String)

    /**
     * Terminates the physical connection and releases hardware resources.
     */
    suspend fun disconnect()

    /**
     * Returns true if the hardware is currently connected.
     */
    fun isConnected(): Boolean
}
