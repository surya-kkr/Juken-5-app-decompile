package com.kkr.juken.domain.model.ui

/**
 * High-level state for Read or Write operations.
 */
sealed interface TransferUiState {
    object Idle : TransferUiState
    
    data class InProgress(
        val type: TransferType,
        val progress: Int,
        val currentStep: String,
        val metrics: TransferMetrics
    ) : TransferUiState
    
    data class Verifying(val progress: Int) : TransferUiState
    
    data class Completed(val type: TransferType) : TransferUiState
    
    data class Failed(
        val type: TransferType,
        val reason: String,
        val canRetry: Boolean = false
    ) : TransferUiState
}

enum class TransferType { READ, WRITE, VERIFY, ROLLBACK }

data class TransferMetrics(
    val bytesPerSec: Int = 0,
    val etaMs: Long = 0,
    val retryCount: Int = 0,
    val totalBlocks: Int = 0,
    val completedBlocks: Int = 0
)
