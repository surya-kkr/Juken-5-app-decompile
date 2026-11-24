package com.kkr.juken.core.workspace.engine

import com.kkr.juken.core.workspace.history.WorkspaceHistory
import com.kkr.juken.core.workspace.layers.LayerStackManager
import com.kkr.juken.domain.model.workspace.WorkspaceLayout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * High-level orchestrator for the professional tuning workspace.
 */
class WorkspaceEngine(
    initialLayout: WorkspaceLayout = WorkspaceLayout()
) {
    val panels = PanelManager(initialLayout)
    val history = WorkspaceHistory()
    val layers = LayerStackManager()
    val comparison = ComparisonEngine()

    private val _activeTool = MutableStateFlow("POINTER")
    val activeTool: StateFlow<String> = _activeTool.asStateFlow()

    fun selectTool(toolId: String) {
        _activeTool.value = toolId
    }

    val layout: StateFlow<WorkspaceLayout> = panels.layout

    fun updateLayout(update: (WorkspaceLayout) -> WorkspaceLayout) {
        panels.updateLayout(update)
    }
}
