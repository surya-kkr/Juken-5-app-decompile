package com.kkr.juken.feature.autotune.workspace.presentation.explorer

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kkr.juken.core.ui.components.GlassSurface
import com.kkr.juken.feature.autotune.workspace.presentation.explorer.components.*

@Composable
fun RecommendationExplorerScreen(
    viewModel: RecommendationExplorerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    GlassSurface(
        modifier = Modifier.fillMaxSize(),
        blur = 0.dp
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            RecommendationSearchBar(
                query = uiState.criteria.searchRegex ?: "",
                onQueryChange = { viewModel.onAction(RecommendationAction.Search(it)) }
            )
            
            RecommendationStatisticsBar(
                total = uiState.rootGroup?.recommendations?.size ?: 0,
                filtered = uiState.rootGroup?.recommendations?.size ?: 0,
                accepted = uiState.selection.size
            )

            RecommendationToolbar(
                onFilterClick = { },
                onSortClick = { }
            )

            uiState.rootGroup?.let { root ->
                RecommendationTree(
                    rootGroup = root,
                    selection = uiState.selection,
                    onRecommendationClick = { id, multi -> 
                        viewModel.onAction(RecommendationAction.ToggleSelection(id, multi)) 
                    }
                )
            }
        }
    }
}
