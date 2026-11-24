package com.kkr.juken.core.protocol.model

import java.util.Collections
import java.util.EnumSet

/**
 * Represents the identity and configuration of a detected ECU.
 *
 * @property protocolVersion The protocol revision used by this ECU.
 * @property ecuName The human-readable name of the ECU model (e.g., "Juken 5 Dualband").
 * @property hardwareVersion The hardware revision.
 * @property firmwareVersion The firmware version.
 * @property serialNumber The unique identifier of the ECU, if available.
 * @property capabilities The immutable set of features supported by this specific ECU instance.
 */
data class EcuIdentity(
    val protocolVersion: ProtocolVersion,
    val ecuName: String,
    val hardwareVersion: HardwareVersion,
    val firmwareVersion: FirmwareVersion,
    val serialNumber: String? = null,
    val capabilities: Set<Capability> = Collections.emptySet()
) {
    /**
     * Secondary constructor that ensures capability sets are immutable.
     */
    constructor(
        protocolVersion: ProtocolVersion,
        ecuName: String,
        hardwareVersion: HardwareVersion,
        firmwareVersion: FirmwareVersion,
        serialNumber: String? = null,
        capabilities: EnumSet<Capability>
    ) : this(
        protocolVersion,
        ecuName,
        hardwareVersion,
        firmwareVersion,
        serialNumber,
        Collections.unmodifiableSet(EnumSet.copyOf(capabilities))
    )
}
