package com.kkr.juken.feature.mapping.presentation.editor.engine

import androidx.compose.runtime.Stable

/**
 * High-level orchestrator for the map UI grid.
 */
@Stable
class GridEngine(
    val rows: Int,
    val cols: Int
) {
    val viewport = ViewportEngine()
    val selection = SelectionEngine(rows, cols)

    /**
     * Hit tests a screen coordinate to find a cell.
     */
    fun hitTest(x: Float, y: Float): Pair<Int, Int> {
        return viewport.screenToCell(x, y)
    }

    /**
     * Updates viewport size.
     */
    fun onSizeChanged(width: Int, height: Int) {
        viewport.viewWidth = width
        viewport.viewHeight = height
    }
}
