package com.kkr.juken.core.autotune.engine

import kotlin.math.roundToInt

/**
 * Calculates recommended fuel map corrections.
 */
class FuelCorrectionEngine(
    private val maxChangePercent: Int = 10,
    private val safetyMarginPercent: Int = 2
) {

    /**
     * Calculates the new cell value based on AFR error.
     * Formula: New = Old * (Measured AFR / Target AFR)
     */
    fun calculateCorrection(
        oldValue: Int,
        measuredAfr: Double,
        targetAfr: Double
    ): Int {
        if (targetAfr <= 0.0 || measuredAfr <= 0.0) return oldValue
        
        val ratio = measuredAfr / targetAfr
        val rawRecommendation = (oldValue * ratio).roundToInt()
        
        // Apply safety margin (don't lean out too much at once)
        val safeRecommendation = if (ratio < 1.0) {
            // Recommendation is to lean out
            (oldValue * (ratio + (safetyMarginPercent / 100.0))).roundToInt()
        } else {
            rawRecommendation
        }
        
        // Apply hard limits
        val maxAllowed = oldValue + (oldValue * (maxChangePercent / 100.0)).roundToInt()
        val minAllowed = oldValue - (oldValue * (maxChangePercent / 100.0)).roundToInt()
        
        return safeRecommendation.coerceIn(minAllowed, maxAllowed)
    }
}
