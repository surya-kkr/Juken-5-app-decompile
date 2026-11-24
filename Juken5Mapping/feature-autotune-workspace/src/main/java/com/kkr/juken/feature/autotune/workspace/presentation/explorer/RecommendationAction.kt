package com.kkr.juken.feature.autotune.workspace.presentation.explorer

import com.kkr.juken.core.autotune.engine.FilterCriteria
import com.kkr.juken.core.autotune.engine.RecommendationSortEngine

sealed interface RecommendationAction {
    data class UpdateFilter(val criteria: FilterCriteria) : RecommendationAction
    data class UpdateSort(val type: RecommendationSortEngine.SortType, val ascending: Boolean) : RecommendationAction
    data class ToggleSelection(val recommendationId: String, val multi: Boolean) : RecommendationAction
    object SelectAll : RecommendationAction
    object ClearSelection : RecommendationAction
    data class Search(val query: String) : RecommendationAction
}
