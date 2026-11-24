package com.kkr.juken.feature.mapping.domain.engine.internal

/**
 * Manages map data on the internal clipboard.
 */
internal class ClipboardEngine {

    data class ClipboardData(
        val rows: Int,
        val cols: Int,
        val data: IntArray
    )

    private var current: ClipboardData? = null

    fun copy(storage: MapStorage, minRow: Int, minCol: Int, maxRow: Int, maxCol: Int) {
        val rows = maxRow - minRow + 1
        val cols = maxCol - minCol + 1
        val selection = IntArray(rows * cols)
        
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                selection[r * cols + c] = storage.getValue(minRow + r, minCol + c)
            }
        }
        current = ClipboardData(rows, cols, selection)
    }

    fun getPasteDeltas(startRow: Int, startCol: Int, targetRows: Int, targetCols: Int): Map<Int, Int>? {
        val clip = current ?: return null
        val deltas = mutableMapOf<Int, Int>()
        
        for (r in 0 until clip.rows) {
            for (c in 0 until clip.cols) {
                val tr = startRow + r
                val tc = startCol + c
                if (tr < targetRows && tc < targetCols) {
                    deltas[tr * targetCols + tc] = clip.data[r * clip.cols + c]
                }
            }
        }
        return deltas
    }
}
