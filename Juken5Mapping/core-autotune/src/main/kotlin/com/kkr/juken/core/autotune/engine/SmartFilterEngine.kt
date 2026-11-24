package com.kkr.juken.core.autotune.engine

import com.kkr.juken.domain.model.autotune.RecommendationStatus
import com.kkr.juken.domain.model.autotune.TuningRecommendation
import com.kkr.juken.domain.model.tuning.MapType

/**
 * Filter criteria for tuning recommendations.
 */
data class FilterCriteria(
    val minConfidence: Float = 0.0f,
    val maxFuelChangePercent: Float = 100f,
    val minHitCount: Int = 0,
    val drivingModes: Set<String> = emptySet(),
    val mapTypes: Set<MapType> = emptySet(),
    val statuses: Set<RecommendationStatus> = emptySet(),
    val searchRegex: String? = null,
    val tempRange: ClosedRange<Double>? = null,
    val rpmRange: IntRange? = null,
    val tpsRange: ClosedRange<Double>? = null
)

/**
 * High-performance engine for filtering large sets of recommendations.
 */
class SmartFilterEngine {
    
    fun filter(
        items: List<TuningRecommendation>,
        criteria: FilterCriteria
    ): List<TuningRecommendation> {
        val regex = criteria.searchRegex?.let { 
            try { Regex(it, RegexOption.IGNORE_CASE) } catch (e: Exception) { null }
        }

        return items.filter { item ->
            // 1. Basic Thresholds
            if (item.confidence < criteria.minConfidence) return@filter false
            if (item.hitCount < criteria.minHitCount) return@filter false
            
            val fuelDelta = Math.abs((item.recommendedValue - item.originalValue).toDouble() / item.originalValue) * 100.0
            if (fuelDelta > criteria.maxFuelChangePercent) return@filter false
            
            // 2. Sets
            if (criteria.drivingModes.isNotEmpty() && !criteria.drivingModes.contains(item.drivingMode)) return@filter false
            if (criteria.mapTypes.isNotEmpty() && !criteria.mapTypes.contains(item.mapType)) return@filter false
            if (criteria.statuses.isNotEmpty() && !criteria.statuses.contains(item.status)) return@filter false

            // 3. Ranges
            if (criteria.tempRange != null && item.temperatureAverage !in criteria.tempRange) return@filter false
            if (criteria.rpmRange != null && (item.rpmRange.first < criteria.rpmRange.first || item.rpmRange.last > criteria.rpmRange.last)) return@filter false
            if (criteria.tpsRange != null && (item.tpsRange.start < criteria.tpsRange.start || item.tpsRange.endInclusive > criteria.tpsRange.endInclusive)) return@filter false

            // 4. Search
            if (regex != null) {
                val match = regex.containsMatchIn(item.reason ?: "") || 
                           regex.containsMatchIn(item.drivingMode) ||
                           regex.containsMatchIn(item.mapType.name) ||
                           regex.containsMatchIn(item.id)
                if (!match) return@filter false
            }
            
            true
        }
    }
}
