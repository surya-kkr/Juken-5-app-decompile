package com.kkr.juken.feature.validation

import com.kkr.juken.core.protocol.model.EcuIdentity
import java.security.MessageDigest

/**
 * Utility to generate a stable, unique identifier for an ECU's hardware and firmware combination.
 */
object FirmwareFingerprint {

    /**
     * Generates a SHA-256 hash of the identity fields.
     */
    fun generate(identity: EcuIdentity): String {
        val input = "${identity.ecuName}|${identity.hardwareVersion.raw}|${identity.firmwareVersion.raw}|${identity.serialNumber}"
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(input.toByteArray(Charsets.UTF_8))
        return hash.joinToString("") { "%02x".format(it) }
    }
}
