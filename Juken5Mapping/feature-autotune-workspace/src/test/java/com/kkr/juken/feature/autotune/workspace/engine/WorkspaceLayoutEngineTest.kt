package com.kkr.juken.feature.autotune.workspace.engine

import org.junit.Assert.assertEquals
import org.junit.Test

class WorkspaceLayoutEngineTest {

    @Test
    fun `resizeLeftPanel should update fraction within bounds`() {
        val engine = WorkspaceLayoutEngine()
        
        // Default is 0.2f
        engine.resizeLeftPanel(0.1f)
        assertEquals(0.3f, engine.layout.value.leftPanelWidthFraction, 0.001f)
        
        // Exceed max (0.4f)
        engine.resizeLeftPanel(1.0f)
        assertEquals(0.4f, engine.layout.value.leftPanelWidthFraction, 0.001f)
    }

    @Test
    fun `toggleRightPanel should flip visibility`() {
        val engine = WorkspaceLayoutEngine()
        assertEquals(false, engine.layout.value.isRightPanelCollapsed)
        
        engine.toggleRightPanel()
        assertEquals(true, engine.layout.value.isRightPanelCollapsed)
    }
}
