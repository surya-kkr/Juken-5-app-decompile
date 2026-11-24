package com.kkr.juken.core.protocol.model

/**
 * Represents a hardware version of the ECU.
 *
 * @property major Major version number.
 * @property minor Minor version number.
 * @property raw Original ASCII string from the ECU.
 */
data class HardwareVersion(
    val major: Int,
    val minor: Int,
    val raw: String
)

/**
 * Represents a firmware version of the ECU.
 *
 * @property major Major version number.
 * @property minor Minor version number.
 * @property patch Patch version number.
 * @property raw Original ASCII string from the ECU.
 */
data class FirmwareVersion(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val raw: String
)
