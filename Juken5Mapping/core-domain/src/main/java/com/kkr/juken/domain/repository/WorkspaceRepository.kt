package com.kkr.juken.domain.repository

import com.kkr.juken.domain.model.workspace.WorkspaceLayout
import kotlinx.coroutines.flow.StateFlow

/**
 * Handles persistence of the professional workspace layout.
 */
interface WorkspaceRepository {
    val layout: StateFlow<WorkspaceLayout>
    suspend fun saveLayout(layout: WorkspaceLayout)
    suspend fun restoreLayout(): WorkspaceLayout
}
