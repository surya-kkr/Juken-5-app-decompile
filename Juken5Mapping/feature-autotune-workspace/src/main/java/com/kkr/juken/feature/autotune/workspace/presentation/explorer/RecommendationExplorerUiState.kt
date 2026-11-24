package com.kkr.juken.feature.autotune.workspace.presentation.explorer

import com.kkr.juken.core.autotune.engine.FilterCriteria
import com.kkr.juken.core.autotune.engine.RecommendationSortEngine
import com.kkr.juken.domain.model.autotune.RecommendationGroup

/**
 * UI State for the Recommendation Explorer panel.
 */
data class RecommendationExplorerUiState(
    val rootGroup: RecommendationGroup? = null,
    val criteria: FilterCriteria = FilterCriteria(),
    val sortType: RecommendationSortEngine.SortType = RecommendationSortEngine.SortType.CONFIDENCE,
    val isAscending: Boolean = false,
    val selection: Set<String> = emptySet(),
    val isLoading: Boolean = false
)
