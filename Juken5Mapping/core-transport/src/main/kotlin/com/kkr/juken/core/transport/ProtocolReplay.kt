package com.kkr.juken.core.transport

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Transport implementation that replays recorded [RecordEntry] sessions.
 */
class ProtocolReplay(
    private val records: List<RecordEntry>,
    private val mode: ReplayMode = ReplayMode.SEQUENTIAL,
    private val preserveTiming: Boolean = true,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) : Transport {

    enum class ReplayMode { 
        SEQUENTIAL, 
        RESPONSE_MATCHING 
    }

    private val _incoming = MutableSharedFlow<ByteArray>(replay = 1, extraBufferCapacity = 128)
    override val incomingPackets: SharedFlow<ByteArray> = _incoming.asSharedFlow()

    private var connected = false
    private var replayJob: Job? = null

    override suspend fun connect(address: String) {
        connected = true
        if (mode == ReplayMode.SEQUENTIAL) {
            replayJob = scope.launch {
                startSequentialReplay()
            }
        }
    }

    override suspend fun disconnect() {
        connected = false
        replayJob?.cancel()
    }

    override suspend fun write(data: ByteArray) {
        if (!connected) return
        if (mode == ReplayMode.RESPONSE_MATCHING) {
            val ascii = String(data, Charsets.US_ASCII).trim()
            val match = records.find { it.direction == RecordEntry.Direction.TX && it.rawAscii.trim() == ascii }
            match?.let {
                val index = records.indexOf(it)
                val response = records.getOrNull(index + 1)
                if (response?.direction == RecordEntry.Direction.RX) {
                    _incoming.emit(response.rawBytes)
                }
            }
        }
    }

    override fun isConnected(): Boolean = connected

    private suspend fun startSequentialReplay() {
        var lastTime = records.firstOrNull()?.timestamp ?: 0L
        records.forEach { record ->
            if (preserveTiming) {
                val wait = record.timestamp - lastTime
                if (wait > 0) delay(wait)
            }
            if (record.direction == RecordEntry.Direction.RX) {
                _incoming.emit(record.rawBytes)
            }
            lastTime = record.timestamp
        }
    }
}
