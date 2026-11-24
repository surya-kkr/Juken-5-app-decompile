package com.kkr.juken.core.autotune.engine

import com.kkr.juken.domain.model.autotune.TuningRecommendation

/**
 * Advanced filtering engine for tuning recommendations.
 * Supports compound logic and regex searching.
 */
class FilterEngine {
    
    fun filter(
        items: List<TuningRecommendation>,
        criteria: FilterCriteria
    ): List<TuningRecommendation> {
        val regex = criteria.searchRegex?.let { 
            try { Regex(it, RegexOption.IGNORE_CASE) } catch (e: Exception) { null }
        }

        return items.filter { item ->
            if (item.confidence < criteria.minConfidence) return@filter false
            
            val fuelDelta = Math.abs((item.recommendedValue - item.originalValue).toDouble() / item.originalValue) * 100.0
            if (fuelDelta > criteria.maxFuelChangePercent) return@filter false
            
            if (criteria.drivingModes.isNotEmpty() && !criteria.drivingModes.contains(item.drivingMode)) return@filter false
            
            if (regex != null) {
                val match = regex.containsMatchIn(item.reason ?: "") || 
                           regex.containsMatchIn(item.drivingMode) ||
                           regex.containsMatchIn(item.mapType.name)
                if (!match) return@filter false
            }
            
            true
        }
    }
}
