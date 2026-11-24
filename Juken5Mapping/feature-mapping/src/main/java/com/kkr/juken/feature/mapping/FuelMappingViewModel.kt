package com.kkr.juken.feature.mapping

import androidx.lifecycle.ViewModel
import com.kkr.juken.domain.model.MapType
import com.kkr.juken.domain.model.SelectionRange
import com.kkr.juken.domain.model.TuningMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FuelMappingViewModel @Inject constructor() : ViewModel() {

    private val _tuningMap = MutableStateFlow(TuningMap(type = MapType.FUEL))
    val tuningMap: StateFlow<TuningMap> = _tuningMap.asStateFlow()

    private val _selection = MutableStateFlow<SelectionRange?>(null)
    val selection: StateFlow<SelectionRange?> = _selection.asStateFlow()

    private var clipboard: Map<Pair<Int, Int>, Double>? = null

    private val history = mutableListOf<TuningMap>()
    private var historyIndex = -1

    init {
        saveToHistory(_tuningMap.value)
    }

    fun updateSelection(range: SelectionRange?) {
        _selection.value = range
    }

    fun updateCell(row: Int, col: Int, value: Double) {
        val current = _tuningMap.value
        val next = current.copyWithUpdate(row, col, value)
        updateMap(next)
    }

    fun undo() {
        if (historyIndex > 0) {
            historyIndex--
            _tuningMap.value = history[historyIndex]
        }
    }

    fun redo() {
        if (historyIndex < history.size - 1) {
            historyIndex++
            _tuningMap.value = history[historyIndex]
        }
    }

    fun interpolate() {
        // ... (existing code)
    }

    fun copy() {
        val range = _selection.value ?: return
        val currentMap = _tuningMap.value
        val data = mutableMapOf<Pair<Int, Int>, Double>()
        for (r in range.minRow..range.maxRow) {
            for (c in range.minCol..range.maxCol) {
                data[r - range.minRow to c - range.minCol] = currentMap.getCell(r, c)
            }
        }
        clipboard = data
    }

    fun paste() {
        val data = clipboard ?: return
        val range = _selection.value ?: return // Paste at start of selection
        val currentMap = _tuningMap.value
        val updates = mutableMapOf<Int, Double>()

        data.forEach { (offset, value) ->
            val targetRow = range.minRow + offset.first
            val targetCol = range.minCol + offset.second
            if (targetRow < currentMap.rows && targetCol < currentMap.cols) {
                updates[targetRow * currentMap.cols + targetCol] = value
            }
        }

        updateMap(currentMap.copyWithUpdates(updates))
    }

    private fun updateMap(newMap: TuningMap) {
        _tuningMap.value = newMap
        saveToHistory(newMap)
    }

    private fun saveToHistory(map: TuningMap) {
        // Remove redo history if we make a new change
        if (historyIndex < history.size - 1) {
            val toRemove = history.size - 1 - historyIndex
            repeat(toRemove) { history.removeAt(history.size - 1) }
        }
        
        history.add(map)
        if (history.size > 50) {
            history.removeAt(0)
        } else {
            historyIndex++
        }
    }
}
