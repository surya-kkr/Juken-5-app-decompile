package com.kkr.juken.core.protocol.model

/**
 * Enumeration of hardware and firmware capabilities supported by various ECU models.
 * This allows the application to dynamically adjust its UI and features based on the
 * detected ECU identity rather than assuming a fixed feature set.
 */
enum class Capability {
    /**
     * ECU supports real-time data streaming (RPM, TPS, etc.).
     */
    LIVE_DATA,

    /**
     * ECU supports automatic fuel map correction based on target AFR.
     */
    AUTO_MAPPING,

    /**
     * ECU supports internal data logging for later analysis.
     */
    DATA_LOGGER,

    /**
     * ECU supports reading and clearing diagnostic trouble codes (DTC).
     */
    DIAGNOSTICS,

    /**
     * ECU supports dual-core processing or features related to secondary map logic.
     */
    SECOND_CORE,

    /**
     * ECU supports sensor and actuator calibration (e.g., TPS calibration).
     */
    CALIBRATION,

    /**
     * ECU supports maintenance tracking and counters.
     */
    MAINTENANCE
}
