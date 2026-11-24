package com.kkr.juken.core.protocol.parser

import com.kkr.juken.core.protocol.model.ProtocolError
import com.kkr.juken.core.protocol.parser.internal.PacketReader
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PacketReaderTest {

    private val reader = PacketReader()

    @Test
    fun `read should split valid packet correctly`() {
        val raw = "9616;Juken 5;1.0;2.3.4\r\n"
        val packet = reader.read(raw)

        assertEquals(0x9616, packet.header.value)
        assertEquals(listOf("Juken 5", "1.0", "2.3.4"), packet.fields)
        assertEquals("9616;Juken 5;1.0;2.3.4", packet.raw)
    }

    @Test
    fun `read should handle packets without fields`() {
        val raw = "1A00\r\n"
        val packet = reader.read(raw)

        assertEquals(0x1A00, packet.header.value)
        assertTrue(packet.fields.isEmpty())
    }

    @Test(expected = ProtocolError.MalformedResponse::class)
    fun `read should throw on empty input`() {
        reader.read("  \r\n")
    }

    @Test(expected = ProtocolError.MalformedResponse::class)
    fun `read should throw on non-hex header`() {
        reader.read("GHIJ;1;2") // GHIJ is not valid hex
    }
}
