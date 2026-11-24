package com.kkr.juken.core.sync

import com.kkr.juken.core.protocol.ProtocolCodec
import com.kkr.juken.core.protocol.model.*
import com.kkr.juken.core.protocol.parser.*
import com.kkr.juken.core.transport.ConnectionSession
import com.kkr.juken.core.transport.ConnectionState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * Finite State Machine for ECU data synchronization.
 */
class SyncEngine(
    private val codec: ProtocolCodec,
    private val session: ConnectionSession,
    private val listener: SyncDataListener,
    private val retryPolicy: RetryPolicy = RetryPolicy(),
    private val scope: CoroutineScope
) {
    private val _progress = MutableStateFlow(SyncProgress(SyncState.DISCONNECTED, 0, "Idle"))
    val progress: StateFlow<SyncProgress> = _progress.asStateFlow()

    private var syncJob: Job? = null
    private var lastSuccessfulProgress = 0

    fun start(address: String) {
        if (address.isEmpty() && session.state.value != ConnectionState.CONNECTED) {
             updateProgress(SyncState.FAILED, 0, "No address provided")
             return
        }
        
        syncJob?.cancel()
        syncJob = scope.launch {
            try {
                runSync(address)
            } catch (e: Exception) {
                updateProgress(SyncState.FAILED, lastSuccessfulProgress, "Sync Failed", e.message)
            } catch (t: Throwable) {
                if (t is CancellationException) {
                     val isTimeout = t.message?.contains("timed out", ignoreCase = true) == true
                     if (isTimeout) {
                         updateProgress(SyncState.FAILED, lastSuccessfulProgress, "Timeout", t.message)
                     } else {
                         updateProgress(SyncState.DISCONNECTED, lastSuccessfulProgress, "Cancelled")
                         throw t
                     }
                } else {
                    updateProgress(SyncState.FAILED, lastSuccessfulProgress, "Fatal Error", t.message)
                }
            }
        }
    }

    fun reset() {
        stop()
        lastSuccessfulProgress = 0
        _progress.value = SyncProgress(SyncState.DISCONNECTED, 0, "Idle")
    }

    fun stop() {
        syncJob?.cancel()
    }

    private suspend fun runSync(address: String) {
        if (session.state.value != ConnectionState.CONNECTED) {
            updateProgress(SyncState.CONNECTING, 2, "Connecting to $address")
            session.connect(address)
        }

        if (lastSuccessfulProgress < 5) {
            updateProgress(SyncState.HANDSHAKING, 5, "Requesting Identity")
            val identityResponse = executeCommand<EcuResponse.EcuIdentityResponse>(EcuCommand.Handshake())
            listener.onEcuIdentityReceived(identityResponse.identity)
            lastSuccessfulProgress = 5
        }

        if (lastSuccessfulProgress < 10) {
            updateProgress(SyncState.DETECTING_IDENTITY, 10, "Reading Global Variables")
            val globalVars = executeCommand<EcuResponse.GlobalVariablesResponse>(EcuCommand.FetchGlobalVariables())
            listener.onGlobalVariablesReceived(globalVars.variables)
            lastSuccessfulProgress = 10
        }

        syncMapIfneeded(MapType.FUEL, SyncState.READING_FUEL, 15)
        syncMapIfneeded(MapType.BASE_MAP, SyncState.READING_BASE_MAP, 35)
        syncMapIfneeded(MapType.INJECTOR, SyncState.READING_INJECTOR, 55)
        syncMapIfneeded(MapType.IGNITION, SyncState.READING_IGNITION, 75)

        if (lastSuccessfulProgress < 95) {
            syncHistory()
        }

        updateProgress(SyncState.READY, 100, "Ready")
    }

    private suspend fun syncMapIfneeded(type: MapType, state: SyncState, baseProgress: Int) {
        val domainMapType = when (type) {
            MapType.FUEL -> com.kkr.juken.domain.model.tuning.MapType.FUEL
            MapType.BASE_MAP -> com.kkr.juken.domain.model.tuning.MapType.BASE_MAP
            MapType.INJECTOR -> com.kkr.juken.domain.model.tuning.MapType.INJECTOR
            MapType.IGNITION -> com.kkr.juken.domain.model.tuning.MapType.IGNITION
        }

        for (i in 0..20) {
            val stepProgress = baseProgress + i
            if (lastSuccessfulProgress < stepProgress) {
                updateProgress(state, stepProgress, "Reading $type block $i")
                val response = executeCommand<EcuResponse>(EcuCommand.ReadMapBlock(type = type, blockIndex = i))
                
                val (blockIndex, values) = when (response) {
                    is EcuResponse.FuelMapResponse -> response.blockIndex to response.values
                    is EcuResponse.BaseMapResponse -> response.blockIndex to response.values
                    is EcuResponse.InjectorMapResponse -> response.blockIndex to response.values
                    is EcuResponse.IgnitionMapResponse -> response.blockIndex to response.values
                    else -> throw Exception("Unexpected response: ${response::class.simpleName}")
                }
                
                listener.onMapBlockReceived(domainMapType, 1, blockIndex, values)
                lastSuccessfulProgress = stepProgress
            }
        }
    }

    private suspend fun syncHistory() {
        for (i in 0..20) {
            val stepProgress = 95 + i
            if (lastSuccessfulProgress < stepProgress) {
                updateProgress(SyncState.READING_HISTORY, stepProgress, "Reading History block $i")
                val response = executeCommand<EcuResponse.HistoryResponse>(EcuCommand.ReadHistory(blockIndex = i))
                listener.onHistoryReceived(response.blockIndex, response.entries)
                lastSuccessfulProgress = stepProgress
            }
        }
    }

    private suspend fun <T : EcuResponse> executeCommand(command: EcuCommand): T {
        var lastError: Throwable? = null
        repeat(retryPolicy.maxRetries) { attempt ->
            try {
                return withTimeout(retryPolicy.timeoutMs) {
                    val ascii = codec.serialize(command)
                    session.write(ascii.toByteArray(Charsets.US_ASCII))
                    session.incomingPackets
                        .map { raw ->
                            val context = ParserContext(
                                protocolVersion = ProtocolVersion.JUKEN_5,
                                transportType = "Unknown",
                                connectionId = "sync",
                                timestamp = System.currentTimeMillis()
                            )
                            codec.decode(String(raw, Charsets.US_ASCII), context)
                        }
                        .filterIsInstance<ParserResult.Success<T>>()
                        .map { it.value }
                        .first()
                }
            } catch (e: Exception) {
                lastError = e
            } catch (t: Throwable) {
                lastError = t
                if (t is CancellationException && t.message?.contains("timed out") != true) throw t
            }
            if (attempt < retryPolicy.maxRetries - 1) delay(retryPolicy.backoffMs)
        }
        throw lastError ?: Exception("Command failed")
    }

    private fun updateProgress(state: SyncState, progress: Int, step: String, error: String? = null) {
        _progress.value = SyncProgress(
            currentState = state,
            progress = progress,
            lastCompletedStep = step,
            errorMessage = error
        )
    }
}
