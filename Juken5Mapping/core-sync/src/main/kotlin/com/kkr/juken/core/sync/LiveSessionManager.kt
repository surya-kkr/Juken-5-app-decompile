package com.kkr.juken.core.sync

import com.kkr.juken.core.protocol.ProtocolCodec
import com.kkr.juken.core.protocol.model.*
import com.kkr.juken.core.protocol.parser.*
import com.kkr.juken.core.sync.internal.ActiveCellTracker
import com.kkr.juken.core.sync.internal.LivePollingEngine
import com.kkr.juken.core.transport.ConnectionSession
import com.kkr.juken.domain.model.ui.LiveSessionUiState
import com.kkr.juken.domain.model.ui.RealtimeEngineState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * High-level manager for real-time ECU interaction.
 */
class LiveSessionManager(
    private val session: ConnectionSession,
    private val codec: ProtocolCodec,
    private val scope: CoroutineScope
) {
    private val pollingEngine = LivePollingEngine(session, codec, scope)
    private val tracker = ActiveCellTracker()

    private val _status = MutableStateFlow<LiveSessionUiState>(LiveSessionUiState.Idle)
    val status: StateFlow<LiveSessionUiState> = _status.asStateFlow()

    private val _realtimeState = MutableStateFlow(RealtimeEngineState())
    val realtimeState: StateFlow<RealtimeEngineState> = _realtimeState.asStateFlow()

    private val trail = mutableListOf<Int>()

    init {
        observeIncoming()
    }

    fun start(hz: Int = 10) {
        _status.value = LiveSessionUiState.Streaming
        pollingEngine.start(hz)
    }

    fun stop() {
        _status.value = LiveSessionUiState.Idle
        pollingEngine.stop()
    }

    fun pause() {
        _status.value = LiveSessionUiState.Paused
        pollingEngine.stop()
    }

    private fun observeIncoming() {
        session.incomingPackets
            .map { raw ->
                val context = ParserContext(
                    protocolVersion = ProtocolVersion.JUKEN_5,
                    transportType = "Live",
                    connectionId = session.id.value,
                    timestamp = System.currentTimeMillis()
                )
                codec.decode(String(raw, Charsets.US_ASCII), context)
            }
            .filterIsInstance<ParserResult.Success<EcuResponse>>()
            .map { it.value }
            .onEach { response ->
                when (response) {
                    is EcuResponse.LiveDataResponse -> processTelemetry(response)
                    is EcuResponse.GlobalVariablesResponse -> processGlobals(response)
                    else -> {}
                }
            }
            .launchIn(scope)
    }

    private fun processTelemetry(data: EcuResponse.LiveDataResponse) {
        val activeIndex = tracker.findActiveIndex(data.rpm, data.tps)
        updateTrail(activeIndex)
        
        _realtimeState.update { current ->
            current.copy(
                rpm = data.rpm,
                tps = data.tps,
                battery = data.batteryVoltage,
                temperature = data.temperature,
                activeCellIndex = activeIndex,
                interpolationIndices = tracker.findInterpolationIndices(data.rpm, data.tps),
                movementTrail = trail.toList()
            )
        }
    }

    private fun processGlobals(data: EcuResponse.GlobalVariablesResponse) {
        // Map 9606 to telemetry if 9607 not available
    }

    private fun updateTrail(index: Int) {
        if (trail.lastOrNull() == index) return
        trail.add(index)
        if (trail.size > 20) trail.removeAt(0)
    }
}
