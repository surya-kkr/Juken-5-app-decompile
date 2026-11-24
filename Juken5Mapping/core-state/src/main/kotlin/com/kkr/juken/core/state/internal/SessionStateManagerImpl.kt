package com.kkr.juken.core.state.internal

import com.kkr.juken.domain.model.state.SessionState
import com.kkr.juken.domain.model.state.SessionStatus
import com.kkr.juken.domain.model.state.TransportType
import com.kkr.juken.domain.state.SessionStateManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SessionStateManagerImpl @Inject constructor() : SessionStateManager {
    
    private val _state = MutableStateFlow(SessionState())
    override val state: StateFlow<SessionState> = _state.asStateFlow()

    override fun onConnected(address: String, transport: TransportType) {
        val current = _state.value
        _state.value = current.copy(
            status = SessionStatus.Connected,
            connectedDeviceAddress = address,
            transportType = transport,
            startTimeMs = System.currentTimeMillis()
        )
    }

    override fun onDisconnected() {
        _state.value = _state.value.copy(
            status = SessionStatus.Disconnected,
            connectedDeviceAddress = null,
            isHeartbeatActive = false
        )
    }

    override fun updateHeartbeat(active: Boolean) {
        _state.value = _state.value.copy(isHeartbeatActive = active)
    }

    override fun updateStatus(status: SessionStatus) {
        _state.value = _state.value.copy(status = status)
    }
}
