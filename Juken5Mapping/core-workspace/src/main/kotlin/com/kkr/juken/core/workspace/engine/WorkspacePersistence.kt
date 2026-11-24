package com.kkr.juken.core.workspace.engine

import com.kkr.juken.domain.model.workspace.WorkspaceLayout

/**
 * Interface for persisting workspace-specific data.
 */
interface WorkspacePersistence {
    suspend fun saveLayout(layout: WorkspaceLayout)
    suspend fun loadLayout(): WorkspaceLayout?
    
    suspend fun saveActiveTool(toolId: String)
    suspend fun loadActiveTool(): String?
    
    suspend fun saveComparisonMode(mode: String)
    suspend fun loadComparisonMode(): String?
}
