package com.kkr.juken.domain.model.state

import com.kkr.juken.domain.model.workspace.WorkspaceLayout

data class ProjectMetadata(
    val id: String,
    val name: String,
    val lastModified: Long
)

data class WorkspaceState(
    val currentProject: ProjectMetadata? = null,
    val openedMapTypes: Set<String> = emptySet(),
    val selectedTableIndex: Int = 0,
    val isEditorDirty: Boolean = false,
    val canUndo: Boolean = false,
    val canRedo: Boolean = false,
    val layout: WorkspaceLayout = WorkspaceLayout()
)
