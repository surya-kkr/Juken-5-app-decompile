package com.kkr.juken.core.protocol.model

/**
 * Represents a single point in the ECU history log.
 */
data class HistoryEntry(
    val row: Int,
    val col: Int,
    val afr: Double,
    val timestamp: Long = System.currentTimeMillis()
)
