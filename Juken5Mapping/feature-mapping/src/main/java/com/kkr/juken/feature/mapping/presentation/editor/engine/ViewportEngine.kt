package com.kkr.juken.feature.mapping.presentation.editor.engine

import androidx.compose.runtime.Stable
import kotlin.math.floor

/**
 * Manages the visible area, zoom level, and coordinate mapping for the map grid.
 */
@Stable
class ViewportEngine(
    val initialCellWidth: Float = 120f,
    val initialCellHeight: Float = 60f
) {
    var offsetX = 0f
    var offsetY = 0f
    var scale = 1f
    
    var viewWidth = 0
    var viewHeight = 0

    val cellWidth: Float get() = initialCellWidth * scale
    val cellHeight: Float get() = initialCellHeight * scale

    /**
     * Calculates the range of cells currently visible in the viewport.
     */
    fun calculateVisibleRange(rows: Int, cols: Int): VisibleRange {
        if (viewWidth <= 0 || viewHeight <= 0) return VisibleRange.EMPTY

        val startCol = floor(-offsetX / cellWidth).toInt().coerceIn(0, cols - 1)
        val endCol = floor((-offsetX + viewWidth) / cellWidth).toInt().coerceIn(0, cols - 1)
        
        val startRow = floor(-offsetY / cellHeight).toInt().coerceIn(0, rows - 1)
        val endRow = floor((-offsetY + viewHeight) / cellHeight).toInt().coerceIn(0, rows - 1)

        return VisibleRange(startRow, startCol, endRow, endCol)
    }

    fun screenToCell(x: Float, y: Float): Pair<Int, Int> {
        val col = floor((x - offsetX) / cellWidth).toInt()
        val row = floor((y - offsetY) / cellHeight).toInt()
        return row to col
    }

    fun cellToScreen(row: Int, col: Int): Pair<Float, Float> {
        return (col * cellWidth + offsetX) to (row * cellHeight + offsetY)
    }
}

data class VisibleRange(
    val startRow: Int,
    val startCol: Int,
    val endRow: Int,
    val endCol: Int
) {
    companion object {
        val EMPTY = VisibleRange(0, 0, 0, 0)
    }
}
