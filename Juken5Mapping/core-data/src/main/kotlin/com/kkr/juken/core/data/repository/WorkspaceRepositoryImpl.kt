package com.kkr.juken.core.data.repository

import com.kkr.juken.core.data.db.dao.WorkspaceDao
import com.kkr.juken.core.data.db.entity.WorkspaceEntity
import com.kkr.juken.domain.model.workspace.WorkspaceLayout
import com.kkr.juken.domain.repository.WorkspaceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkspaceRepositoryImpl @Inject constructor(
    private val dao: WorkspaceDao,
    private val scope: CoroutineScope
) : WorkspaceRepository {

    private val currentWorkspaceId = "default"

    override val layout: StateFlow<WorkspaceLayout> = dao.getWorkspace(currentWorkspaceId)
        .map { entity ->
            entity?.let {
                WorkspaceLayout(
                    leftPanelWidthFraction = it.leftPanelWidthFraction,
                    rightPanelWidthFraction = it.rightPanelWidthFraction,
                    bottomPanelHeightFraction = it.bottomPanelHeightFraction,
                    isLeftPanelCollapsed = it.isLeftPanelCollapsed,
                    isRightPanelCollapsed = it.isRightPanelCollapsed,
                    isBottomPanelCollapsed = it.isBottomPanelCollapsed,
                    lastSelectedTool = it.lastSelectedTool,
                    lastSelectedOverlay = it.lastSelectedOverlay
                )
            } ?: WorkspaceLayout()
        }.stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = WorkspaceLayout()
        )

    override suspend fun saveLayout(layout: WorkspaceLayout) {
        dao.insertWorkspace(
            WorkspaceEntity(
                id = currentWorkspaceId,
                leftPanelWidthFraction = layout.leftPanelWidthFraction,
                rightPanelWidthFraction = layout.rightPanelWidthFraction,
                bottomPanelHeightFraction = layout.bottomPanelHeightFraction,
                isLeftPanelCollapsed = layout.isLeftPanelCollapsed,
                isRightPanelCollapsed = layout.isRightPanelCollapsed,
                isBottomPanelCollapsed = layout.isBottomPanelCollapsed,
                lastSelectedTool = layout.lastSelectedTool,
                lastSelectedOverlay = layout.lastSelectedOverlay
            )
        )
    }

    override suspend fun restoreLayout(): WorkspaceLayout {
        return layout.value
    }
}
