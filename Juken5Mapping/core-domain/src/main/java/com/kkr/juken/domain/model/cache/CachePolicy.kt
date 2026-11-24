package com.kkr.juken.domain.model.cache

/**
 * Strategy for data retrieval from cache or hardware.
 */
enum class CachePolicy {
    USE_CACHE,
    CACHE_ONLY,
    HARDWARE_ONLY,
    REFRESH,
    FORCE_REFRESH
}

/**
 * Complete set of identifiers to validate ECU data cache integrity.
 */
data class CacheValidation(
    val ecuFingerprint: String,
    val firmwareVersion: String,
    val protocolVersion: String,
    val memoryLayoutVersion: Int,
    val calibrationChecksum: String,
    val mapLayoutVersion: Int
)
