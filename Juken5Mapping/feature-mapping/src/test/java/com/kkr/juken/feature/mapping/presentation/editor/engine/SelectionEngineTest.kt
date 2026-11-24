package com.kkr.juken.feature.mapping.presentation.editor.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SelectionEngineTest {

    @Test
    fun `selectRange should select rectangle area`() {
        val selection = SelectionEngine(10, 10)
        
        selection.selectCell(1, 1) // Anchor
        selection.selectRange(3, 3)
        
        // Should select 1,1 to 3,3 (9 cells)
        assertEquals(9, selection.selection.size)
        assertTrue(selection.isSelected(1, 1))
        assertTrue(selection.isSelected(2, 2))
        assertTrue(selection.isSelected(3, 3))
    }

    @Test
    fun `clear should reset selection`() {
        val selection = SelectionEngine(10, 10)
        selection.selectCell(1, 1)
        selection.clear()
        
        assertTrue(selection.selection.isEmpty())
        assertEquals(null, selection.anchorIndex)
    }
}
