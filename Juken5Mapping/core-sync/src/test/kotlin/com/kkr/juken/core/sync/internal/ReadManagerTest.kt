package com.kkr.juken.core.sync.internal

import com.kkr.juken.domain.model.tuning.MapType
import com.kkr.juken.domain.model.ui.TransferUiState
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReadManagerTest {

    private val orchestrator = mockk<ReadWriteOrchestrator>()
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var readManager: ReadManager

    @Before
    fun setup() {
        readManager = ReadManager(orchestrator, testScope) { _, _, _ -> }
    }

    @Test
    fun `readMap should progress through all blocks and complete`() = testScope.runTest {
        coEvery { orchestrator.readBlock(any(), any()) } returns List(64) { 100 }
        
        readManager.readMap(MapType.FUEL)
        
        assertTrue(readManager.state.value is TransferUiState.Completed)
        coVerify(exactly = 21) { orchestrator.readBlock(MapType.FUEL, any()) }
    }

    @Test
    fun `readMap should fail if orchestrator throws`() = testScope.runTest {
        coEvery { orchestrator.readBlock(any(), any()) } throws Exception("Socket Timeout")
        
        readManager.readMap(MapType.FUEL)
        
        assertTrue(readManager.state.value is TransferUiState.Failed)
        assertEquals("Socket Timeout", (readManager.state.value as TransferUiState.Failed).reason)
    }
}
