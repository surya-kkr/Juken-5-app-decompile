package com.kkr.juken.core.workspace.history

import com.kkr.juken.domain.model.autotune.RecommendationStatus
import com.kkr.juken.domain.model.workspace.WorkspaceLayout

/**
 * Command for changing a recommendation's status.
 */
class RecommendationStatusCommand(
    private val recommendationId: String,
    private val newStatus: RecommendationStatus,
    private val oldStatus: RecommendationStatus,
    private val updateStatus: (String, RecommendationStatus) -> Unit
) : WorkspaceCommand {
    override fun execute() = updateStatus(recommendationId, newStatus)
    override fun undo() = updateStatus(recommendationId, oldStatus)
}

/**
 * Command for changing the workspace layout.
 */
class LayoutChangeCommand(
    private val newLayout: WorkspaceLayout,
    private val oldLayout: WorkspaceLayout,
    private val updateLayout: (WorkspaceLayout) -> Unit
) : WorkspaceCommand {
    override fun execute() = updateLayout(newLayout)
    override fun undo() = updateLayout(oldLayout)
}

/**
 * Higher-level undo/redo commands to wrap history operations.
 */
class UndoCommand(private val history: WorkspaceHistory) : WorkspaceCommand {
    override fun execute() = history.undo()
    override fun undo() = history.redo()
}

class RedoCommand(private val history: WorkspaceHistory) : WorkspaceCommand {
    override fun execute() = history.redo()
    override fun undo() = history.undo()
}
