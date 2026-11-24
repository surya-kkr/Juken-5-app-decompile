package com.kkr.juken.core.transport

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Data model for a single recorded protocol interaction.
 */
data class RecordEntry(
    val timestamp: Long,
    val direction: Direction,
    val connectionId: ConnectionId,
    val rawBytes: ByteArray,
    val rawHex: String,
    val rawAscii: String,
    val decodedCommand: String? = null,
    val decodedResponse: String? = null,
    val latencyMs: Long? = null,
    val transportType: String,
    val protocolVersion: String
) {
    enum class Direction { TX, RX }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as RecordEntry
        if (!rawBytes.contentEquals(other.rawBytes)) return false
        return true
    }

    override fun hashCode(): Int = rawBytes.contentHashCode()
}

/**
 * Captures and stores protocol interactions for diagnostic and testing purposes.
 */
class ProtocolRecorder {
    private val _history = MutableStateFlow<List<RecordEntry>>(emptyList())
    val history: StateFlow<List<RecordEntry>> = _history.asStateFlow()

    fun record(entry: RecordEntry) {
        _history.update { it + entry }
    }

    fun clear() {
        _history.value = emptyList()
    }
}
