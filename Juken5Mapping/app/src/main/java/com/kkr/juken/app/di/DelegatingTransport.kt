package com.kkr.juken.app.di

import com.kkr.juken.core.transport.Transport
import com.kkr.juken.domain.model.state.TransportType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

/**
 * A [Transport] that delegates to another implementation based on a selector.
 */
class DelegatingTransport(
    private val btTransport: Transport,
    private val simulatorTransport: Transport,
    transportTypeSelector: Flow<TransportType>
) : Transport {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val currentTransport: Flow<Transport> = transportTypeSelector
        .map { type ->
            when (type) {
                TransportType.BLUETOOTH -> btTransport
                TransportType.SIMULATOR -> simulatorTransport
                else -> btTransport
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val incomingPackets: Flow<ByteArray> = currentTransport.flatMapLatest { it.incomingPackets }

    override suspend fun write(data: ByteArray) {
        currentTransport.first().write(data)
    }

    override suspend fun connect(address: String) {
        currentTransport.first().connect(address)
    }

    override suspend fun disconnect() {
        // Disconnect all to be safe? Or just the active one?
        // Usually we only care about the active one.
        currentTransport.first().disconnect()
    }

    override fun isConnected(): Boolean {
        // In this architecture, it's hard to get the current transport synchronously.
        // We'll return the status of the bluetooth one as a fallback, 
        // or better, implement isConnected as a flow in the future.
        return runBlocking { currentTransport.first().isConnected() }
    }
}
