package com.kkr.juken.feature.mapping.domain.engine.internal

/**
 * Command pattern interface for reversible editor operations.
 */
internal interface MapCommand {
    fun execute()
    fun undo()
}

/**
 * Manages the history stack for the map editor.
 */
internal class UndoRedoManager(private val maxDepth: Int = 50) {
    private val undoStack = mutableListOf<MapCommand>()
    private val redoStack = mutableListOf<MapCommand>()

    fun execute(command: MapCommand) {
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

/**
 * Concrete command for single cell editing.
 */
internal class CellEditCommand(
    private val storage: MapStorage,
    private val index: Int,
    private val newValue: Int
) : MapCommand {
    private var oldValue: Int = 0

    override fun execute() {
        oldValue = storage.getValue(index)
        storage.setValue(index, newValue)
    }

    override fun undo() {
        storage.setValue(index, oldValue)
    }
}

/**
 * Concrete command for batch editing (math, interpolation).
 */
internal class BatchEditCommand(
    private val storage: MapStorage,
    private val deltas: Map<Int, Int> // index to newValue
) : MapCommand {
    private val oldValues = mutableMapOf<Int, Int>()

    override fun execute() {
        deltas.forEach { (index, newValue) ->
            oldValues[index] = storage.getValue(index)
            storage.setValue(index, newValue)
        }
    }

    override fun undo() {
        oldValues.forEach { (index, oldValue) ->
            storage.setValue(index, oldValue)
        }
    }
}
