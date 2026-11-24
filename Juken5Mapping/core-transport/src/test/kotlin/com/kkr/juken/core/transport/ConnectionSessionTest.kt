package com.kkr.juken.core.transport

import com.kkr.juken.core.transport.internal.ConnectionSessionImpl
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ConnectionSessionTest {

    private val transport = mockk<Transport>()
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var session: ConnectionSession

    @Before
    fun setup() {
        every { transport.incomingPackets } returns emptyFlow()
        session = ConnectionSessionImpl(
            transport = transport,
            scope = testScope,
            dispatcher = testDispatcher
        )
    }

    @Test
    fun `connect should transition state to CONNECTED on success`() = testScope.runTest {
        coEvery { transport.connect(any()) } just Runs
        
        session.connect("ADDR")
        advanceUntilIdle()
        
        assertEquals(ConnectionState.CONNECTED, session.state.value)
    }

    @Test
    fun `disconnect should transition state to DISCONNECTED`() = testScope.runTest {
        coEvery { transport.disconnect() } just Runs
        
        session.disconnect()
        advanceUntilIdle()
        
        assertEquals(ConnectionState.DISCONNECTED, session.state.value)
    }

    @Test
    fun `session should attempt reconnect on transport failure`() = testScope.runTest {
        coEvery { transport.connect(any()) } throws Exception("Failed")
        
        session.connect("ADDR")
        
        // Wait for first attempt to fail and enter delay for second attempt
        testDispatcher.scheduler.advanceTimeBy(10)
        
        assertEquals(ConnectionState.RECONNECTING, session.state.value)
        assertEquals(1, session.metrics.value.reconnectCount)
        
        // Let it finish all attempts
        advanceUntilIdle()
        assertEquals(ConnectionState.FAILED, session.state.value)
        assertEquals(3, session.metrics.value.reconnectCount)
    }
}
