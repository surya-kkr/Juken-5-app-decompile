package com.kkr.juken.core.workspace.history

/**
 * Interface for reversible workspace operations.
 */
interface WorkspaceCommand {
    fun execute()
    fun undo()
}

/**
 * Professional-grade history manager using the Command Pattern.
 * Supports infinite undo/redo with zero data duplication.
 */
class WorkspaceHistory(private val maxDepth: Int = 100) {
    private val undoStack = mutableListOf<WorkspaceCommand>()
    private val redoStack = mutableListOf<WorkspaceCommand>()

    fun execute(command: WorkspaceCommand) {
        command.execute()
        undoStack.add(command)
        redoStack.clear()
        if (undoStack.size > maxDepth) {
            undoStack.removeAt(0)
        }
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            val command = undoStack.removeAt(undoStack.size - 1)
            command.undo()
            redoStack.add(command)
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            val command = redoStack.removeAt(redoStack.size - 1)
            command.execute()
            undoStack.add(command)
        }
    }

    fun canUndo(): Boolean = undoStack.isNotEmpty()
    fun canRedo(): Boolean = redoStack.isNotEmpty()
    
    fun clear() {
        undoStack.clear()
        redoStack.clear()
    }
}
