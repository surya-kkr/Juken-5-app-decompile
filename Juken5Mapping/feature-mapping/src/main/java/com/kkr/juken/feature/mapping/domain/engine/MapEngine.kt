package com.kkr.juken.feature.mapping.domain.engine

import com.kkr.juken.feature.mapping.domain.engine.internal.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Production-grade orchestrator for professional ECU map editing.
 */
class MapEngine(
    val rows: Int,
    val cols: Int,
    initialData: IntArray? = null
) {
    private val storage = MapStorage(rows, cols, initialData)
    private val history = UndoRedoManager()
    private val batch = BatchOperationEngine(storage)
    private val interpolation = InterpolationEngine(storage)
    private val dirtyTracker = DirtyTracker()
    private val clipboard = ClipboardEngine()
    private val validator = ValidationEngine()

    private val _state = MutableStateFlow(MapEngineState(storage.snapshot()))
    val state: StateFlow<MapEngineState> = _state.asStateFlow()

    /**
     * Edit a single cell.
     */
    fun editCell(row: Int, col: Int, value: Int) {
        val index = row * cols + col
        val command = CellEditCommand(storage, index, value)
        execute(command, setOf(index))
    }

    /**
     * Perform batch addition on a selection.
     */
    fun add(indices: Set<Int>, value: Int) {
        val deltas = batch.add(indices, value)
        execute(BatchEditCommand(storage, deltas), indices)
    }

    /**
     * Perform batch multiplication on a selection.
     */
    fun multiply(indices: Set<Int>, factor: Float) {
        val deltas = batch.multiply(indices, factor)
        execute(BatchEditCommand(storage, deltas), indices)
    }

    /**
     * Perform bilinear interpolation on a selection.
     */
    fun interpolate(minR: Int, minC: Int, maxR: Int, maxC: Int) {
        val deltas = interpolation.interpolate(minR, minC, maxR, maxC)
        if (deltas.isNotEmpty()) {
            execute(BatchEditCommand(storage, deltas), deltas.keys)
        }
    }

    fun undo() {
        history.undo()
        notifyStateChanged()
    }

    fun redo() {
        history.redo()
        notifyStateChanged()
    }

    private fun execute(command: MapCommand, affectedIndices: Set<Int>) {
        history.execute(command)
        dirtyTracker.markDirty(affectedIndices)
        notifyStateChanged()
    }

    private fun notifyStateChanged() {
        _state.update { it.copy(
            data = storage.snapshot(),
            dirtyIndices = dirtyTracker.snapshot(),
            canUndo = history.canUndo(),
            canRedo = history.canRedo()
        ) }
    }
}

data class MapEngineState(
    val data: IntArray,
    val dirtyIndices: Set<Int> = emptySet(),
    val canUndo: Boolean = false,
    val canRedo: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MapEngineState
        if (!data.contentEquals(other.data)) return false
        if (dirtyIndices != other.dirtyIndices) return false
        if (canUndo != other.canUndo) return false
        if (canRedo != other.canRedo) return false
        return true
    }

    override fun hashCode(): Int {
        var result = data.contentHashCode()
        result = 31 * result + dirtyIndices.hashCode()
        result = 31 * result + canUndo.hashCode()
        result = 31 * result + canRedo.hashCode()
        return result
    }
}
