package com.kkr.juken.feature.validation

import com.kkr.juken.core.protocol.ProtocolCodec
import com.kkr.juken.core.protocol.model.*
import com.kkr.juken.feature.validation.model.ValidationRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HardwareValidationTest {

    private val codec = ProtocolCodec.createJuken5()
    private val validator = ProtocolValidator(codec)

    @Test
    fun `Validator should pass valid handshake exchange`() {
        val request = EcuCommand.Handshake()
        val rawResponse = "9616;Juken 5 Dualband;1.0.0;2.3.4;SN001"
        
        val rules = validator.validateExchange(request, rawResponse)
        
        assertTrue(rules.all { it.status == ValidationRule.Status.PASSED })
    }

    @Test
    fun `Validator should fail on mismatched header`() {
        val request = EcuCommand.Handshake()
        val rawResponse = "9602;0;1;2;3" // Fuel response for handshake
        
        val rules = validator.validateExchange(request, rawResponse)
        
        assertTrue(rules.any { it.name == "HeaderMatch" && it.status == ValidationRule.Status.FAILED })
    }

    @Test
    fun `Discovery should detect second core`() {
        val identity = EcuIdentity(
            ProtocolVersion.JUKEN_5, "Dualband", 
            HardwareVersion(1,0,""), FirmwareVersion(1,0,0,"")
        )
        val vars = mapOf("core" to "2", "rev_limit" to "16000")
        
        val caps = CapabilityDiscovery.discover(identity, vars)
        
        assertTrue(caps.contains(Capability.SECOND_CORE))
        assertTrue(caps.contains(Capability.LIVE_DATA))
    }

    @Test
    fun `Fingerprint should be stable for same identity`() {
        val identity1 = EcuIdentity(
            ProtocolVersion.JUKEN_5, "Model", 
            HardwareVersion(1,0,"1"), FirmwareVersion(2,0,0,"2"), "SN1"
        )
        val identity2 = identity1.copy()
        
        val f1 = FirmwareFingerprint.generate(identity1)
        val f2 = FirmwareFingerprint.generate(identity2)
        
        assertEquals(f1, f2)
    }
}
