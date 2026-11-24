package com.kkr.juken.feature.mapping.presentation.editor.engine

import org.junit.Assert.assertEquals
import org.junit.Test

class GridEngineTest {

    @Test
    fun `hitTest should return correct cell coordinates`() {
        val grid = GridEngine(10, 10)
        grid.onSizeChanged(1000, 1000)
        
        // At scale 1.0 and offset 0,0, a click at (125, 65) should be cell (1, 1)
        // Default cell is 120x60
        val cell = grid.hitTest(125f, 65f)
        
        assertEquals(1, cell.first) // row
        assertEquals(1, cell.second) // col
    }

    @Test
    fun `visibleRange should calculate correctly with offset`() {
        val grid = GridEngine(20, 20)
        grid.onSizeChanged(500, 500)
        
        // Scroll down and right
        grid.viewport.offsetX = -200f
        grid.viewport.offsetY = -150f
        
        val range = grid.viewport.calculateVisibleRange(20, 20)
        
        // StartCol: floor(200 / 120) = 1
        assertEquals(1, range.startCol)
        // StartRow: floor(150 / 60) = 2
        assertEquals(2, range.startRow)
    }
}
