package com.kkr.juken.feature.autotune.workspace.presentation.workspace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kkr.juken.domain.model.workspace.WorkspaceLayout
import com.kkr.juken.domain.usecase.RestoreWorkspaceLayoutUseCase
import com.kkr.juken.domain.usecase.SaveWorkspaceLayoutUseCase
import com.kkr.juken.domain.usecase.LoadRecommendationsUseCase
import com.kkr.juken.feature.autotune.workspace.engine.WorkspaceLayoutEngine
import com.kkr.juken.domain.model.autotune.TuningRecommendation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AutoTuneWorkspaceViewModel @Inject constructor(
    private val restoreLayoutUseCase: RestoreWorkspaceLayoutUseCase,
    private val saveLayoutUseCase: SaveWorkspaceLayoutUseCase,
    private val loadRecommendationsUseCase: LoadRecommendationsUseCase
) : ViewModel() {

    private val layoutEngine = WorkspaceLayoutEngine()

    private val _uiState = MutableStateFlow(AutoTuneWorkspaceUiState())
    val uiState: StateFlow<AutoTuneWorkspaceUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val initialLayout = restoreLayoutUseCase()
            layoutEngine.setLayout(initialLayout)

            layoutEngine.layout.onEach { layout ->
                _uiState.update { it.copy(layout = layout) }
                saveLayoutUseCase(layout)
            }.launchIn(this)
            
            loadRecommendationsUseCase("current").onEach { recs ->
                _uiState.update { it.copy(recommendations = recs) }
            }.launchIn(this)
        }
    }

    fun onAction(action: WorkspaceAction) {
        when (action) {
            is WorkspaceAction.ResizeLeft -> layoutEngine.resizeLeftPanel(action.delta)
            is WorkspaceAction.ResizeRight -> layoutEngine.resizeRightPanel(action.delta)
            is WorkspaceAction.ResizeBottom -> layoutEngine.resizeBottomPanel(action.delta)
            WorkspaceAction.ToggleLeft -> layoutEngine.toggleLeftPanel()
            WorkspaceAction.ToggleRight -> layoutEngine.toggleRightPanel()
            WorkspaceAction.ToggleBottom -> layoutEngine.toggleBottomPanel()
        }
    }
}

data class AutoTuneWorkspaceUiState(
    val layout: WorkspaceLayout = WorkspaceLayout(),
    val recommendations: List<TuningRecommendation> = emptyList(),
    val selectedCellIndex: Int? = null,
    val isLoading: Boolean = false
)

sealed interface WorkspaceAction {
    data class ResizeLeft(val delta: Float) : WorkspaceAction
    data class ResizeRight(val delta: Float) : WorkspaceAction
    data class ResizeBottom(val delta: Float) : WorkspaceAction
    object ToggleLeft : WorkspaceAction
    object ToggleRight : WorkspaceAction
    object ToggleBottom : WorkspaceAction
}
