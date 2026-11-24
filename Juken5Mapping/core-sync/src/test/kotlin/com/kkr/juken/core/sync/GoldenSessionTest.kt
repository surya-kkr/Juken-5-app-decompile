package com.kkr.juken.core.sync

import com.kkr.juken.core.protocol.ProtocolCodec
import com.kkr.juken.core.protocol.model.*
import com.kkr.juken.core.protocol.parser.ParserResult
import com.kkr.juken.core.transport.ConnectionSession
import com.kkr.juken.core.transport.ConnectionState
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.LinkedList
import java.util.Queue

@OptIn(ExperimentalCoroutinesApi::class)
class GoldenSessionTest {

    private val codec = mockk<ProtocolCodec>()
    private val session = mockk<ConnectionSession>()
    private val listener = mockk<SyncDataListener>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var syncEngine: SyncEngine
    private val incomingPackets = MutableSharedFlow<ByteArray>(replay = 10)
    private val responseQueue: Queue<EcuResponse> = LinkedList()
    private val sessionState = MutableStateFlow(ConnectionState.CONNECTED)

    @Before
    fun setup() {
        every { session.incomingPackets } returns incomingPackets
        every { session.state } returns sessionState
        every { codec.serialize(any()) } returns "MOCK_TX"
        
        coEvery { session.write(any()) } coAnswers {
            yield()
            incomingPackets.emit("MOCK_RX".toByteArray())
        }
        
        every { codec.decode(any(), any()) } answers {
            val resp = responseQueue.poll() ?: throw Exception("Empty Queue")
            ParserResult.Success(resp)
        }

        syncEngine = SyncEngine(
            codec = codec,
            session = session,
            listener = listener,
            retryPolicy = RetryPolicy(maxRetries = 1, timeoutMs = 1000),
            scope = testScope
        )
    }

    @Test
    fun `Sync with Golden Responses should succeed`() = testScope.runTest {
        fillResponseQueue()

        syncEngine.start("ADDR")
        advanceUntilIdle()
        
        assertEquals(SyncState.READY, syncEngine.progress.value.currentState)
        coVerify(atLeast = 1) { listener.onEcuIdentityReceived(any()) }
    }

    private fun fillResponseQueue() {
        responseQueue.clear()
        responseQueue.add(createIdentityResponse())
        responseQueue.add(EcuResponse.GlobalVariablesResponse(emptyMap()))
        repeat(21) { responseQueue.add(EcuResponse.FuelMapResponse(it, emptyList())) }
        repeat(21) { responseQueue.add(EcuResponse.BaseMapResponse(it, emptyList())) }
        repeat(21) { responseQueue.add(EcuResponse.InjectorMapResponse(it, emptyList())) }
        repeat(21) { responseQueue.add(EcuResponse.IgnitionMapResponse(it, emptyList())) }
        repeat(21) { responseQueue.add(EcuResponse.HistoryResponse(it, emptyList())) }
    }

    private fun createIdentityResponse() = EcuResponse.EcuIdentityResponse(
        EcuIdentity(
            ProtocolVersion.JUKEN_5, "Mock", 
            HardwareVersion(1,0,""), FirmwareVersion(1,0,0,"")
        )
    )
}
