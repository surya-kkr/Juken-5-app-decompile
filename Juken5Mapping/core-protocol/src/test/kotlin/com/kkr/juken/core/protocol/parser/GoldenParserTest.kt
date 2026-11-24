package com.kkr.juken.core.protocol.parser

import com.kkr.juken.core.protocol.model.*
import com.kkr.juken.core.protocol.parser.internal.PacketReader
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GoldenParserTest {

    private val reader = PacketReader()
    private val registry = ParserRegistry()
    private val context = ParserContext(
        protocolVersion = ProtocolVersion.JUKEN_5,
        transportType = "Mock",
        connectionId = "test-session",
        timestamp = System.currentTimeMillis()
    )

    @Test
    fun `Golden Parse Identity (9616)`() {
        val raw = loadVector("response/9616_identity.txt")
        val parsedPacket = reader.read(raw)
        val result = registry.parse(parsedPacket, context)

        assertTrue(result is ParserResult.Success)
        val response = (result as ParserResult.Success).value as EcuResponse.EcuIdentityResponse
        assertEquals("Juken 5 Turbo", response.identity.ecuName)
        assertEquals("2.3.4", response.identity.firmwareVersion.raw)
    }

    @Test
    fun `Golden Parse Fuel Map (9602)`() {
        val raw = loadVector("response/9602_fuel.txt")
        val parsedPacket = reader.read(raw)
        val result = registry.parse(parsedPacket, context)

        assertTrue(result is ParserResult.Success)
        val response = (result as ParserResult.Success).value as EcuResponse.FuelMapResponse
        assertEquals(0, response.blockIndex)
        assertEquals(listOf(100, 105, 110), response.values)
    }

    @Test
    fun `Golden Parse Global Variables (9606)`() {
        val raw = loadVector("response/9606_global.txt")
        val parsedPacket = reader.read(raw)
        val result = registry.parse(parsedPacket, context)

        assertTrue(result is ParserResult.Success)
        val response = (result as ParserResult.Success).value as EcuResponse.GlobalVariablesResponse
        assertEquals("16000", response.variables["rev_limit"])
        assertEquals("1000", response.variables["warming_up"])
    }

    @Test
    fun `Golden Parse Ack (1A00)`() {
        val raw = loadVector("response/1A00_ack.txt")
        val parsedPacket = reader.read(raw)
        val result = registry.parse(parsedPacket, context)

        assertTrue(result is ParserResult.Success)
        assertTrue((result as ParserResult.Success).value is EcuResponse.AckResponse)
    }

    private fun loadVector(path: String): String {
        return GoldenParserTest::class.java.classLoader.getResourceAsStream("vectors/$path")?.bufferedReader()?.use { it.readText() }
            ?: throw IllegalArgumentException("Vector not found: $path")
    }
}
