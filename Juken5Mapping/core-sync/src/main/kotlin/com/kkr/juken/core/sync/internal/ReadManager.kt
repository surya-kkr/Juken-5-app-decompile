package com.kkr.juken.core.sync.internal

import com.kkr.juken.domain.model.tuning.MapType
import com.kkr.juken.domain.model.ui.TransferMetrics
import com.kkr.juken.domain.model.ui.TransferType
import com.kkr.juken.domain.model.ui.TransferUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReadManager(
    private val orchestrator: ReadWriteOrchestrator,
    private val scope: CoroutineScope,
    private val onBlockRead: (MapType, Int, List<Int>) -> Unit
) {
    private val _state = MutableStateFlow<TransferUiState>(TransferUiState.Idle)
    val state: StateFlow<TransferUiState> = _state.asStateFlow()

    fun readMap(type: MapType) {
        scope.launch {
            try {
                val total = 21 // Juken 5 standard
                var completed = 0
                
                for (i in 0 until total) {
                    _state.value = TransferUiState.InProgress(
                        type = TransferType.READ,
                        progress = (i * 100) / total,
                        currentStep = "Reading block $i",
                        metrics = TransferMetrics(totalBlocks = total, completedBlocks = i)
                    )
                    
                    val values = orchestrator.readBlock(type, i)
                    onBlockRead(type, i, values)
                    completed++
                }
                
                _state.value = TransferUiState.Completed(TransferType.READ)
            } catch (e: Exception) {
                _state.value = TransferUiState.Failed(TransferType.READ, e.message ?: "Unknown error")
            }
        }
    }
}
