package com.kkr.juken.core.transport

import com.kkr.juken.core.protocol.ProtocolCodec
import com.kkr.juken.core.transport.internal.HeartbeatManager
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HeartbeatManagerTest {

    private val codec = mockk<ProtocolCodec>()
    private val session = mockk<ConnectionSession>()
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var heartbeatManager: HeartbeatManager

    @Before
    fun setup() {
        heartbeatManager = HeartbeatManager(
            codec = codec,
            intervalMs = 100,
            timeoutMs = 50
        )
    }

    @Test
    fun `HeartbeatManager should signal lost if response times out`() = testScope.runTest {
        val incoming = MutableSharedFlow<ByteArray>()
        every { codec.serialize(any()) } returns "PING"
        every { session.incomingPackets } returns incoming
        coEvery { session.write(any()) } just Runs
        every { session.notifyHeartbeatLost() } just Runs

        heartbeatManager.start(session, testScope)
        
        // Trigger first delay
        advanceTimeBy(150)
        // Heartbeat sent, now wait for timeout
        advanceTimeBy(100)
        
        verify(atLeast = 1) { session.notifyHeartbeatLost() }
        heartbeatManager.stop()
    }
}
