package com.kkr.juken.core.data.repository

import com.kkr.juken.core.sync.LiveSessionManager
import com.kkr.juken.core.sync.SyncEngine
import com.kkr.juken.core.sync.SyncState
import com.kkr.juken.core.transport.ConnectionSession
import com.kkr.juken.core.transport.ConnectionState
import com.kkr.juken.domain.model.ui.ConnectionUiState
import com.kkr.juken.domain.model.ui.RealtimeEngineState
import com.kkr.juken.domain.model.ui.LiveSessionUiState
import com.kkr.juken.domain.repository.EcuRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EcuRepositoryImpl @Inject constructor(
    private val session: ConnectionSession,
    private val liveManager: LiveSessionManager,
    private val syncEngine: SyncEngine,
    private val scope: CoroutineScope
) : EcuRepository {

    override val connectionState: StateFlow<ConnectionUiState> = combine(
        session.state,
        syncEngine.progress
    ) { connState, syncProgress ->
        when {
            syncProgress.currentState == SyncState.FAILED -> ConnectionUiState.Error(syncProgress.errorMessage ?: "Sync Failed")
            syncProgress.currentState == SyncState.READY -> ConnectionUiState.Connected
            syncProgress.currentState != SyncState.DISCONNECTED && syncProgress.currentState != SyncState.READY -> {
                ConnectionUiState.Synchronizing(syncProgress.progress, syncProgress.lastCompletedStep)
            }
            connState == ConnectionState.CONNECTING -> ConnectionUiState.Connecting
            connState == ConnectionState.CONNECTED -> ConnectionUiState.Connected
            connState == ConnectionState.FAILED -> ConnectionUiState.Error("Connection failure")
            connState == ConnectionState.HEARTBEAT_LOST -> ConnectionUiState.HeartbeatLost
            else -> ConnectionUiState.Idle
        }
    }.stateIn(scope, SharingStarted.Eagerly, ConnectionUiState.Idle)

    init {
        scope.launch {
            syncEngine.progress
                .filter { it.currentState == SyncState.READY }
                .collect {
                    liveManager.start()
                }
        }
    }

    override val liveSessionState: StateFlow<LiveSessionUiState> = liveManager.status
    override val realtimeEngineState: StateFlow<RealtimeEngineState> = liveManager.realtimeState

    override suspend fun connect(address: String) {
        syncEngine.start(address)
    }

    override suspend fun disconnect() {
        syncEngine.stop()
        session.disconnect()
        liveManager.stop()
    }

    override fun startStreaming() = liveManager.start()
    override fun stopStreaming() = liveManager.stop()
    override fun pauseStreaming() = liveManager.pause()
    override fun resumeStreaming() = liveManager.start()

    override suspend fun writePacket(payload: ByteArray) = session.write(payload)
}
