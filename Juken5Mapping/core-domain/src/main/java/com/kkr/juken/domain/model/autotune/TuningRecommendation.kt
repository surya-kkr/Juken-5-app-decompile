package com.kkr.juken.domain.model.autotune

import com.kkr.juken.domain.model.tuning.MapType

/**
 * A high-fidelity tuning recommendation for a map cell, including physical engine context.
 */
data class TuningRecommendation(
    val id: String,
    val mapType: MapType,
    val cellIndex: Int,
    val originalValue: Int,
    val recommendedValue: Int,
    val confidence: Float,
    val hitCount: Int,
    val averageAfr: Double,
    val targetAfr: Double,
    val drivingMode: String,
    val rpmRange: IntRange,
    val tpsRange: ClosedRange<Double>,
    val temperatureAverage: Double,
    val batteryAverage: Double,
    val lambdaAverage: Double,
    val interpolationWeight: Double,
    val status: RecommendationStatus = RecommendationStatus.PENDING,
    val reason: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

enum class RecommendationStatus {
    PENDING, ACCEPTED, REJECTED, IGNORED, APPLIED, VERIFIED
}
