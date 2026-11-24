package com.kkr.juken.feature.mapping.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MapEngineTest {

    @Test
    fun `editCell should update data and mark dirty`() {
        val engine = MapEngine(2, 2)
        engine.editCell(0, 0, 100)
        
        assertEquals(100, engine.state.value.data[0])
        assertTrue(engine.state.value.dirtyIndices.contains(0))
    }

    @Test
    fun `undo should restore previous value`() {
        val engine = MapEngine(2, 2)
        engine.editCell(0, 0, 100)
        assertEquals(100, engine.state.value.data[0])
        
        engine.undo()
        assertEquals(0, engine.state.value.data[0])
    }

    @Test
    fun `batch add should update multiple cells`() {
        val engine = MapEngine(2, 2)
        engine.add(setOf(0, 1), 50)
        
        assertEquals(50, engine.state.value.data[0])
        assertEquals(50, engine.state.value.data[1])
        assertTrue(engine.state.value.dirtyIndices.containsAll(setOf(0, 1)))
    }

    @Test
    fun `interpolation should generate gradients`() {
        val engine = MapEngine(3, 3)
        // Set 4 corners
        engine.editCell(0, 0, 0)
        engine.editCell(0, 2, 100)
        engine.editCell(2, 0, 100)
        engine.editCell(2, 2, 200)
        
        engine.interpolate(0, 0, 2, 2)
        
        // Center cell (1,1) should be around 100 (weighted average of corners)
        assertEquals(100, engine.state.value.data[1 * 3 + 1])
    }
}
