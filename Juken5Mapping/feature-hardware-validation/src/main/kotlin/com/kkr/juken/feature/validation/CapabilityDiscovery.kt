package com.kkr.juken.feature.validation

import com.kkr.juken.core.protocol.model.Capability
import com.kkr.juken.core.protocol.model.EcuIdentity

/**
 * Logic for dynamic feature detection.
 */
object CapabilityDiscovery {

    /**
     * Probes identity and variables to determine supported features.
     */
    fun discover(identity: EcuIdentity, variables: Map<String, String>): Set<Capability> {
        val caps = mutableSetOf<Capability>()
        
        // 1. Name based heuristics
        if (identity.ecuName.contains("Dualband", ignoreCase = true)) {
            // Dualband might imply second core or specific feature sets
        }
        
        if (identity.ecuName.contains("Pro", ignoreCase = true) || identity.ecuName.contains("Turbo", ignoreCase = true)) {
            caps.add(Capability.AUTO_MAPPING)
            caps.add(Capability.DATA_LOGGER)
        }

        // 2. Variable based detection
        if (variables.containsKey("core") && variables["core"] == "2") {
            caps.add(Capability.SECOND_CORE)
        }
        
        if (variables.containsKey("rev_limit")) {
            caps.add(Capability.LIVE_DATA)
        }

        // Standard features for all Juken 5
        caps.add(Capability.DIAGNOSTICS)
        caps.add(Capability.CALIBRATION)
        caps.add(Capability.MAINTENANCE)

        return caps
    }
}
