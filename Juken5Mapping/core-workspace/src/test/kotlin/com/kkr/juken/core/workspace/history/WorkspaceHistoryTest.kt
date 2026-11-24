package com.kkr.juken.core.workspace.history

import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class WorkspaceHistoryTest {

    @Test
    fun `execute should run command and add to stack`() {
        val history = WorkspaceHistory()
        val command = mockk<WorkspaceCommand>(relaxed = true)
        
        history.execute(command)
        
        verify { command.execute() }
        assertEquals(true, history.canUndo())
    }

    @Test
    fun `undo should revert command`() {
        val history = WorkspaceHistory()
        val command = mockk<WorkspaceCommand>(relaxed = true)
        
        history.execute(command)
        history.undo()
        
        verify { command.undo() }
        assertEquals(false, history.canUndo())
        assertEquals(true, history.canRedo())
    }
}
