package com.kkr.juken.core.sync.internal

import com.kkr.juken.core.protocol.ProtocolCodec
import com.kkr.juken.core.protocol.model.EcuCommand
import com.kkr.juken.core.transport.ConnectionSession
import kotlinx.coroutines.*

/**
 * Executes a high-frequency polling loop for live telemetry.
 */
internal class LivePollingEngine(
    private val session: ConnectionSession,
    private val codec: ProtocolCodec,
    private val scope: CoroutineScope
) {
    private var pollingJob: Job? = null
    private var isPollingActive = false
    private var frequencyHz = 10

    fun start(hz: Int = 10) {
        frequencyHz = hz
        if (isPollingActive) return
        isPollingActive = true
        
        pollingJob = scope.launch(Dispatchers.IO) {
            val delayMs = 1000L / frequencyHz
            while (isActive && isPollingActive) {
                try {
                    poll()
                    delay(delayMs)
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                }
            }
        }
    }

    fun stop() {
        isPollingActive = false
        pollingJob?.cancel()
        pollingJob = null
    }

    private suspend fun poll() {
        val command = EcuCommand.FetchGlobalVariables() 
        val payload = codec.serialize(command).toByteArray(Charsets.US_ASCII)
        session.write(payload)
    }
}
