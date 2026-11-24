package com.kkr.juken.core.protocol.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.EnumSet

class EcuResponseTest {

    @Test
    fun `Identity response should hold identity`() {
        val identity = EcuIdentity(
            protocolVersion = ProtocolVersion.JUKEN_5,
            ecuName = "Test ECU",
            hardwareVersion = HardwareVersion(1, 0, "1.0"),
            firmwareVersion = FirmwareVersion(2, 0, 0, "2.0.0"),
            capabilities = EnumSet.of(Capability.LIVE_DATA)
        )
        val response = EcuResponse.EcuIdentityResponse(identity)
        assertEquals(identity, response.identity)
    }

    @Test
    fun `LiveData response should validate bounds`() {
        val live = EcuResponse.LiveDataResponse(
            rpm = 1500,
            tps = 15.5,
            temperature = 80,
            batteryVoltage = 13.8
        )
        assertEquals(1500, live.rpm)
        assertEquals(15.5, live.tps, 0.0)
        assertNull(live.manifoldPressure)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `LiveData should reject invalid TPS`() {
        EcuResponse.LiveDataResponse(rpm = 0, tps = 101.0, temperature = 0, batteryVoltage = 0.0)
    }

    @Test
    fun `DiagnosticResponse should hold error codes`() {
        val errors = listOf("E01", "E05")
        val diag = EcuResponse.DiagnosticResponse(0, 2, errors)
        assertEquals(2, diag.activeDtcCount)
        assertEquals(errors, diag.errorCodes)
    }

    @Test
    fun `HistoryResponse should hold entry list`() {
        val entries = listOf(com.kkr.juken.core.protocol.model.HistoryEntry(100, 20, 14.7, 2000L))
        val response = EcuResponse.HistoryResponse(0, entries)
        assertEquals(1, response.entries.size)
        assertEquals(14.7, response.entries[0].afr, 0.0)
    }

    @Test
    fun `Ack and Nack should hold state`() {
        assertTrue(EcuResponse.AckResponse is EcuResponse)
        val nack = EcuResponse.NackResponse("ECU Timeout")
        assertEquals("ECU Timeout", nack.reason)
    }
}
