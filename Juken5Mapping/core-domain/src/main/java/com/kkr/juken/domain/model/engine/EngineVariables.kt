package com.kkr.juken.domain.model.engine

/**
 * High-level engine configuration parameters extracted from the ECU.
 * Replaces the legacy SharedPreferences based storage for core parameters.
 */
data class EngineVariables(
    val vvtcRpm: Int = 1000,
    val revLimit: Int = 16000,
    val warmingUpTemp: Int = 1000,
    val tssMode: Int = 1,
    val fuelStart: Int = 0,
    val idleRpmPosition: Int = 1200,
    val jjsoreRpm: Int = 60,
    val rangeLo: Int = 14,
    val rangeMid: Int = 16,
    val rangeHi: Int = 31,
    val autoIgTemp: Int = 85,
    val protectEnable: Int = 0,
    val protectLimit: Int = 0,
    val protectTemp: Double = 60.0,
    val suhuFan: Int = 600,
    // Add other fields from the legacy mapping (74 total fields) as needed
) {
    init {
        require(revLimit <= 20000) { "Rev limit exceeds hardware safety" }
    }
}
