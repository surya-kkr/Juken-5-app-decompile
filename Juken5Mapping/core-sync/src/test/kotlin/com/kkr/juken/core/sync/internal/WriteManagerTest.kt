package com.kkr.juken.core.sync.internal

import com.kkr.juken.domain.model.tuning.MapType
import com.kkr.juken.domain.model.ui.TransferUiState
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WriteManagerTest {

    private val orchestrator = mockk<ReadWriteOrchestrator>()
    private val verifier = mockk<WriteVerifier>()
    private val rollback = mockk<RollbackManager>()
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var writeManager: WriteManager

    @Before
    fun setup() {
        writeManager = WriteManager(orchestrator, verifier, rollback, testScope)
    }

    @Test
    fun `writeMap should verify after each block write`() = testScope.runTest {
        val data = mapOf(0 to List(64) { 100 })
        coEvery { orchestrator.writeBlock(any(), any(), any()) } returns Unit
        coEvery { orchestrator.readBlock(any(), any()) } returns List(64) { 100 }
        every { verifier.verify(any(), any()) } returns true
        
        writeManager.writeMap(MapType.FUEL, data)
        
        assertTrue(writeManager.state.value is TransferUiState.Completed)
        coVerify(exactly = 1) { orchestrator.writeBlock(MapType.FUEL, 0, any()) }
        coVerify(exactly = 1) { orchestrator.readBlock(MapType.FUEL, 0) }
    }

    @Test
    fun `writeMap should fail if verification fails`() = testScope.runTest {
        val data = mapOf(0 to List(64) { 100 })
        coEvery { orchestrator.writeBlock(any(), any(), any()) } returns Unit
        coEvery { orchestrator.readBlock(any(), any()) } returns List(64) { 99 } // mismatch
        every { verifier.verify(any(), any()) } returns false
        
        writeManager.writeMap(MapType.FUEL, data)
        
        assertTrue(writeManager.state.value is TransferUiState.Failed)
    }
}
