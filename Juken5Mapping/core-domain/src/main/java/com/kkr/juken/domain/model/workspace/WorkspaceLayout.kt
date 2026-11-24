package com.kkr.juken.domain.model.workspace

/**
 * Persistable state of the professional workspace layout.
 */
data class WorkspaceLayout(
    val leftPanelWidthFraction: Float = 0.2f,
    val rightPanelWidthFraction: Float = 0.2f,
    val bottomPanelHeightFraction: Float = 0.25f,
    val isLeftPanelCollapsed: Boolean = false,
    val isRightPanelCollapsed: Boolean = false,
    val isBottomPanelCollapsed: Boolean = false,
    val lastSelectedTool: String = "POINTER",
    val lastSelectedOverlay: String = "HEATMAP",
    val lastSearchQuery: String = ""
)
