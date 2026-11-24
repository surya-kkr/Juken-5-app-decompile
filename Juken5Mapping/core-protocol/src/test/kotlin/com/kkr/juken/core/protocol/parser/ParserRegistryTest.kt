package com.kkr.juken.core.protocol.parser

import com.kkr.juken.core.protocol.model.ProtocolVersion
import com.kkr.juken.core.protocol.parser.internal.UnknownPacketHandler
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertTrue
import org.junit.Test

class ParserRegistryTest {

    private val factory = ResponseFactoryImpl()
    private val unknownHandler = mockk<UnknownPacketHandler>(relaxed = true)
    private val registry = ParserRegistry(factory, unknownHandler)
    private val context = ParserContext(
        protocolVersion = ProtocolVersion.JUKEN_5,
        transportType = "Mock",
        connectionId = "test",
        timestamp = 0
    )

    @Test
    fun `parse should return Failure for unknown header and call handler`() {
        val packet = ParsedPacket(ProtocolHeader(0x9999), emptyList(), "9999")
        
        val result = registry.parse(packet, context)
        
        assertTrue(result is ParserResult.Failure)
        verify { unknownHandler.handle(packet, context) }
    }

    @Test
    fun `parse should return Failure for unsupported protocol version`() {
        val packet = ParsedPacket(ProtocolHeader(0x9606), emptyList(), "9606")
        val unsupportedContext = context.copy(protocolVersion = ProtocolVersion.JUKEN_6)
        
        val result = registry.parse(packet, unsupportedContext)
        
        assertTrue(result is ParserResult.Failure)
    }
}
