package com.kkr.juken.feature.autotune.workspace.presentation.workspace

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.kkr.juken.core.ui.components.*
import com.kkr.juken.feature.autotune.workspace.presentation.explorer.RecommendationExplorerScreen
import com.kkr.juken.feature.autotune.workspace.presentation.workspace.components.*
import com.kkr.juken.feature.autotune.workspace.presentation.layout.ResizableWorkspace
import com.kkr.juken.feature.autotune.workspace.presentation.inspector.RecommendationInspector

@Composable
fun AutoTuneWorkspaceScreen(
    viewModel: AutoTuneWorkspaceViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    GlassBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            WorkspaceToolbar(
                onToggleLeft = { viewModel.onAction(WorkspaceAction.ToggleLeft) },
                onToggleRight = { viewModel.onAction(WorkspaceAction.ToggleRight) },
                onToggleBottom = { viewModel.onAction(WorkspaceAction.ToggleBottom) }
            )

            WorkspaceScaffold(
                layout = uiState.layout,
                onResizeLeft = { viewModel.onAction(WorkspaceAction.ResizeLeft(it)) },
                onResizeRight = { viewModel.onAction(WorkspaceAction.ResizeRight(it)) },
                onResizeBottom = { viewModel.onAction(WorkspaceAction.ResizeBottom(it)) },
                leftPanel = {
                    RecommendationExplorerScreen()
                },
                centerPanel = {
                    GlassCard(modifier = Modifier.fillMaxSize()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                            Text("Map Editor Stage", color = Color.White.copy(alpha = 0.5f))
                        }
                    }
                },
                rightPanel = {
                    val selected = uiState.recommendations.find { it.cellIndex == uiState.selectedCellIndex }
                    RecommendationInspector(recommendation = selected)
                },
                bottomPanel = {
                    Column(modifier = Modifier.fillMaxSize()) {
                        TimelinePanel(modifier = Modifier.weight(1f))
                        AnalyticsPanel(modifier = Modifier.weight(1f))
                    }
                }
            )
        }
    }
}
