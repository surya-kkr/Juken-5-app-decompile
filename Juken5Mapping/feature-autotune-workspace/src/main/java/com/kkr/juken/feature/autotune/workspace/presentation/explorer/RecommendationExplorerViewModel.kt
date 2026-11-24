package com.kkr.juken.feature.autotune.workspace.presentation.explorer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kkr.juken.core.autotune.engine.*
import com.kkr.juken.domain.model.autotune.RecommendationGroup
import com.kkr.juken.domain.model.autotune.TuningRecommendation
import com.kkr.juken.domain.repository.EcuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class RecommendationExplorerViewModel @Inject constructor(
    private val repository: EcuRepository
) : ViewModel() {

    private val filterEngine = SmartFilterEngine()
    private val groupingEngine = RecommendationGroupingEngine()
    private val sortEngine = RecommendationSortEngine()

    private val _uiState = MutableStateFlow(RecommendationExplorerUiState())
    val uiState: StateFlow<RecommendationExplorerUiState> = _uiState.asStateFlow()

    private val rawRecommendations = MutableStateFlow<List<TuningRecommendation>>(emptyList())

    init {
        rawRecommendations
            .combine(_uiState) { raw, state ->
                processRecommendations(raw, state.criteria, state.sortType, state.isAscending)
            }
            .onEach { result ->
                _uiState.update { it.copy(rootGroup = result) }
            }
            .launchIn(viewModelScope)
    }

    private fun processRecommendations(
        raw: List<TuningRecommendation>,
        criteria: FilterCriteria,
        sort: RecommendationSortEngine.SortType,
        asc: Boolean
    ): RecommendationGroup {
        val filtered = filterEngine.filter(raw, criteria)
        val sorted = sortEngine.sort(filtered, sort, asc)
        return groupingEngine.groupByMapAndMode(sorted)
    }

    fun onAction(action: RecommendationAction) {
        when (action) {
            is RecommendationAction.UpdateFilter -> _uiState.update { it.copy(criteria = action.criteria) }
            is RecommendationAction.UpdateSort -> _uiState.update { it.copy(sortType = action.type, isAscending = action.ascending) }
            is RecommendationAction.ToggleSelection -> {
                _uiState.update { current ->
                    val newSelection = if (action.multi) {
                        if (current.selection.contains(action.recommendationId)) current.selection - action.recommendationId
                        else current.selection + action.recommendationId
                    } else {
                        setOf(action.recommendationId)
                    }
                    current.copy(selection = newSelection)
                }
            }
            RecommendationAction.SelectAll -> { }
            RecommendationAction.ClearSelection -> _uiState.update { it.copy(selection = emptySet()) }
            is RecommendationAction.Search -> {
                _uiState.update { current ->
                    current.copy(criteria = current.criteria.copy(searchRegex = action.query))
                }
            }
        }
    }
}
