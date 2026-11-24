package com.kkr.juken.core.transport

import org.junit.Assert.assertEquals
import org.junit.Test

class ProtocolRecorderTest {

    @Test
    fun `recorder should store entries in order`() {
        val recorder = ProtocolRecorder()
        val entry1 = RecordEntry(
            timestamp = 1000,
            direction = RecordEntry.Direction.TX,
            connectionId = ConnectionId("1"),
            rawBytes = "REQ".toByteArray(),
            rawHex = "524551",
            rawAscii = "REQ",
            transportType = "Mock",
            protocolVersion = "J5"
        )
        val entry2 = entry1.copy(timestamp = 1010, direction = RecordEntry.Direction.RX, rawBytes = "RESP".toByteArray())

        recorder.record(entry1)
        recorder.record(entry2)

        assertEquals(2, recorder.history.value.size)
        assertEquals(entry1, recorder.history.value[0])
        assertEquals(entry2, recorder.history.value[1])
    }
}
