package com.kkr.juken.core.protocol.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ProtocolErrorTest {

    @Test
    fun `ProtocolError should provide descriptive messages`() {
        val malformed = ProtocolError.MalformedResponse("raw", "Bad format")
        val rejected = ProtocolError.CommandRejected("ECU Busy")
        val unsupported = ProtocolError.UnsupportedCommand(EcuCommand.Handshake())

        assertEquals("Bad format", malformed.message)
        assertTrue(rejected.message.contains("ECU Busy"))
        assertTrue(unsupported.message.contains("Handshake"))
    }

    @Test
    fun `InvalidChecksum should have constant message`() {
        assertEquals("Response checksum mismatch", ProtocolError.InvalidChecksum.message)
    }
}
