package com.kkr.juken.core.protocol.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.EnumSet

class EcuIdentityTest {

    @Test
    fun `EcuIdentity should maintain immutability and equality`() {
        val hw = HardwareVersion(1, 0, "1.0")
        val fw = FirmwareVersion(2, 3, 4, "2.3.4")
        val identity1 = EcuIdentity(
            protocolVersion = ProtocolVersion.JUKEN_5,
            ecuName = "Juken 5 Turbo",
            hardwareVersion = hw,
            firmwareVersion = fw,
            serialNumber = "SN12345",
            capabilities = EnumSet.of(Capability.LIVE_DATA, Capability.DIAGNOSTICS)
        )

        val identity2 = identity1.copy(firmwareVersion = fw.copy(patch = 5, raw = "2.3.5"))

        assertEquals("Juken 5 Turbo", identity1.ecuName)
        assertEquals(4, identity1.firmwareVersion.patch)
        assertEquals(5, identity2.firmwareVersion.patch)
        assertEquals(identity1.ecuName, identity2.ecuName)
        assertNotSame(identity1, identity2)
    }

    @Test
    fun `EcuIdentity should default to no capabilities`() {
        val identity = EcuIdentity(
            protocolVersion = ProtocolVersion.JUKEN_5,
            ecuName = "Generic",
            hardwareVersion = HardwareVersion(1, 0, "1"),
            firmwareVersion = FirmwareVersion(1, 0, 0, "1")
        )

        assertTrue(identity.capabilities.isEmpty())
    }
}
