package com.kkr.juken.core.autotune.engine

import com.kkr.juken.core.analysis.StatisticsEngine

/**
 * Evaluates the quality and reliability of tuning data points.
 */
class ConfidenceEngine(
    private val minSamples: Int = 10,
    private val maxAfrSd: Double = 0.5,
    private val tempStabilityMin: Int = 75,
    private val tempStabilityMax: Int = 105
) {

    /**
     * Calculates a confidence score (0.0 to 1.0) for a set of AFR readings.
     */
    fun evaluate(
        afrReadings: DoubleArray,
        coolantTemp: Int,
        batteryVoltage: Double
    ): Double {
        if (afrReadings.size < minSamples) return 0.0
        
        // 1. Temperature Stability Check
        if (coolantTemp < tempStabilityMin || coolantTemp > tempStabilityMax) return 0.1
        
        // 2. Battery Stability Check
        if (batteryVoltage < 12.0) return 0.2
        
        // 3. Mathematical Stability (Standard Deviation)
        val sd = StatisticsEngine.standardDeviation(afrReadings)
        val stabilityScore = (1.0 - (sd / maxAfrSd)).coerceIn(0.0, 1.0)
        
        // 4. Data Density Score
        val densityScore = (afrReadings.size.toDouble() / 100.0).coerceIn(0.0, 1.0)
        
        return (stabilityScore * 0.7 + densityScore * 0.3)
    }
}
