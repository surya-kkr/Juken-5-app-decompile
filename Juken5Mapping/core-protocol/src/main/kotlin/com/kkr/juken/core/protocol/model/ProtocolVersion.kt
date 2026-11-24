package com.kkr.juken.core.protocol.model

/**
 * Supported versions of the Juken ECU protocol.
 */
enum class ProtocolVersion {
    /**
     * Juken 5 standard protocol.
     */
    JUKEN_5,

    /**
     * Juken 5 Pro variant protocol.
     */
    JUKEN_5_PRO,

    /**
     * Placeholder for future Juken 6 protocol support.
     */
    JUKEN_6
}
