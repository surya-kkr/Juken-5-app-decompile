package com.kkr.juken.core.sync

/**
 * High-level states of the Synchronization Engine.
 */
enum class SyncState {
    DISCONNECTED,
    CONNECTING,
    HANDSHAKING,
    DETECTING_IDENTITY,
    READING_FUEL,
    READING_BASE_MAP,
    READING_INJECTOR,
    READING_IGNITION,
    READING_HISTORY,
    READY,
    FAILED
}

/**
 * Encapsulates the current progress and state of the synchronization process.
 *
 * @property currentState The current operational state of the FSM.
 * @property progress Percentage of completion (0-100).
 * @property lastCompletedStep Human-readable description of the last successful operation.
 * @property errorMessage Optional error details if the state is FAILED.
 */
data class SyncProgress(
    val currentState: SyncState,
    val progress: Int,
    val lastCompletedStep: String,
    val errorMessage: String? = null
)

/**
 * Configuration for command retries and timeouts.
 */
data class RetryPolicy(
    val maxRetries: Int = 3,
    val timeoutMs: Long = 1500L,
    val backoffMs: Long = 100L
)
