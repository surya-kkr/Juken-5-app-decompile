package com.kkr.juken.feature.mapping.presentation.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kkr.juken.domain.model.tuning.MapType
import com.kkr.juken.domain.repository.EcuRepository
import com.kkr.juken.domain.repository.CalibrationRepository
import com.kkr.juken.domain.model.ui.TransferUiState
import com.kkr.juken.domain.model.ui.RealtimeEngineState
import com.kkr.juken.feature.mapping.domain.engine.MapEngine
import com.kkr.juken.feature.mapping.domain.engine.MapEngineState
import com.kkr.juken.feature.mapping.presentation.editor.engine.GridEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapEditorViewModel @Inject constructor(
    private val ecuRepository: EcuRepository,
    private val calibrationRepository: CalibrationRepository
) : ViewModel() {

    private val engine = MapEngine(21, 61)
    val grid = GridEngine(21, 61)

    val realtimeState: StateFlow<RealtimeEngineState> = ecuRepository.realtimeEngineState

    private val _uiState = MutableStateFlow(MapEditorUiState())
    val uiState: StateFlow<MapEditorUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            engine.state.collect { mapState ->
                _uiState.update { it.copy(mapState = mapState) }
            }
        }
    }

    fun onAction(action: MapEditorAction) {
        when (action) {
            is MapEditorAction.SelectCell -> {
                grid.selection.selectCell(action.row, action.col, action.multi)
            }
            is MapEditorAction.EditSelected -> {
                val selection = grid.selection.selection
                if (selection.size == 1) {
                    val index = selection.first()
                    engine.editCell(index / engine.cols, index % engine.cols, action.value)
                } else if (selection.isNotEmpty()) {
                    engine.add(selection, action.value)
                }
            }
            MapEditorAction.Undo -> engine.undo()
            MapEditorAction.Redo -> engine.redo()
            MapEditorAction.WriteToEcu -> {
                // This would call WriteMapUseCase
            }
            MapEditorAction.DismissTransfer -> { }
            else -> {}
        }
    }
}

data class MapEditorUiState(
    val mapState: MapEngineState = MapEngineState(IntArray(0)),
    val isLoading: Boolean = false,
    val mapType: MapType = MapType.FUEL,
    val writeState: TransferUiState = TransferUiState.Idle
)

sealed interface MapEditorAction {
    data class SelectCell(val row: Int, val col: Int, val multi: Boolean = false) : MapEditorAction
    data class EditSelected(val value: Int) : MapEditorAction
    object Undo : MapEditorAction
    object Redo : MapEditorAction
    object Interpolate : MapEditorAction
    object WriteToEcu : MapEditorAction
    object DismissTransfer : MapEditorAction
}
