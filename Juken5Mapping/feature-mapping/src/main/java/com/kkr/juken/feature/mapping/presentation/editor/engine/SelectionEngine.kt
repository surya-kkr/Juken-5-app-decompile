package com.kkr.juken.feature.mapping.presentation.editor.engine

/**
 * Manages map cell selections, including multi-select and drag operations.
 */
class SelectionEngine(
    private val rows: Int,
    private val cols: Int
) {
    private var _selection = mutableSetOf<Int>()
    val selection: Set<Int> get() = _selection

    var anchorIndex: Int? = null

    fun selectCell(row: Int, col: Int, multi: Boolean = false) {
        val index = row * cols + col
        if (row !in 0 until rows || col !in 0 until cols) return

        if (!multi) _selection.clear()
        _selection.add(index)
        anchorIndex = index
    }

    fun selectRange(endRow: Int, endCol: Int) {
        val anchor = anchorIndex ?: return
        val startRow = anchor / cols
        val startCol = anchor % cols

        val minR = minOf(startRow, endRow).coerceIn(0, rows - 1)
        val maxR = maxOf(startRow, endRow).coerceIn(0, rows - 1)
        val minC = minOf(startCol, endCol).coerceIn(0, cols - 1)
        val maxC = maxOf(startCol, endCol).coerceIn(0, cols - 1)

        _selection.clear()
        for (r in minR..maxR) {
            for (c in minC..maxC) {
                _selection.add(r * cols + c)
            }
        }
    }

    fun clear() {
        _selection.clear()
        anchorIndex = null
    }

    fun isSelected(row: Int, col: Int): Boolean {
        return _selection.contains(row * cols + col)
    }
}
