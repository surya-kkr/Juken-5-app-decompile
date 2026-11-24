package com.kkr.juken.core.workspace.coordinator

import com.kkr.juken.domain.model.workspace.WorkspaceLayout
import com.kkr.juken.domain.repository.WorkspaceRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Orchestrates workspace-level business workflows.
 */
@Singleton
class WorkspaceCoordinator @Inject constructor(
    private val repository: WorkspaceRepository
) {
    val layout: StateFlow<WorkspaceLayout> = repository.layout

    suspend fun restoreLayout(): WorkspaceLayout {
        return repository.restoreLayout()
    }

    suspend fun saveLayout(layout: WorkspaceLayout) {
        repository.saveLayout(layout)
    }
}
