package com.kkr.juken.core.workspace.layers

import org.junit.Assert.assertEquals
import org.junit.Test

class LayerStackTest {

    @Test
    fun `addLayer should maintain zIndex order`() {
        val manager = LayerStackManager()
        manager.addLayer(LayerState("1", zIndex = 10))
        manager.addLayer(LayerState("2", zIndex = 5))
        
        val stack = manager.stack.value
        assertEquals("2", stack[0].id)
        assertEquals("1", stack[1].id)
    }

    @Test
    fun `updateLayer should modify specific layer`() {
        val manager = LayerStackManager()
        manager.addLayer(LayerState("1", isVisible = true))
        
        manager.updateLayer("1") { it.copy(isVisible = false) }
        
        assertEquals(false, manager.stack.value[0].isVisible)
    }
}
