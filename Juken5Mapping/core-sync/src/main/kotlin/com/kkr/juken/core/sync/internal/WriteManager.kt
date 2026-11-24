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

class WriteManager(
    private val orchestrator: ReadWriteOrchestrator,
    private val verifier: WriteVerifier,
    private val rollback: RollbackManager,
    private val scope: CoroutineScope
) {
    private val _state = MutableStateFlow<TransferUiState>(TransferUiState.Idle)
    val state: StateFlow<TransferUiState> = _state.asStateFlow()

    fun writeMap(type: MapType, data: Map<Int, List<Int>>) {
        scope.launch {
            try {
                val total = data.size
                var completed = 0
                
                data.forEach { (index, values) ->
                    _state.value = TransferUiState.InProgress(
                        type = TransferType.WRITE,
                        progress = (completed * 100) / total,
                        currentStep = "Writing block $index",
                        metrics = TransferMetrics(totalBlocks = total, completedBlocks = completed)
                    )
                    
                    orchestrator.writeBlock(type, index, values)
                    
                    _state.value = TransferUiState.Verifying((completed * 100) / total)
                    val ecuData = orchestrator.readBlock(type, index)
                    if (!verifier.verify(ecuData, values)) {
                        throw Exception("Verification failed for block $index")
                    }
                    
                    completed++
                }
                
                _state.value = TransferUiState.Completed(TransferType.WRITE)
            } catch (e: Exception) {
                _state.value = TransferUiState.Failed(TransferType.WRITE, e.message ?: "Unknown error")
            }
        }
    }
}
