package com.kkr.juken.core.workspace.engine

import com.kkr.juken.domain.model.workspace.WorkspaceLayout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Logic for managing panel visibility, fractions, and collapsed states.
 */
class PanelManager(initialLayout: WorkspaceLayout = WorkspaceLayout()) {

    private val _layout = MutableStateFlow(initialLayout)
    val layout: StateFlow<WorkspaceLayout> = _layout.asStateFlow()

    fun resizeLeft(delta: Float) {
        _layout.update { current ->
            val newWidth = (current.leftPanelWidthFraction + delta).coerceIn(0.1f, 0.4f)
            current.copy(leftPanelWidthFraction = newWidth)
        }
    }

    fun resizeRight(delta: Float) {
        _layout.update { current ->
            val newWidth = (current.rightPanelWidthFraction - delta).coerceIn(0.1f, 0.4f)
            current.copy(rightPanelWidthFraction = newWidth)
        }
    }

    fun resizeBottom(delta: Float) {
        _layout.update { current ->
            val newHeight = (current.bottomPanelHeightFraction - delta).coerceIn(0.1f, 0.5f)
            current.copy(bottomPanelHeightFraction = newHeight)
        }
    }

    fun toggleLeft() = _layout.update { it.copy(isLeftPanelCollapsed = !it.isLeftPanelCollapsed) }
    fun toggleRight() = _layout.update { it.copy(isRightPanelCollapsed = !it.isRightPanelCollapsed) }
    fun toggleBottom() = _layout.update { it.copy(isBottomPanelCollapsed = !it.isBottomPanelCollapsed) }

    fun updateLayout(update: (WorkspaceLayout) -> WorkspaceLayout) {
        _layout.update(update)
    }
}
