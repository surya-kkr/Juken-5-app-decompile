package com.kkr.juken.feature.autotune.workspace.engine

import com.kkr.juken.domain.model.workspace.WorkspaceLayout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Handles real-time resizing logic and constraints for the workspace panels.
 */
class WorkspaceLayoutEngine(
    initialLayout: WorkspaceLayout = WorkspaceLayout()
) {
    private val _layout = MutableStateFlow(initialLayout)
    val layout: StateFlow<WorkspaceLayout> = _layout.asStateFlow()

    fun resizeLeftPanel(delta: Float) {
        _layout.update { current ->
            val newFraction = (current.leftPanelWidthFraction + delta).coerceIn(0.1f, 0.4f)
            current.copy(leftPanelWidthFraction = newFraction)
        }
    }

    fun resizeRightPanel(delta: Float) {
        _layout.update { current ->
            val newFraction = (current.rightPanelWidthFraction - delta).coerceIn(0.1f, 0.4f)
            current.copy(rightPanelWidthFraction = newFraction)
        }
    }

    fun resizeBottomPanel(delta: Float) {
        _layout.update { current ->
            val newFraction = (current.bottomPanelHeightFraction - delta).coerceIn(0.1f, 0.5f)
            current.copy(bottomPanelHeightFraction = newFraction)
        }
    }

    fun toggleLeftPanel() {
        _layout.update { it.copy(isLeftPanelCollapsed = !it.isLeftPanelCollapsed) }
    }

    fun toggleRightPanel() {
        _layout.update { it.copy(isRightPanelCollapsed = !it.isRightPanelCollapsed) }
    }

    fun toggleBottomPanel() {
        _layout.update { it.copy(isBottomPanelCollapsed = !it.isBottomPanelCollapsed) }
    }

    fun setLayout(layout: WorkspaceLayout) {
        _layout.value = layout
    }
}
