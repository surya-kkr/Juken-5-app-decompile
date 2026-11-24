package com.kkr.juken.domain.repository

import com.kkr.juken.domain.model.ui.ConnectionUiState
import com.kkr.juken.domain.model.ui.LiveSessionUiState
import com.kkr.juken.domain.model.ui.RealtimeEngineState
import kotlinx.coroutines.flow.StateFlow

/**
 * Repository focused exclusively on physical or simulated ECU communication.
 */
interface EcuRepository {
    val connectionState: StateFlow<ConnectionUiState>
    val liveSessionState: StateFlow<LiveSessionUiState>
    val realtimeEngineState: StateFlow<RealtimeEngineState>
    
    suspend fun connect(address: String)
    suspend fun disconnect()
    
    fun startStreaming()
    fun stopStreaming()
    fun pauseStreaming()
    fun resumeStreaming()
    
    suspend fun writePacket(payload: ByteArray)
}
