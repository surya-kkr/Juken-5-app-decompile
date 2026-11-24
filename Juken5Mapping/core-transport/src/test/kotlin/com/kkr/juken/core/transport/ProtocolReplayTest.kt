package com.kkr.juken.core.transport

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProtocolReplayTest {

    @Test
    fun `Sequential Replay should emit RX packets`() = runTest {
        val records = listOf(
            RecordEntry(100, RecordEntry.Direction.TX, ConnectionId("1"), "REQ".toByteArray(), "", "REQ", transportType = "", protocolVersion = ""),
            RecordEntry(110, RecordEntry.Direction.RX, ConnectionId("1"), "RESP".toByteArray(), "", "RESP", transportType = "", protocolVersion = "")
        )
        val replay = ProtocolReplay(records, mode = ProtocolReplay.ReplayMode.SEQUENTIAL, preserveTiming = false, scope = this)
        
        replay.connect("MOCK")
        val rx = replay.incomingPackets.first()
        
        assertEquals("RESP", String(rx))
        replay.disconnect()
    }

    @Test
    fun `Response Matching Replay should return specific RX for TX`() = runTest {
        val records = listOf(
            RecordEntry(100, RecordEntry.Direction.TX, ConnectionId("1"), "A".toByteArray(), "", "A", transportType = "", protocolVersion = ""),
            RecordEntry(110, RecordEntry.Direction.RX, ConnectionId("1"), "B".toByteArray(), "", "B", transportType = "", protocolVersion = ""),
            RecordEntry(200, RecordEntry.Direction.TX, ConnectionId("1"), "C".toByteArray(), "", "C", transportType = "", protocolVersion = ""),
            RecordEntry(210, RecordEntry.Direction.RX, ConnectionId("1"), "D".toByteArray(), "", "D", transportType = "", protocolVersion = "")
        )
        val replay = ProtocolReplay(records, mode = ProtocolReplay.ReplayMode.RESPONSE_MATCHING, scope = this)
        
        replay.connect("MOCK")
        
        replay.write("C".toByteArray())
        val rx = replay.incomingPackets.first()
        
        assertEquals("D", String(rx))
        replay.disconnect()
    }
}
